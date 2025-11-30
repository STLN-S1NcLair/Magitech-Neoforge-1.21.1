package net.stln.magitech.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

public class BreakBlockPayLoadHandler {

    public static void handleDataOnMainS2C(final BreakBlockPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        if (player != null) {
            Player player1 = player;
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            BlockPos pos = payload.pos();
            BlockPos initialPos = payload.initialPos();
            if (item instanceof PartToolItem) {
                PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                    if (payload.callBreakBlock()) {
                        trait.onBreakBlock(player1, player1.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player1, player1.level(), stack), player1.level().getBlockState(pos), pos, 1, pos.equals(initialPos));
                    }
                    if (payload.effect()) {
                        trait.addEffect(player1, player1.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player1, player1.level(), stack), player1.level().getBlockState(pos), pos, 1, pos.equals(initialPos));
                    }
                });
            }
        }
    }
}
