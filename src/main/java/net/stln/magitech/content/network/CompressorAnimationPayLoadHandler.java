package net.stln.magitech.content.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.content.block.block_entity.CompressorBlockEntity;

public class CompressorAnimationPayLoadHandler {

    public static void handleDataOnMainS2C(final CompressorAnimationPayload payload, final IPayloadContext context) {
        if (context.player().level().getBlockEntity(payload.pos()) instanceof CompressorBlockEntity entity) {
            entity.requestAnimation();
        }
    }
}
