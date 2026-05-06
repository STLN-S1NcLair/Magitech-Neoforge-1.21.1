package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.InfuserBlockEntity;

public class InfuserBlockEntityRenderer extends PedestalBlockEntityRenderer<InfuserBlockEntity> {
    public InfuserBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 1.3F);
    }
}
