package net.stln.magitech.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.trait.TraitHelper;

public class EntanglerEntanglePayLoadHandler {

    public static void handleDataOnMainS2C(final EntanglerEntanglePayload payload, final IPayloadContext context) {
        PointVFX.burst(context.player().level(), payload.pos().getCenter(), Element.MANA, SquareParticles::squareParticle, 10, 0.2F);
    }
}
