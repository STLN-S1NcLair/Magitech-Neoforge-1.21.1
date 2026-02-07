package net.stln.magitech.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ManaVesselBlockEntityRenderer extends GeoBlockRenderer<ManaVesselBlockEntity> {
    public ManaVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new ManaVesselBlockEntityModel());
    }
}
