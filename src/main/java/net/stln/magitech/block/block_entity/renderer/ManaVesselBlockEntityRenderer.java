package net.stln.magitech.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ManaVesselBlockEntityRenderer extends GeoBlockRenderer<ManaVesselBlockEntity> {
    public ManaVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new ManaVesselBlockEntityModel());
    }
}
