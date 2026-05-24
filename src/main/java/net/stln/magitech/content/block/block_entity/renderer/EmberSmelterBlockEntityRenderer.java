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
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;
import net.stln.magitech.content.block.block_entity.EntanglerBlockEntity;

public class EmberSmelterBlockEntityRenderer implements BlockEntityRenderer<EmberSmelterBlockEntity> {
    public EmberSmelterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(EmberSmelterBlockEntity entity, float partialTick, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getItem(EmberSmelterBlockEntity.INPUT);

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.5F, 0.5f);
        pPoseStack.scale(0.5f, 0.5f, 0.5f);

        Level level = entity.getLevel();
        if (level != null) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees((level.getGameTime() + partialTick) % 360));
        }

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(level,
                entity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, bufferSource, level, 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
