package net.stln.magitech.magic.mana;

import net.minecraft.world.entity.player.Player;
import net.stln.magitech.util.Map2d;

public class ManaData {
    private static Map2d<Player, ManaUtil.ManaType, Double> currentManaMap = new Map2d<>();
    private static Map2d<Player, ManaUtil.ManaType, Double> prevManaMap = new Map2d<>();

    public static Map2d<Player, ManaUtil.ManaType, Double> getCurrentManaMap() {
        return currentManaMap;
    }

    public static void cleanUp() {
        currentManaMap = new Map2d<>();
        prevManaMap = new Map2d<>();
    }

    public static void setCurrentMana(Player player, ManaUtil.ManaType type, double value) {
        ManaData.currentManaMap.put(player, type, value);
    }

    public static void setPrevMana(Player player, ManaUtil.ManaType type, double value) {
        ManaData.prevManaMap.put(player, type, value);
    }

    public static double getCurrentMana(Player player, ManaUtil.ManaType type) {
        return ManaData.currentManaMap.getOrDefault(player, type, 0.0);
    }

    public static double getPrevMana(Player player, ManaUtil.ManaType type) {
        return ManaData.prevManaMap.getOrDefault(player, type, 0.0);
    }
}
