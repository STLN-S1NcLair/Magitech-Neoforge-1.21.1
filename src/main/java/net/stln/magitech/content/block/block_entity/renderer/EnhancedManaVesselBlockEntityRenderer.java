package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.stln.magitech.content.block.block_entity.EnhancedManaVesselBlockEntity;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EnhancedManaVesselBlockEntityRenderer extends GeoBlockRenderer<EnhancedManaVesselBlockEntity> {
    public EnhancedManaVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new EnhancedManaVesselBlockEntityModel());
    }
}
