package net.stln.magitech.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.stln.magitech.block.ZardiusCrucibleBlock;
import net.stln.magitech.block.block_entity.ZardiusCrucibleBlockEntity;

public class ZardiusCrucibleBlockEntityRenderer implements BlockEntityRenderer<ZardiusCrucibleBlockEntity> {
    public ZardiusCrucibleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    public static float intToRandPattern(float value) {
        return (float) (Math.sqrt(value) % 1);
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.addVertex(poseStack.last().pose(), x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setLight(packedLight)
                .setNormal(1, 0, 0);
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
    }

    @Override
    public void render(ZardiusCrucibleBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer,
                       int pPackedLight, int pPackedOverlay) {
        NonNullList<ItemStack> itemStack = pBlockEntity.getRenderStack();
        FluidTank fluidTank = pBlockEntity.fluidTank;
        int leng = itemStack.size();

        float renderCount = (pBlockEntity.tickCounter + pPartialTick) % 500;
        Boolean isLit = pBlockEntity.getBlockState().getValue(ZardiusCrucibleBlock.LIT);
        if (isLit) {
            renderCount *= 5;
            renderCount %= 500;
        }
        float height = (float) pBlockEntity.getFluidAnim(pBlockEntity, pPartialTick) / fluidTank.getCapacity();

        for (int i = 0; i < leng; i++) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            float randomizer0 = (intToRandPattern(i + 1.5f) - 0.5f) / 8;
            float randomizer1 = (intToRandPattern(i + 2.5f) - 0.5f) / 8;
            float randomizer2 = (intToRandPattern(i + 3.5f) - 0.5f) / 8;

            float floatOffset = 0;
            float floatRandomizer = intToRandPattern(i + 2) * 500;
            if ((renderCount + floatRandomizer) % 500 < 250) {
                floatOffset = ((float) (renderCount + floatRandomizer) % 500) / 250 - 0.5f;
            } else {
                floatOffset = ((float) -(renderCount + floatRandomizer) % 500) / 250 + 1.5f;
            }
            if (isLit) {
                floatOffset *= 2;
            }
            double rotation = Math.toRadians((double) (i * 360) / leng);
            double Yoffset = fluidTank.isEmpty() && height == 0 ? 0.375 : height * 0.75f + 0.1875f + (Math.sin(floatOffset * Math.PI) / 50) + randomizer1 / 2;

            pPoseStack.pushPose();
            pPoseStack.translate(Math.sin(rotation) * 0.3 + 0.5 + randomizer0, Yoffset, Math.cos(rotation) * 0.3 + 0.5 + randomizer2);
            pPoseStack.scale(0.35f, 0.35f, 0.35f);
            pPoseStack.mulPose(Axis.YN.rotation((float) -rotation + randomizer0 * 5));
            pPoseStack.mulPose(Axis.XP.rotationDegrees((25 + randomizer1 * 300)));

            itemRenderer.renderStatic(itemStack.get(i), ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                    pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }
        FluidStack fluidStack = new FluidStack(fluidTank.getFluid().getFluid(), 1);
        if (fluidStack.isEmpty() && height > 0) {
            fluidStack = pBlockEntity.oldFluidStack;
        } else if (!fluidStack.isEmpty()) {
            pBlockEntity.oldFluidStack = fluidStack.copy();
        }
        if (fluidStack.isEmpty()) {
            return;
        }

        Level level = pBlockEntity.getLevel();
        if (level == null)
            return;

        BlockPos pos = pBlockEntity.getBlockPos();

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);

        FluidState state = fluidStack.getFluid().defaultFluidState();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);

        VertexConsumer builder = pBuffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
        int lightLevel;
        if (state.getFluidType().getLightLevel() > 0) {
            lightLevel = 15728880;
        } else {
            lightLevel = getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());
        }

        if (fluidStack.getAmount() > 0) {
            drawQuad(builder, pPoseStack, 0.125f, height * 0.75f + 0.1875f, 0.125f, 0.875f, height * 0.75f + 0.1875f, 0.875f,
                    sprite.getU(0.125F), sprite.getV(0.125F), sprite.getU(0.875F), sprite.getV(0.875F),
                    lightLevel, tintColor);
        }

//        drawQuad(builder, pPoseStack, 0.126f, 0.188f, 0.126f, 0.874f, /*height*/0.9f, 0.126f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, tintColor);

//        pPoseStack.pushPose();
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
//        pPoseStack.translate(-1f, 0, -1f);
//        drawQuad(builder, pPoseStack, 0.126f, 0.188f, 0.874f, 0.874f, /*height*/0.9f, 0.874f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, tintColor);
//        pPoseStack.popPose();

//        pPoseStack.pushPose();
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
//        pPoseStack.translate(-1f, 0, 0);
//        drawQuad(builder, pPoseStack, 0.126f, 0.188f, 0.126f, 0.874f, /*height*/0.9f, 0.126f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, tintColor);
//        pPoseStack.popPose();

//        pPoseStack.pushPose();
//        pPoseStack.mulPose(Axis.YN.rotationDegrees(90));
//        pPoseStack.translate(0, 0, -1f);
//        drawQuad(builder, pPoseStack, 0.126f, 0.188f, 0.126f, 0.874f, /*height*/0.9f, 0.126f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, tintColor);
//        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
