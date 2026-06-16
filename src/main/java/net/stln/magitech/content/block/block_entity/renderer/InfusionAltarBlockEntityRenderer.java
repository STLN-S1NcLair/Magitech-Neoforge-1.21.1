package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.InfusionAltarBlockEntity;

public class InfusionAltarBlockEntityRenderer extends PedestalBlockEntityRenderer<InfusionAltarBlockEntity> {
    public InfusionAltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 1.3F);
    }
}
