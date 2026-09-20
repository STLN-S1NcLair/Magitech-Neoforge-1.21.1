package net.stln.magitech.core.api.field_effect.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.core.api.field_effect.sync.FieldEffectCacheSyncManager;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * サーバーワールド内のフィールド効果範囲を管理・保存します。
 * Manages and persists field-effect ranges in a server world.
 */
public class FieldEffectManager extends SavedData {
    private static final String DATA_NAME = "magitech_field_effect";

    private static final String TAG_ENTRIES = "Entries";
    private static final String TAG_FROM = "From";
    private static final String TAG_TO = "To";
    private static final String TAG_SOURCE = "Source";
    private static final String TAG_EFFECTS = "Effects";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_INTENSITY = "Intensity";

    private final List<RangeEntry> entries = new ArrayList<>();
    private final Map<Long, List<RangeEntry>> chunkIndex = new HashMap<>();

    // 効果がない範囲は登録しない / Ranges without effects are not registered
//    public void addRange(BlockPos from, BlockPos to, BlockPos source) {
//        addRange(from, to, source, new FieldEffectInstance(Set.of()));
//    }

    /**
     * 指定座標範囲にフィールド影響を追加し、クライアントへ同期します。
     * Adds a field influence to a coordinate range and synchronizes it to clients.
     */
    public void addRange(ServerLevel level, BlockPos from, BlockPos to, BlockPos source, FieldInfluenceInstance instance) {
        Objects.requireNonNull(level, "level");

        addRangeInternal(from, to, source, instance);
        FieldEffectCacheSyncManager.syncSource(level, source);
    }

    /**
     * 指定された範囲エントリを追加し、クライアントへ同期します。
     * Adds a range entry and synchronizes it to clients.
     */
    public void addRange(ServerLevel level, RangeEntry entry) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(entry, "entry");

        addRangeInternal(entry.from(), entry.to(), entry.source(), entry.instance());
        FieldEffectCacheSyncManager.syncSource(level, entry.source());
    }

    private void addRangeInternal(BlockPos from, BlockPos to, BlockPos source, FieldInfluenceInstance instance) {
        BlockPos min = new BlockPos(
                Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()),
                Math.min(from.getZ(), to.getZ())
        );
        BlockPos max = new BlockPos(
                Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()),
                Math.max(from.getZ(), to.getZ())
        );

        RangeEntry entry = new RangeEntry(min, max, source.immutable(), sanitize(instance));
        entries.add(entry);
        indexEntry(entry);
        setDirty();
    }

    /**
     * 登録済み範囲の不変スナップショットを返します。
     * Returns an immutable snapshot of all registered ranges.
     */
    public List<RangeEntry> getEntriesSnapshot() {
        return List.copyOf(entries);
    }

    /**
     * 指定ソースに紐づく範囲のスナップショットを返します。
     * Returns a snapshot of ranges belonging to the specified source.
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
     * 指定ソースの範囲を削除し、削除数を返します。
     * Removes a source's ranges and returns the number removed.
     */
    public int removeRangesBySource(ServerLevel level, BlockPos source) {
        Objects.requireNonNull(level, "level");

        int removed = removeRangesBySourceInternal(source);
        if (removed > 0) {
            FieldEffectCacheSyncManager.removeSource(level, source);
        }
        return removed;
    }

    private int removeRangesBySourceInternal(BlockPos source) {
        int before = entries.size();
        entries.removeIf(entry -> entry.source().equals(source));
        int removed = before - entries.size();

        if (removed > 0) {
            rebuildChunkIndex();
            setDirty();
        }

        return removed;
    }

    /**
     * 指定ソースの範囲を置き換え、クライアントへ同期します。
     * Replaces a source's ranges and synchronizes the result to clients.
     */
    public void replaceRangesBySource(ServerLevel level, BlockPos source, Collection<RangeEntry> ranges) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(ranges, "ranges");

        removeRangesBySourceInternal(source);

        for (RangeEntry range : ranges) {
            if (range == null || !source.equals(range.source())) {
                continue;
            }
            addRangeInternal(range.from(), range.to(), source, range.instance());
        }

        if (getRangesBySource(source).isEmpty()) {
            FieldEffectCacheSyncManager.removeSource(level, source);
        } else {
            FieldEffectCacheSyncManager.syncSource(level, source);
        }
    }

    /**
     * すべての範囲を削除し、クライアントへ通知します。
     * Removes all ranges and notifies clients.
     */
    public void clear(ServerLevel level) {
        Objects.requireNonNull(level, "level");

        if (entries.isEmpty()) {
            return;
        }

        entries.clear();
        chunkIndex.clear();
        setDirty();
        FieldEffectCacheSyncManager.clear(level);
    }

    /**
     * ロード済みチャンク内の範囲位置をコールバックへ渡します。
     * Passes positions in loaded chunks covered by ranges to a callback.
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
                        if (!level.hasChunkAt(pos)) {
                            continue;
                        }
                        consumer.accept(pos, getFieldEffectInstance(pos));
                    }
                }
            }
        }
    }

    /**
     * 指定位置に適用される影響を重複範囲から合成して返します。
     * Merges overlapping ranges and returns the influences applied at a position.
     */
    public FieldInfluenceInstance getFieldEffectInstance(BlockPos pos) {
        Map<FieldInfluenceType, Integer> merged = new HashMap<>();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        List<RangeEntry> candidates = chunkIndex.get(ChunkPos.asLong(chunkX, chunkZ));
        if (candidates == null || candidates.isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        for (RangeEntry entry : candidates) {
            if (!entry.contains(pos)) {
                continue;
            }

            // 重複範囲は同タイプごとに強度を合算する / Sum intensities by type across overlapping ranges
            for (FieldInfluence effect : entry.instance().fieldInfluences()) {
                merged.merge(effect.type(), effect.intensity(), Integer::sum);
            }
        }

        if (merged.isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        Set<FieldInfluence> result = new HashSet<>();
        for (Map.Entry<FieldInfluenceType, Integer> e : merged.entrySet()) {
            result.add(new FieldInfluence(e.getKey(), e.getValue()));
        }
        return new FieldInfluenceInstance(result);
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

    private static FieldInfluenceInstance sanitize(FieldInfluenceInstance instance) {
        if (instance == null || instance.fieldInfluences() == null || instance.fieldInfluences().isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        Set<FieldInfluence> normalized = new HashSet<>();
        for (FieldInfluence effect : instance.fieldInfluences()) {
            if (effect != null && effect.type() != null && effect.intensity() != 0) {
                normalized.add(effect);
            }
        }
        return new FieldInfluenceInstance(normalized);
    }

    private static FieldEffectManager load(CompoundTag tag) {
        FieldEffectManager manager = new FieldEffectManager();
        ListTag entriesTag = tag.getList(TAG_ENTRIES, Tag.TAG_COMPOUND);

        for (Tag raw : entriesTag) {
            if (!(raw instanceof CompoundTag entryTag)) {
                continue;
            }
            if (!entryTag.contains(TAG_FROM, Tag.TAG_LONG)
                    || !entryTag.contains(TAG_TO, Tag.TAG_LONG)
                    || !entryTag.contains(TAG_SOURCE, Tag.TAG_LONG)) {
                continue;
            }

            BlockPos from = BlockPos.of(entryTag.getLong(TAG_FROM));
            BlockPos to = BlockPos.of(entryTag.getLong(TAG_TO));
            BlockPos source = BlockPos.of(entryTag.getLong(TAG_SOURCE));

            Set<FieldInfluence> effects = new HashSet<>();
            ListTag effectsTag = entryTag.getList(TAG_EFFECTS, Tag.TAG_COMPOUND);
            for (Tag effectRaw : effectsTag) {
                if (!(effectRaw instanceof CompoundTag effectTag)) {
                    continue;
                }
                if (!effectTag.contains(TAG_TYPE, Tag.TAG_STRING) || !effectTag.contains(TAG_INTENSITY, Tag.TAG_INT)) {
                    continue;
                }

                FieldInfluenceType type = null;
                String serializedType = effectTag.getString(TAG_TYPE);
                type = MagitechRegistries.FIELD_INFLUENCE_TYPE.get(ResourceLocation.parse(serializedType));
                if (type == null) {
                    continue;
                }

                int intensity = effectTag.getInt(TAG_INTENSITY);
                if (intensity == 0) {
                    continue;
                }
                effects.add(new FieldInfluence(type, intensity));
            }

            manager.addRangeInternal(from.immutable(), to.immutable(), source.immutable(), new FieldInfluenceInstance(effects));
        }

        return manager;
    }

    private static final Factory<FieldEffectManager> FACTORY = new Factory<>(
            FieldEffectManager::new,
            (tag, provider) -> FieldEffectManager.load(tag),
            null
    );

    /**
     * ワールドのフィールド効果管理データを取得します。
     * Gets the field-effect manager data for a world.
     */
    public static FieldEffectManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }

    /**
     * フィールド効果範囲をNBTへ保存します。
     * Saves field-effect ranges to NBT.
     */
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag entriesTag = new ListTag();

        for (RangeEntry entry : entries) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putLong(TAG_FROM, entry.from().asLong());
            entryTag.putLong(TAG_TO, entry.to().asLong());
            entryTag.putLong(TAG_SOURCE, entry.source().asLong());

            ListTag effectsTag = new ListTag();
            for (FieldInfluence effect : entry.instance().fieldInfluences()) {
                CompoundTag effectTag = new CompoundTag();
                effectTag.putString(TAG_TYPE, MagitechRegistries.FIELD_INFLUENCE_TYPE.getKey(effect.type()).toString());
                effectTag.putInt(TAG_INTENSITY, effect.intensity());
                effectsTag.add(effectTag);
            }
            entryTag.put(TAG_EFFECTS, effectsTag);

            entriesTag.add(entryTag);
        }

        tag.put(TAG_ENTRIES, entriesTag);
        return tag;
    }
}
