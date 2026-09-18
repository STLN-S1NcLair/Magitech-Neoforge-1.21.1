package net.stln.magitech.api.machine.inspection;

import net.minecraft.world.entity.player.Player;

/**
 * 機械情報を表示できるプレイヤーかを判定します。
 * Determines whether a player may see machine inspection data.
 */
@FunctionalInterface
public interface MachineInspectionCondition {
    /**
     * 表示条件を判定します。
     * Evaluates the display condition.
     */
    boolean canDisplay(Player player);
}
