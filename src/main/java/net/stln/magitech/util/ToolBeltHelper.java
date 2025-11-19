package net.stln.magitech.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ToolBeltHelper {

    public static void swapTool(Player player, ItemStack stack, int select) {
        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack toolInBelt = ComponentHelper.getToolsInBelt(stack).stacks().get(select);
        ComponentHelper.updateToolBelt(stack, toolBeltComponent -> toolBeltComponent.changeStack(itemInHand.copy(), select));
        player.setItemInHand(InteractionHand.MAIN_HAND, toolInBelt);
        if (player.level() != null && !(itemInHand.isEmpty() && toolInBelt.isEmpty())) {
            player.level().playSound(player, player.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON.value(), player.getSoundSource(), 1.0F, 1.0F);
        }
    }

}
