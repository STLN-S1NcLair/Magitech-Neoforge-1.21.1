package net.stln.magitech.magic.mana;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.util.TableHelper;
import org.jetbrains.annotations.NotNull;

public class ManaData {
    private static final Table<Player, ManaUtil.ManaType, Double> currentManaMap = HashBasedTable.create();
    private static final Table<Player, ManaUtil.ManaType, Double> prevManaMap = HashBasedTable.create();

    public static @NotNull Table<Player, ManaUtil.ManaType, Double> getCurrentManaMap() {
        return currentManaMap;
    }

    public static void cleanUp(@NotNull Player player) {
        TableHelper.removeByRow(currentManaMap, player);
        TableHelper.removeByRow(prevManaMap, player);
    }

    public static void setCurrentMana(@NotNull Player player, @NotNull ManaUtil.ManaType type, double value) {
        currentManaMap.put(player, type, Math.max(value, 0));
    }

    public static void setPrevMana(@NotNull Player player, @NotNull ManaUtil.ManaType type, double value) {
        prevManaMap.put(player, type, Math.max(value, 0));
    }

    public static double getCurrentMana(@NotNull Player player, @NotNull ManaUtil.ManaType type) {
        return TableHelper.getOrDefault(currentManaMap, player, type, 0.0);
    }

    public static double getPrevMana(@NotNull Player player, @NotNull ManaUtil.ManaType type) {
        return TableHelper.getOrDefault(prevManaMap, player, type, 0.0);
    }
}
