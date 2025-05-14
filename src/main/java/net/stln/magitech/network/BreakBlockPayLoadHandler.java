package net.stln.magitech.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.UUID;

public class BreakBlockPayLoadHandler {

    public static void handleDataOnMainS2C(final BreakBlockPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        BlockPos pos = payload.pos();
        BlockPos initialPos = payload.initialPos();
        if (item instanceof PartToolItem) {
            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                if (payload.callBreakBlock()) {
                    trait.onBreakBlock(player, player.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack), player.level().getBlockState(pos), pos, 1, pos.equals(initialPos));
                }
                if (payload.effect()) {
                    trait.addEffect(player, player.level(), stack, integer, ((PartToolItem)stack.getItem()).getSumStats(player, player.level(), stack), player.level().getBlockState(pos), pos, 1, pos.equals(initialPos));
                }
            });
        }
    }
}
