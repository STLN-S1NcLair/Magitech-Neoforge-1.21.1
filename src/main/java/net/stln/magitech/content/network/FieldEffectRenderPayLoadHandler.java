package net.stln.magitech.content.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.core.api.field_effect.data.FieldEffectClientCache;

public class FieldEffectRenderPayLoadHandler {

    public static void handleDataOnMainS2C(final FieldEffectRenderPayload payload, final IPayloadContext context) {
        FieldEffectClientCache.getInstance().apply(payload);
    }
}
