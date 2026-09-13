package net.stln.magitech.core.api.field_effect.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.content.network.FieldEffectRenderPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * クライアント側でフィールド効果範囲を保持するキャッシュです。
 * Client-side cache that stores field-effect ranges.
 */
public final class FieldEffectClientCache {
    private static final FieldEffectClientCache INSTANCE = new FieldEffectClientCache();

    private final List<RangeEntry> entries = new ArrayList<>();
    private final Map<Long, List<RangeEntry>> chunkIndex = new HashMap<>();
    private long revision;

    private FieldEffectClientCache() {
    }

    /**
     * シングルトンのキャッシュインスタンスを返します。
     * Returns the singleton cache instance.
     */
    public static FieldEffectClientCache getInstance() {
        return INSTANCE;
    }

    /**
     * ネットワークから受信した範囲更新を適用します。
     * Applies a range update received from the network.
     */
    public void apply(FieldEffectRenderPayload payload) {
        Objects.requireNonNull(payload, "payload");

        switch (payload.action()) {
            case CLEAR -> clear();
            case FULL_SYNC -> replaceAll(payload.ranges());
            case UPSERT_SOURCE -> payload.source().ifPresent(source -> replaceSourceRanges(source, payload.ranges()));
            case REMOVE_SOURCE -> payload.source().ifPresent(this::removeSourceRanges);
        }
    }

    /**
     * 保持しているすべての範囲を削除します。
     * Removes all cached ranges.
     */
    public void clear() {
        entries.clear();
        chunkIndex.clear();
        revision++;
    }

    /**
     * キャッシュ全体を指定範囲で置き換えます。
     * Replaces the entire cache with the specified ranges.
     */
    public void replaceAll(List<RangeEntry> ranges) {
        clear();
        addAll(ranges);
    }

    /**
     * 指定ソースの範囲を置き換えます。
     * Replaces all ranges belonging to the specified source.
     */
    public void replaceSourceRanges(BlockPos source, List<RangeEntry> ranges) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(ranges, "ranges");

        removeSourceRanges(source);
        addAll(filterRangesForSource(source, ranges));
        revision++;
    }

    /**
     * 指定ソースの範囲を削除します。
     * Removes all ranges belonging to the specified source.
     *
     * @return 1件以上削除した場合はtrue / true if at least one range was removed
     */
    public boolean removeSourceRanges(BlockPos source) {
        Objects.requireNonNull(source, "source");

        boolean removed = entries.removeIf(entry -> entry.source().equals(source));
        if (removed) {
            rebuildChunkIndex();
            revision++;
        }
        return removed;
    }

    /**
     * 指定ソースに紐づく範囲のスナップショットを返します。
     * Returns an immutable snapshot of ranges belonging to a source.
     */
    public List<RangeEntry> getRangesBySource(BlockPos source) {
        Objects.requireNonNull(source, "source");

        List<RangeEntry> result = new ArrayList<>();
        for (RangeEntry entry : entries) {
            if (entry.source().equals(source)) {
                result.add(entry);
            }
        }
        return List.copyOf(result);
    }

    /**
     * クライアントに保持されている全範囲の不変スナップショットを返します。
     * Returns an immutable snapshot of all ranges held by the client.
     */
    public List<RangeEntry> getEntriesSnapshot() {
        return List.copyOf(entries);
    }

    /**
     * 範囲キャッシュの更新番号を返します。
     * Returns the update revision of the range cache.
     *
     * <p>描画側はこの番号を使って、範囲が変化したときだけ境界メッシュを再構築できます。</p>
     * <p>The renderer can use this value to rebuild the boundary mesh only when the ranges change.</p>
     *
     * @return キャッシュ更新番号 / cache update revision
     */
    public long getRevision() {
        return revision;
    }

    /**
     * 指定位置に適用される影響を範囲から合成して返します。
     * Merges and returns the influences applied to a position by the cached ranges.
     */
    public FieldInfluenceInstance getFieldEffectInstance(BlockPos pos) {
        Objects.requireNonNull(pos, "pos");

        Map<FieldInfluenceType, Integer> merged = new HashMap<>();
        List<RangeEntry> candidates = chunkIndex.get(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
        if (candidates == null || candidates.isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        for (RangeEntry entry : candidates) {
            if (!entry.contains(pos)) {
                continue;
            }

            for (FieldInfluence influence : entry.instance().fieldInfluences()) {
                merged.merge(influence.type(), influence.intensity(), Integer::sum);
            }
        }

        if (merged.isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        Set<FieldInfluence> result = new HashSet<>();
        for (Map.Entry<FieldInfluenceType, Integer> entry : merged.entrySet()) {
            result.add(new FieldInfluence(entry.getKey(), entry.getValue()));
        }
        return new FieldInfluenceInstance(result);
    }

    /**
     * キャッシュされた全範囲の各位置を一度ずつコールバックへ渡します。
     * Passes each position in the cached ranges to the callback once.
     */
    public void forEachPosInRanges(Level level, BiConsumer<BlockPos, FieldInfluenceInstance> consumer) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(consumer, "consumer");

        Set<Long> visited = new HashSet<>();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (RangeEntry entry : entries) {

            for (int x = entry.from().getX(); x <= entry.to().getX(); x++) {
                for (int y = entry.from().getY(); y <= entry.to().getY(); y++) {
                    for (int z = entry.from().getZ(); z <= entry.to().getZ(); z++) {
                        cursor.set(x, y, z);
                        long key = cursor.asLong();
                        if (!visited.add(key)) {
                            continue;
                        }

                        BlockPos pos = BlockPos.of(key);

                        consumer.accept(pos, getFieldEffectInstance(pos));
                    }
                }
            }
        }
    }

    private void addAll(List<RangeEntry> ranges) {
        Objects.requireNonNull(ranges, "ranges");

        for (RangeEntry entry : ranges) {
            if (entry == null) {
                continue;
            }

            entries.add(entry);
            indexEntry(entry);
        }
    }

    private List<RangeEntry> filterRangesForSource(BlockPos source, List<RangeEntry> ranges) {
        List<RangeEntry> result = new ArrayList<>();
        for (RangeEntry entry : ranges) {
            if (entry != null && entry.source().equals(source)) {
                result.add(entry);
            }
        }
        return result;
    }

    private void rebuildChunkIndex() {
        chunkIndex.clear();
        for (RangeEntry entry : entries) {
            indexEntry(entry);
        }
    }

    private void indexEntry(RangeEntry entry) {
        int minChunkX = entry.from().getX() >> 4;
        int maxChunkX = entry.to().getX() >> 4;
        int minChunkZ = entry.from().getZ() >> 4;
        int maxChunkZ = entry.to().getZ() >> 4;

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                long key = ChunkPos.asLong(chunkX, chunkZ);
                chunkIndex.computeIfAbsent(key, ignored -> new ArrayList<>()).add(entry);
            }
        }
    }
}

