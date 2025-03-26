package net.stln.magitech.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface LeftClickOverrideItem {
    InteractionResult onLeftClick(Player player, InteractionHand hand, Level world);
}
