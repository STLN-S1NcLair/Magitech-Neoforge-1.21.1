package net.stln.magitech.block.block_entity.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class BlockRendererInit {

    @SubscribeEvent
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockInit.TOOL_HANGER_ENTITY.get(), ToolHangerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockInit.ALCHEMETRIC_PYLON_ENTITY.get(), AlchemetricPylonBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockInit.ATHANOR_PILLAR_ENTITY.get(), AthanorPillarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), ZardiusCrucibleBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockInit.MANA_VESSEL_ENTITY.get(), ManaVesselBlockEntityRenderer::new);
    }

}
