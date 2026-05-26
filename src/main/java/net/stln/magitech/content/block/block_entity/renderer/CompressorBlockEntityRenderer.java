package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.CompressorBlockEntity;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CompressorBlockEntityRenderer extends GeoBlockRenderer<CompressorBlockEntity> {
    public CompressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new CompressorBlockEntityModel());
    }
}
