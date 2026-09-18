package net.stln.magitech.api.machine.inspection;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.items.IItemHandler;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import org.jetbrains.annotations.Nullable;

/**
 * 機械情報表示で標準の能力取得方法を上書きする BlockEntity 用 API です。
 * API for BlockEntities that override the standard capability lookup used by machine inspection.
 */
public interface IMachineInspectionTarget {
    /**
     * 表示対象となる機械の代表位置を返します。
     * Returns the representative position of the inspected machine.
     */
    BlockPos getInspectionPosition();

    /**
     * 表示するマナハンドラーを返します。
     * Returns the mana handler to display.
     */
    @Nullable
    IBlockManaHandler getInspectionManaHandler();

    /**
     * 表示するアイテムハンドラーを返します。
     * Returns the item handler to display.
     */
    @Nullable
    IItemHandler getInspectionItemHandler();

    /**
     * 表示する現在のマナ流量を返します。
     * Returns the current mana flow rate to display.
     */
    default long getInspectionFlowRate() {
        return 0L;
    }

    /**
     * 表示する生産・消費レートの取得元となる機械を返します。
     * Returns the machine used as the source of the displayed production and consumption rates.
     */
    @Nullable
    default IManaMachineBlockEntity getInspectionMachine() {
        return this instanceof IManaMachineBlockEntity machine ? machine : null;
    }

    /**
     * マナを持たない対象でもInspectionを表示できるか返します。
     * Returns whether inspection can be displayed for a target without mana.
     */
    default boolean canInspectWithoutMana() {
        return false;
    }

    /**
     * Inspectionでフィールド効果の要因を表示するか返します。
     * Returns whether field-effect influences should be shown in inspection.
     */
    default boolean showFieldEffectInfluencesInInspection() {
        return false;
    }

    /**
     * 機械固有の追加情報を表示データへ追加します。
     * Appends machine-specific information to the inspection data.
     */
    default void appendInspectionData(MachineInspectionData.Builder builder) {
    }
}
