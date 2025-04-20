package net.stln.magitech.magic.mana;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.util.Map2d;

import java.util.HashMap;
import java.util.Map;

public class UsedHandData {
    private static final Map<Player, InteractionHand> handMap = new HashMap<>();

    public static void setUsedHand(Player player, InteractionHand value) {
        UsedHandData.handMap.put(player, value);
    }

    public static InteractionHand getUsedHand(Player player) {
        return UsedHandData.handMap.getOrDefault(player, InteractionHand.MAIN_HAND);
    }
}
