package net.stln.magitech.content.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.content.block.block_entity.CompressorBlockEntity;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

public class CompressorCraftPayLoadHandler {

    public static void handleDataOnMainS2C(final CompressorCraftPayload payload, final IPayloadContext context) {
        if (context.player().level().getBlockEntity(payload.pos()) instanceof CompressorBlockEntity entity) {
            entity.spawnCraftCompleteParticles();
        }
    }
}
