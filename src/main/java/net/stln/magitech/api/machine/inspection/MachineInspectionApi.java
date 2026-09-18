package net.stln.magitech.api.machine.inspection;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectHelper;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.data.FieldEffectManager;
import net.stln.magitech.core.api.mana.container.IManaContainerBlockEntity;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 機械情報表示の条件、拡張データ、サーバー側収集処理を提供します。
 * Provides display conditions, extension data, and server-side collection for machine inspection.
 */
public final class MachineInspectionApi {
    private static final List<MachineInspectionCondition> CONDITIONS = new CopyOnWriteArrayList<>();
    private static final List<RegisteredProvider> PROVIDERS = new CopyOnWriteArrayList<>();

    private MachineInspectionApi() {
    }

    /**
     * プレイヤー側の表示条件を登録します。複数登録時は、いずれかを満たせば表示します。
     * Registers a player-side display condition. When multiple conditions are registered, any passing condition allows display.
     */
    public static void registerDisplayCondition(MachineInspectionCondition condition) {
        CONDITIONS.add(condition);
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
        PROVIDERS.add(new RegisteredProvider(blockEntityType, provider));
    }

    /**
     * 指定位置の機械情報をサーバー側で収集します。
     * Collects machine inspection data for the specified position on the server.
     */
    public static @Nullable MachineInspectionData collect(ServerPlayer player, BlockPos targetPosition) {
        if (!canDisplay(player)) {
            return null;
        }

        Level level = player.level();
        BlockEntity blockEntity = level.getBlockEntity(targetPosition);
        if (blockEntity == null) {
            return null;
        }

        BlockState blockState = level.getBlockState(targetPosition);
        IMachineInspectionTarget inspectionTarget = blockEntity instanceof IMachineInspectionTarget target
                ? target
                : null;
        BlockPos displayPosition = targetPosition;
        IBlockManaHandler manaHandler = null;
        IItemHandler itemHandler = null;
        long flowRate = 0L;

        if (inspectionTarget != null) {
            displayPosition = inspectionTarget.getInspectionPosition();
            manaHandler = inspectionTarget.getInspectionManaHandler();
            itemHandler = inspectionTarget.getInspectionItemHandler();
            flowRate = inspectionTarget.getInspectionFlowRate();
        }

        if (manaHandler == null) {
            manaHandler = ManaTransferHelper.getManaContainer(level, targetPosition, null);
        }
        boolean canInspectWithoutMana = inspectionTarget != null && inspectionTarget.canInspectWithoutMana();
        if (manaHandler == null && !canInspectWithoutMana) {
            return null;
        }

        if (itemHandler == null) {
            itemHandler = level.getCapability(
                    Capabilities.ItemHandler.BLOCK,
                    targetPosition,
                    blockState,
                    blockEntity,
                    null
            );
        }
        if (flowRate == 0L && blockEntity instanceof IManaContainerBlockEntity container) {
            flowRate = container.getFlowRate();
        }

        MachineInspectionData.Builder builder = MachineInspectionData.builder(targetPosition, displayPosition)
                .setMana(
                        manaHandler == null ? 0L : manaHandler.getMana(),
                        manaHandler == null ? 0L : manaHandler.getMaxMana()
                )
                .setFlow(flowRate, manaHandler == null ? 0L : manaHandler.getMaxFlow());

        IManaMachineBlockEntity machine = resolveInspectionMachine(
                level,
                blockEntity,
                displayPosition,
                inspectionTarget
        );
        if (machine != null) {
            builder.setProductionRate(machine.getProductionRate())
                    .setConsumptionRate(machine.getConsumptionRate());
        }

        ResourceLocation fieldEffectId = null;
        FieldInfluenceInstance fieldInfluences = null;
        if (level instanceof ServerLevel serverLevel) {
            FieldInfluenceInstance detectedInfluences =
                    FieldEffectManager.get(serverLevel).getFieldEffectInstance(displayPosition);
            FieldEffectType fieldEffect = FieldEffectHelper.getFieldEffect(
                    level,
                    detectedInfluences
            );
            if (fieldEffect != null) {
                fieldEffectId = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(fieldEffect);
            }
            if (inspectionTarget != null && inspectionTarget.showFieldEffectInfluencesInInspection()) {
                fieldInfluences = detectedInfluences;
            }
        }
        builder.setFieldEffect(fieldEffectId).setFieldInfluences(fieldInfluences);

        if (itemHandler != null) {
            int slotCount = Math.min(itemHandler.getSlots(), MachineInspectionData.MAX_ITEM_SLOTS);
            for (int slot = 0; slot < slotCount; slot++) {
                builder.addItem(itemHandler.getStackInSlot(slot));
            }
        }

        IFluidHandler fluidHandler = level.getCapability(
                Capabilities.FluidHandler.BLOCK,
                targetPosition,
                blockState,
                blockEntity,
                null
        );
        if (fluidHandler != null) {
            int tankCount = Math.min(fluidHandler.getTanks(), MachineInspectionData.MAX_FLUID_TANKS);
            for (int tank = 0; tank < tankCount; tank++) {
                builder.addFluid(fluidHandler.getFluidInTank(tank));
            }
        }

        if (inspectionTarget != null) {
            inspectionTarget.appendInspectionData(builder);
        }

        if (!PROVIDERS.isEmpty()) {
            MachineInspectionContext context = new MachineInspectionContext(
                    player,
                    level,
                    targetPosition,
                    displayPosition,
                    blockEntity
            );
            for (RegisteredProvider registeredProvider : PROVIDERS) {
                if (registeredProvider.blockEntityType().isInstance(blockEntity)) {
                    registeredProvider.provider().append(context, builder);
                }
            }
        }
        return builder.build();
    }

    private static @Nullable IManaMachineBlockEntity resolveInspectionMachine(
            Level level,
            BlockEntity blockEntity,
            BlockPos displayPosition,
            @Nullable IMachineInspectionTarget inspectionTarget
    ) {
        if (inspectionTarget != null) {
            IManaMachineBlockEntity machine = inspectionTarget.getInspectionMachine();
            if (machine != null) {
                return machine;
            }
        }

        if (displayPosition.equals(blockEntity.getBlockPos())
                && blockEntity instanceof IManaMachineBlockEntity machine) {
            return machine;
        }

        BlockEntity displayBlockEntity = level.getBlockEntity(displayPosition);
        if (displayBlockEntity instanceof IManaMachineBlockEntity displayMachine) {
            return displayMachine;
        }
        return blockEntity instanceof IManaMachineBlockEntity machine ? machine : null;
    }

    private record RegisteredProvider(
            Class<? extends BlockEntity> blockEntityType,
            MachineInspectionDataProvider provider
    ) {
    }
}
