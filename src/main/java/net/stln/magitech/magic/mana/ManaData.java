package net.stln.magitech.magic.mana;

import net.minecraft.world.entity.player.Player;
import net.stln.magitech.util.Map2d;

import java.util.HashMap;
import java.util.Map;

public class ManaData {
    private static final Map2d<Player, ManaUtil.ManaType, Double> currentManaMap = new Map2d<>();

    public static Map2d<Player, ManaUtil.ManaType, Double> getCurrentManaMap() {
        return currentManaMap;
    }

    public static void setCurrentMana(Player player, ManaUtil.ManaType type, double value) {
        ManaData.currentManaMap.put(player, type, value);
    }

    public static double getCurrentMana(Player player, ManaUtil.ManaType type) {
        return ManaData.currentManaMap.getOrDefault(player, type, 0.0);
    }
}
