package net.stln.magitech.core.api.field_effect.sync;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.stln.magitech.content.network.FieldEffectRenderPayload;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.data.FieldEffectManager;
import net.stln.magitech.core.api.field_effect.data.RangeEntry;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * サーバーのフィールド効果範囲をクライアントへ同期するAPIです。
 * API for synchronizing server field-effect ranges to clients.
 */
public final class FieldEffectCacheSyncManager {
    private FieldEffectCacheSyncManager() {
    }

    /**
     * 範囲を追加して同期します。
     * Adds a range and synchronizes it.
     */
    public static void addRangeAndSync(ServerLevel level, BlockPos from, BlockPos to, BlockPos source, FieldInfluenceInstance instance) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(instance, "instance");

        FieldEffectManager.get(level).addRange(level, from, to, source, instance);
    }

    /**
     * 指定ソースの単一範囲を置き換えて同期します。
     * Replaces a source's single range and synchronizes it.
     */
    public static void replaceSourceAndSync(ServerLevel level, BlockPos from, BlockPos to, BlockPos source, FieldInfluenceInstance instance) {
        replaceSourceAndSync(level, source, List.of(new RangeEntry(from, to, source, instance)));
    }

    /**
     * 指定ソースの範囲群を置き換えて同期します。
     * Replaces a source's ranges and synchronizes them.
     */
    public static void replaceSourceAndSync(ServerLevel level, BlockPos source, Collection<RangeEntry> ranges) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(ranges, "ranges");

        FieldEffectManager.get(level).replaceRangesBySource(level, source, ranges);
    }

    /**
     * 指定ソースの範囲を削除して同期します。
     * Removes a source's ranges and synchronizes the removal.
     */
    public static void removeSourceAndSync(ServerLevel level, BlockPos source) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(source, "source");

        FieldEffectManager.get(level).removeRangesBySource(level, source);
    }

    /**
     * すべての範囲を削除して同期します。
     * Removes all ranges and synchronizes the removal.
     */
    public static void clearAndSync(ServerLevel level) {
        Objects.requireNonNull(level, "level");

        FieldEffectManager.get(level).clear(level);
    }

    /**
     * 指定ソースの現在の範囲を全プレイヤーへ送信します。
     * Sends the current ranges for a source to all players.
     */
    public static void syncSource(ServerLevel level, BlockPos source) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(source, "source");

        sendToLevel(level, FieldEffectRenderPayload.upsertSource(source, FieldEffectManager.get(level).getRangesBySource(source)));
    }

    /**
     * 指定ソースの削除通知を全プレイヤーへ送信します。
     * Sends a source-removal notification to all players.
     */
    public static void removeSource(ServerLevel level, BlockPos source) {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(source, "source");

        sendToLevel(level, FieldEffectRenderPayload.removeSource(source));
    }

    /**
     * 全範囲の削除通知を全プレイヤーへ送信します。
     * Sends an all-ranges-clear notification to all players.
     */
    public static void clear(ServerLevel level) {
        Objects.requireNonNull(level, "level");

        sendToLevel(level, FieldEffectRenderPayload.clearAll());
    }

    /**
     * 指定プレイヤーへ現在の全範囲を送信します。
     * Sends all current ranges to a player.
     */
    public static void syncPlayer(ServerPlayer player) {
        Objects.requireNonNull(player, "player");

        if (!(player.level() instanceof ServerLevel)) {
            return;
        }
        ServerLevel level = (ServerLevel) player.level();
        sendToPlayer(player, FieldEffectRenderPayload.fullSync(FieldEffectManager.get(level).getEntriesSnapshot()));
    }

    /**
     * ワールド内の全プレイヤーへ現在の全範囲を送信します。
     * Sends all current ranges to every player in a level.
     */
    public static void syncLoadedLevel(ServerLevel level) {
        Objects.requireNonNull(level, "level");

        sendToLevel(level, FieldEffectRenderPayload.fullSync(FieldEffectManager.get(level).getEntriesSnapshot()));
    }

    private static void sendToLevel(ServerLevel level, FieldEffectRenderPayload payload) {
        for (ServerPlayer player : level.players()) {
            sendToPlayer(player, payload);
        }
    }

    private static void sendToPlayer(ServerPlayer player, FieldEffectRenderPayload payload) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, payload);
    }
}


