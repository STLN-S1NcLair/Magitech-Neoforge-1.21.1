package net.stln.magitech.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.trait.TraitHelper;

public class TraitBlockBreakVFXPayloadHandler {

    public static void handleDataOnMainS2C(final TraitBlockBreakVFXPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        if (player != null) {
            Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            BlockPos pos = payload.pos();
            if (item instanceof SynthesisedToolItem) {
                payload.material().trait().emitBlockBreakParticle(level, pos, level.getBlockState(pos));
            }
        }
    }
}
