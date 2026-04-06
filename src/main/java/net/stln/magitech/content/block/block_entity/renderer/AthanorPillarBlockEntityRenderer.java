package net.stln.magitech.content.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.stln.magitech.content.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.content.block.block_entity.AthanorPillarBlockEntity;

public class AthanorPillarBlockEntityRenderer extends PedestalBlockEntityRenderer<AthanorPillarBlockEntity> {
    public AthanorPillarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 1.3F);
    }
}
