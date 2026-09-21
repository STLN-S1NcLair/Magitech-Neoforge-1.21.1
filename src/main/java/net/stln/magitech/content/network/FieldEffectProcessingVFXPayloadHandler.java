package net.stln.magitech.content.network;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.effect.visual.preset.BlockVFX;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;

import java.awt.Color;

public final class FieldEffectProcessingVFXPayloadHandler {

    private FieldEffectProcessingVFXPayloadHandler() {
    }

    public static void handleDataOnMainS2C(FieldEffectProcessingVFXPayload payload, IPayloadContext context) {
        var level = context.player().level();
        Color primary = new Color(payload.primaryColor(), true);
        Color secondary = new Color(payload.secondaryColor(), true);

        switch (payload.target()) {
            case BLOCK -> BlockVFX.fieldEffectProcessing(level, primary, secondary, payload.blockPos());
            case ITEM -> {
                Entity entity = level.getEntity(payload.entityId());
                if (entity != null) {
                    EntityVFX.fieldEffectProcessing(level, primary, secondary, entity);
                }
            }
            case PEDESTAL -> PointVFX.fieldEffectProcessing(level, primary, secondary, Vec3.atBottomCenterOf(payload.blockPos()).add(0.0D, 0.8D, 0.0D));
        }
    }
}
