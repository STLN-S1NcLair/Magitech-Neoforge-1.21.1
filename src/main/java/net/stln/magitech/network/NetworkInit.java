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
                LeftClickPayload.TYPE,
                LeftClickPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        LeftClickPayLoadHandler::handleDataOnMainS2C,
                        LeftClickPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                TraitActionPayload.TYPE,
                TraitActionPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        TraitActionPayLoadHandler::handleDataOnMainS2C,
                        TraitActionPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                DoubleJumpPayload.TYPE,
                DoubleJumpPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        DoubleJumpPayLoadHandler::handleDataOnMainS2C,
                        DoubleJumpPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                LongJumpPayload.TYPE,
                LongJumpPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        LongJumpPayLoadHandler::handleDataOnMainS2C,
                        LongJumpPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                TraitTickPayload.TYPE,
                TraitTickPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        TraitTickPayLoadHandler::handleDataOnMainS2C,
                        TraitTickPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                SyncManaPayload.TYPE,
                SyncManaPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SyncManaPayLoadHandler::handleDataOnMainS2C,
                        SyncManaPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playToClient(
                BreakBlockPayload.TYPE,
                BreakBlockPayload.STREAM_CODEC,
                BreakBlockPayLoadHandler::handleDataOnMainS2C
        );
        registrar.playBidirectional(
                UseSpellPayload.TYPE,
                UseSpellPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        UseSpellPayLoadHandler::handleDataOnMainS2C,
                        UseSpellPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                ReleaseUsingSpellPayload.TYPE,
                ReleaseUsingSpellPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ReleaseUsingSpellPayLoadHandler::handleDataOnMainS2C,
                        ReleaseUsingSpellPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playBidirectional(
                ThreadBoundSelectPayload.TYPE,
                ThreadBoundSelectPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ThreadboundSelectPayLoadHandler::handleDataOnMainS2C,
                        ThreadboundSelectPayLoadHandler::handleDataOnMainC2S
                )
        );
        registrar.playToServer(
                OpenThreadBoundPageScreenPayload.TYPE,
                OpenThreadBoundPageScreenPayload.STREAM_CODEC,
                OpenThreadBoundPageScreenPayLoadHandler::handleDataOnMainC2S
        );
        registrar.playToClient(
                TierUpToastPayload.TYPE,
                TierUpToastPayload.STREAM_CODEC,
                TierUpToastPayLoadHandler::handleDataOnMainS2C
        );
    }
}
