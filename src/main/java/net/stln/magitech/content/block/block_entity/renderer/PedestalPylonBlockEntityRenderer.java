package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.content.block.block_entity.PedestalPylonBlockEntity;

public class PedestalPylonBlockEntityRenderer extends PedestalBlockEntityRenderer<PedestalPylonBlockEntity> {
    public PedestalPylonBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 1.1F);
    }
}
