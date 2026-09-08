package net.stln.magitech.feature.magic.mana;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class UsedHandData {

    // main = false, off = true
    private static final Map<Player, boolean[]> handMap = new WeakHashMap<>();

    public static void setUsedHand(Player player, boolean value) {
        UsedHandData.handMap.computeIfAbsent(player, k -> new boolean[]{false})[0] = value;
    }

    public static boolean[] getUsedHand(Player player) {
        UsedHandData.handMap.computeIfAbsent(player, k -> new boolean[]{false});
        return UsedHandData.handMap.get(player);
    }
}
