package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CrusherBlockEntityRenderer extends GeoBlockRenderer<CrusherBlockEntity> {
    public CrusherBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new CrusherBlockEntityModel());
    }
}
