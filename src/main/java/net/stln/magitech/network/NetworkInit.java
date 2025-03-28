package net.stln.magitech.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkInit {

    @SubscribeEvent
    public static void registerPayload(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                LeftClickC2SPayload.TYPE,
                LeftClickC2SPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        LeftClickPayLoadHandler::handleDataOnMainS2C,
                        LeftClickPayLoadHandler::handleDataOnMainC2S
                )
        );
    }
}
