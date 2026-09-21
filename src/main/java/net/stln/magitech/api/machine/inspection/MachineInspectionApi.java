package net.stln.magitech.api.machine.inspection;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 機械情報表示の条件、拡張データ、サーバー側収集処理を提供します。
 * Provides display conditions, extension data, and server-side collection for machine inspection.
 */
public final class MachineInspectionApi {
    private static final List<MachineInspectionCondition> CONDITIONS = new CopyOnWriteArrayList<>();
    private static final List<RegisteredProvider> PROVIDERS = new CopyOnWriteArrayList<>();
    private static final Map<Class<?>, List<MachineInspectionDataProvider>> PROVIDER_CACHE = new ConcurrentHashMap<>();

    private MachineInspectionApi() {
    }

    /**
     * プレイヤー側の表示条件を登録します。複数登録時は、いずれかを満たせば表示します。
     * Registers a player-side display condition. When multiple conditions are registered, any passing condition allows display.
     */
    public static void registerDisplayCondition(MachineInspectionCondition condition) {
        CONDITIONS.add(Objects.requireNonNull(condition, "condition"));
    }

    /**
     * 指定プレイヤーが現在の表示条件を満たすかを返します。
     * Returns whether the specified player currently satisfies the display conditions.
     */
    public static boolean canDisplay(Player player) {
        if (CONDITIONS.isEmpty()) {
            return true;
        }
        for (MachineInspectionCondition condition : CONDITIONS) {
            if (condition.canDisplay(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 特定の BlockEntity 用に追加表示データの提供処理を登録します。
     * Registers a provider for additional display data for a specific BlockEntity type.
     */
    public static <T extends BlockEntity> void registerProvider(
            Class<T> blockEntityType,
            MachineInspectionDataProvider provider
    ) {
        PROVIDERS.add(new RegisteredProvider(
                Objects.requireNonNull(blockEntityType, "blockEntityType"),
                Objects.requireNonNull(provider, "provider")
        ));
        PROVIDER_CACHE.clear();
    }

    /**
     * 指定位置の機械情報をサーバー側で収集します。
     * Collects machine inspection data for the specified position on the server.
     */
    public static @Nullable MachineInspectionData collect(ServerPlayer player, BlockPos targetPosition) {
        return MachineInspectionCollector.collect(player, targetPosition);
    }

    static List<MachineInspectionDataProvider> providersFor(Class<?> blockEntityClass) {
        return PROVIDER_CACHE.computeIfAbsent(blockEntityClass, type -> PROVIDERS.stream()
                .filter(registeredProvider -> registeredProvider.blockEntityType().isAssignableFrom(type))
                .map(RegisteredProvider::provider)
                .toList());
    }

    private record RegisteredProvider(
            Class<? extends BlockEntity> blockEntityType,
            MachineInspectionDataProvider provider
    ) {
    }
}
