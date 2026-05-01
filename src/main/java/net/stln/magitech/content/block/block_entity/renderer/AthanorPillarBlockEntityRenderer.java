package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.InfuserBlockEntity;

public class AthanorPillarBlockEntityRenderer extends PedestalBlockEntityRenderer<InfuserBlockEntity> {
    public AthanorPillarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 1.3F);
    }
}
