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
        registrar.playBidirectional(
                UseC2SPayload.TYPE,
                UseC2SPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        UsePayLoadHandler::handleDataOnMainS2C,
                        UsePayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                TraitTickC2SPayload.TYPE,
                TraitTickC2SPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        TraitTickPayLoadHandler::handleDataOnMainS2C,
                        TraitTickPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playToClient(
                BreakBlockS2CPayload.TYPE,
                BreakBlockS2CPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        BreakBlockPayLoadHandler::handleDataOnMainS2C,
                        BreakBlockPayLoadHandler::handleDataOnMainS2C
                )
        );
    }
}
