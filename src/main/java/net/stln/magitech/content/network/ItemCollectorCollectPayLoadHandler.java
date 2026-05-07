package net.stln.magitech.content.network;

import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

public class ItemCollectorCollectPayLoadHandler {

    public static void handleDataOnMainS2C(final ItemCollectorCollectPayload payload, final IPayloadContext context) {
        LineVFX.destinationLined(context.player().level(), new Vec3(payload.pos()), payload.blockPos().getCenter(), Element.MANA, SquareParticles::squareParticle, Section.cover(), 5.0F, 0.1F, 0.1F);
        TrailVFX.directionalZapTrail(context.player().level(), payload.blockPos().getCenter(), new Vec3(payload.pos()), 0.5F, 3F, 0.25F, 15, Element.MANA);
        PointVFX.burst(context.player().level(), new Vec3(payload.pos()), Element.MANA, SquareParticles::squareParticle, 10, 0.1F);
    }
}
