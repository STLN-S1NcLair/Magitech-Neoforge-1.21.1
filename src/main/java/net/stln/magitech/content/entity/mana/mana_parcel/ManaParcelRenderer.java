package net.stln.magitech.content.entity.mana.mana_parcel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class ManaParcelRenderer extends EntityRenderer<ManaParcelEntity> {

    public ManaParcelRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(ManaParcelEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(ManaParcelEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0, entity.getBbHeight() / 2, 0);

        poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTick) * 6 % 360));
        poseStack.mulPose(Axis.XP.rotationDegrees((entity.tickCount + partialTick) * 8.5F % 360));
        poseStack.mulPose(Axis.ZN.rotationDegrees((entity.tickCount + partialTick) * 4.5F % 360));

        poseStack.scale(0.5f, 0.5f, 0.5f);

        // アイテムスタックを取得して 3D ワールド描画
        ItemStack stack = entity.getStack();
        if (!stack.isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            // ワールド座標系での 3D 描画
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(entity.level(),
                    entity.blockPosition()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 1);
        }

        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
