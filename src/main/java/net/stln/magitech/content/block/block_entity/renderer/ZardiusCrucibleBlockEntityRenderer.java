package net.stln.magitech.content.block.block_entity.renderer;

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
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.capability.FallbackFluidTank;
import net.stln.magitech.content.block.ZardiusCrucibleBlock;
import net.stln.magitech.content.block.block_entity.ZardiusCrucibleBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ZardiusCrucibleBlockEntityRenderer implements BlockEntityRenderer<ZardiusCrucibleBlockEntity> {
    private static final float SURFACE_MIN = 0.125f;
    private static final float SURFACE_MAX = 0.875f;
    private static final float FLUID_Y_BASE = 0.1875f;
    private static final int RENDER_FLUID_CAPACITY = 2000;

    public ZardiusCrucibleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static float intToRandPattern(float value) {
        // Stable 0..1 hash that does not produce NaN for negative inputs.
        double hashed = Math.sin(value * 12.9898 + 78.233) * 43758.5453;
        return (float) (hashed - Math.floor(hashed));
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        try {
            builder.addVertex(poseStack.last().pose(), x, y, z)
                    .setColor(color)
                    .setUv(u, v)
                    .setLight(packedLight)
                    .setNormal(1, 0, 0);
        } catch (Exception e) {
            // Log and skip any vertex that causes issues to avoid crashing the entire renderer.
            Magitech.LOGGER.error("Error rendering fluid vertex at ({}, {}, {}): {}", x, y, z, e.getMessage());
        }
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
    }

    private record FluidRenderData(FluidStack stack, TextureAtlasSprite sprite, int tintColor, VertexConsumer builder, int lightLevel) {
    }

    private void drawSurfaceRect(VertexConsumer builder, PoseStack poseStack, TextureAtlasSprite sprite,
                                 float xMin, float xMax, float y, float zMin, float zMax,
                                 int packedLight, int color) {
        drawQuad(builder, poseStack, xMin, y, zMin, xMax, y, zMax,
                sprite.getU(xMin), sprite.getV(zMin),
                sprite.getU(xMax), sprite.getV(zMax),
                packedLight, color);
    }

    private void drawBoundaryFaceX(VertexConsumer builder, PoseStack poseStack, TextureAtlasSprite sprite,
                                   float x, float yBottom, float yTop, float zMin, float zMax,
                                   int packedLight, int color, boolean reverseWinding) {
        float uMin = sprite.getU(zMin);
        float uMax = sprite.getU(zMax);
        float vMin = sprite.getV(0f);
        float vMax = sprite.getV((yTop - yBottom));

        if (!reverseWinding) {
            drawVertex(builder, poseStack, x, yBottom, zMin, uMin, vMin, packedLight, color);
            drawVertex(builder, poseStack, x, yTop, zMin, uMin, vMax, packedLight, color);
            drawVertex(builder, poseStack, x, yTop, zMax, uMax, vMax, packedLight, color);
            drawVertex(builder, poseStack, x, yBottom, zMax, uMax, vMin, packedLight, color);
        } else {
            drawVertex(builder, poseStack, x, yBottom, zMax, uMax, vMin, packedLight, color);
            drawVertex(builder, poseStack, x, yTop, zMax, uMax, vMax, packedLight, color);
            drawVertex(builder, poseStack, x, yTop, zMin, uMin, vMax, packedLight, color);
            drawVertex(builder, poseStack, x, yBottom, zMin, uMin, vMin, packedLight, color);
        }
    }

    private void drawBoundaryX(FluidRenderData fluid, PoseStack poseStack, float splitX, float yBottom, float yTop, boolean reversed) {
        // ユーザー要望: 境界面の fluid テクスチャ割り当てを fluid0/fluid1 で入れ替える。
        drawBoundaryFaceX(fluid.builder(), poseStack, fluid.sprite(), splitX, yBottom, yTop, SURFACE_MIN, SURFACE_MAX,
                fluid.lightLevel(), fluid.tintColor(), reversed);
    }

    private void renderMixOverlay(PoseStack poseStack, ZardiusCrucibleBlockEntity blockEntity, FluidRenderData fluid, float y, boolean isPrimary) {
        BlockPos pos = blockEntity.getBlockPos();
        float time = blockEntity.tickCounter;

        // Surface-wide scattered square pulses for a "mixed" feeling.
        int particles = blockEntity.getBlockState().getValue(ZardiusCrucibleBlock.LIT) ? 48 : 30;
        float cycle = blockEntity.getBlockState().getValue(ZardiusCrucibleBlock.LIT) ? 88.0f : 180.0f;

        for (int i = 0; i < particles; i++) {
            float baseSeed = (pos.getX() * 31f) + (pos.getY() * 17f) + (pos.getZ() * 13f) + i * 11.73f;
            float phase = ((time + intToRandPattern(baseSeed + 0.37f) * cycle) % cycle) / cycle;

            // 0 -> 1 -> 0 pulse curve (grow then shrink then disappear).
            float pulse = (float) Math.sin(phase * Mth.TWO_PI);
            if (pulse <= 0.02f) {
                continue;
            }

            float cx = SURFACE_MIN + (SURFACE_MAX - SURFACE_MIN) * intToRandPattern(baseSeed + 1.19f);
            float cz = SURFACE_MIN + (SURFACE_MAX - SURFACE_MIN) * intToRandPattern(baseSeed + 2.61f);

            float minHalf = 0.05f;
            float maxHalf = 0.12f;
            float half = minHalf + (maxHalf - minHalf) * pulse;

            float xMin = Math.max(SURFACE_MIN, cx - half);
            float xMax = Math.min(SURFACE_MAX, cx + half);
            float zMin = Math.max(SURFACE_MIN, cz - half);
            float zMax = Math.min(SURFACE_MAX, cz + half);

            if (isPrimary ^ (intToRandPattern(baseSeed + 3.77f) > 0.5f)) {
                drawSurfaceRect(fluid.builder(), poseStack, fluid.sprite(), xMin, xMax, y + 0.01f + i * 0.001f, zMin, zMax, fluid.lightLevel(), fluid.tintColor());
            }
        }
    }

    private FluidRenderData createFluidRenderData(FluidStack fluidStack, Level level, BlockPos pos, MultiBufferSource bufferSource) {
        if (fluidStack.isEmpty()) {
            return null;
        }

        // Resolve sprite/tint/render layer from the fluid's client extension.
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);

        FluidState state = fluidStack.getFluid().defaultFluidState();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
        VertexConsumer builder = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
        int lightLevel = state.getFluidType().getLightLevel() > 0 ? 15728880 : getLightLevel(level, pos);
        return new FluidRenderData(fluidStack, sprite, tintColor, builder, lightLevel);
    }

    @Override
    public void render(ZardiusCrucibleBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        NonNullList<ItemStack> itemStack = blockEntity.getRenderStack();
        FallbackFluidTank fluidTank = blockEntity.tank;
        int leng = itemStack.size();

        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }
        BlockPos pos = blockEntity.getBlockPos();

        FluidStack fluid0 = fluidTank.getFluidInTank(0);
        FluidStack fluid1 = fluidTank.getFluidInTank(1);
        int amount0 = fluid0.getAmount();
        int amount1 = fluid1.getAmount();
        int totalAmount = amount0 + amount1;

        // 合計液量ベース(最大2000)で高さを決め、getFluidAnimで補間する。
        double animatedAmount = blockEntity.getFluidAnim(blockEntity, partialTick);
        float normalizedHeight = (float) Math.min(animatedAmount, RENDER_FLUID_CAPACITY) / RENDER_FLUID_CAPACITY;

        float renderCount = (blockEntity.tickCounter + partialTick) % 500;
        Boolean isLit = blockEntity.getBlockState().getValue(ZardiusCrucibleBlock.LIT);
        if (isLit) {
            renderCount *= 5;
            renderCount %= 500;
        }

        for (int i = 0; i < leng; i++) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            float randomizer0 = (intToRandPattern(i + 1.5f) - 0.5f) / 8;
            float randomizer1 = (intToRandPattern(i + 2.5f) - 0.5f) / 8;
            float randomizer2 = (intToRandPattern(i + 3.5f) - 0.5f) / 8;

            float floatOffset;
            float floatRandomizer = intToRandPattern(i + 2) * 500;
            if ((renderCount + floatRandomizer) % 500 < 250) {
                floatOffset = ((renderCount + floatRandomizer) % 500) / 250 - 0.5f;
            } else {
                floatOffset = (-(renderCount + floatRandomizer) % 500) / 250 + 1.5f;
            }
            if (isLit) {
                floatOffset *= 2;
            }
            double rotation = Math.toRadians((double) (i * 360) / leng);
            double Yoffset = totalAmount <= 0 ? 0.375 : normalizedHeight * 0.75f + 0.1875f + (Math.sin(floatOffset * Math.PI) / 50) + randomizer1 / 2;

            poseStack.pushPose();
            poseStack.translate(Math.sin(rotation) * 0.3 + 0.5 + randomizer0, Yoffset, Math.cos(rotation) * 0.3 + 0.5 + randomizer2);
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.mulPose(Axis.YN.rotation((float) -rotation + randomizer0 * 5));
            poseStack.mulPose(Axis.XP.rotationDegrees((25 + randomizer1 * 300)));

            itemRenderer.renderStatic(itemStack.get(i), ItemDisplayContext.FIXED, getLightLevel(level, pos), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, level, 1);
            poseStack.popPose();
        }

        if (totalAmount <= 0) {
            return;
        }

        float y = normalizedHeight * 0.75f + FLUID_Y_BASE;

        FluidRenderData data0 = createFluidRenderData(fluid0, level, pos, bufferSource);

        if (data0 == null) {
            return;
        }

        if (fluid1.isEmpty()) {
            // Single-fluid fallback keeps the old full-surface rendering behavior.
            drawSurfaceRect(data0.builder(), poseStack, data0.sprite(), SURFACE_MIN, SURFACE_MAX, y, SURFACE_MIN, SURFACE_MAX, data0.lightLevel(), data0.tintColor());
            return;
        }

        float ratio = (float) amount0 / totalAmount;
        // Animate split ratio when fluid balance changes.
        float animatedRatio = blockEntity.getSplitRatioAnim(ratio, partialTick);

        // Use a fixed split direction for all crucibles.
        float splitX = SURFACE_MIN + (SURFACE_MAX - SURFACE_MIN) * animatedRatio;
        drawSurfaceRect(data0.builder(), poseStack, data0.sprite(), SURFACE_MIN, splitX, y, SURFACE_MIN, SURFACE_MAX, data0.lightLevel(), data0.tintColor());
        drawBoundaryX(data0, poseStack, splitX, FLUID_Y_BASE, y, false);
        renderMixOverlay(poseStack, blockEntity, data0, y, false);

        FluidRenderData data1 = createFluidRenderData(fluid1, level, pos, bufferSource);
        drawSurfaceRect(data1.builder(), poseStack, data1.sprite(), splitX, SURFACE_MAX, y, SURFACE_MIN, SURFACE_MAX, data1.lightLevel(), data1.tintColor());

        drawBoundaryX(data1, poseStack, splitX, FLUID_Y_BASE, y, true);
        renderMixOverlay(poseStack, blockEntity, data1, y, true);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
