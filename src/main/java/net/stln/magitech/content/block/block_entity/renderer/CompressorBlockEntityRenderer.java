package net.stln.magitech.content.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.stln.magitech.content.block.block_entity.CompressorBlockEntity;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CompressorBlockEntityRenderer extends GeoBlockRenderer<CompressorBlockEntity> {
    public CompressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new CompressorBlockEntityModel());
    }

    @Override
    public void render(CompressorBlockEntity entity, float partialTick, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(entity, partialTick, pPoseStack, bufferSource, packedLight, packedOverlay);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getItem(CompressorBlockEntity.INPUT);

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.275F, 0.5f);
        pPoseStack.scale(0.5f, 0.5f, 0.5f);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(90));

        Level level = entity.getLevel();

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
