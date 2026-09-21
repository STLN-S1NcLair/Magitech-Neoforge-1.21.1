package net.stln.magitech.api.machine.inspection.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.awt.Color;

/**
 * Inspectionのアイテム・液体アイコンに使う歪み描画を担当します。
 * Owns distorted rendering for inspection item and fluid icons.
 */
public final class MachineInspectionDistortedItemRenderer {
    private static final int ICON_CELL_SIZE = 16;
    private static final int ICON_DRAW_SIZE = 12;
    private static final int ICON_DRAW_OFFSET = (ICON_CELL_SIZE - ICON_DRAW_SIZE) / 2;
    private static final float ITEM_ICON_ADDITIVE_Z = 175.0F;
    private static final float ITEM_ICON_ADDITIVE_Z_STEP = 5.0F;
    private static final float TEXT_DISTORTION_SPEED = 2400F;
    private static final ShaderHolder VERTEX_DISTORTED_ITEM_SHADER = new ShaderHolder(
            Magitech.id("inspection_item"),
            DefaultVertexFormat.NEW_ENTITY
    );

    private MachineInspectionDistortedItemRenderer() {
    }

    public static void registerShaders(RegisterShadersEvent event) {
        VERTEX_DISTORTED_ITEM_SHADER.register(event);
    }

    public static void renderDistortedFluidIcon(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            FluidStack fluid,
            int x,
            int y,
            float phaseOffset,
            float fadeAlpha
    ) {
        renderFluidIcon(guiGraphics, minecraft, fluid, x, y, fadeAlpha);

        ExtendedShaderInstance shader = VERTEX_DISTORTED_ITEM_SHADER.getShaderInstance();
        if (shader == null) {
            return;
        }

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTexture = extensions.getStillTexture(fluid);
        TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tint = extensions.getTintColor(fluid);
        Color fluidColor = new Color(tint | 0xFF000000, true);

        renderDistortedFluidIconPass(
                guiGraphics,
                sprite,
                x,
                y,
                fluidColor,
                shader,
                0.2F * fadeAlpha,
                phaseOffset,
                ITEM_ICON_ADDITIVE_Z
        );
        renderDistortedFluidIconPass(
                guiGraphics,
                sprite,
                x,
                y,
                fluidColor,
                shader,
                0.05F * fadeAlpha,
                phaseOffset + 5000.0F,
                ITEM_ICON_ADDITIVE_Z + ITEM_ICON_ADDITIVE_Z_STEP
        );
        renderDistortedFluidIconPass(
                guiGraphics,
                sprite,
                x,
                y,
                fluidColor,
                shader,
                0.05F * fadeAlpha,
                phaseOffset + 10000.0F,
                ITEM_ICON_ADDITIVE_Z + ITEM_ICON_ADDITIVE_Z_STEP * 2.0F
        );
    }

    private static void renderFluidIcon(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            FluidStack fluid,
            int x,
            int y,
            float fadeAlpha
    ) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTexture = extensions.getStillTexture(fluid);
        TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tint = extensions.getTintColor(fluid);
        float red = ((tint >> 16) & 0xFF) / 255.0F;
        float green = ((tint >> 8) & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        guiGraphics.setColor(red, green, blue, fadeAlpha);
        try {
            guiGraphics.blit(x + ICON_DRAW_OFFSET, y + ICON_DRAW_OFFSET, 0, ICON_DRAW_SIZE, ICON_DRAW_SIZE, sprite);
        } finally {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void renderDistortedFluidIconPass(
            GuiGraphics guiGraphics,
            TextureAtlasSprite sprite,
            int x,
            int y,
            Color fluidColor,
            ExtendedShaderInstance shader,
            float alpha,
            float phaseOffset,
            float zOffset
    ) {
        setInspectionItemShaderUniforms(shader, alpha, phaseOffset);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        try {
            pose.translate(x + ICON_CELL_SIZE / 2.0F, y + ICON_CELL_SIZE / 2.0F, zOffset);
            pose.scale(ICON_DRAW_SIZE, -ICON_DRAW_SIZE, ICON_DRAW_SIZE);
            pose.translate(-0.5F, -0.5F, -0.5F);

            BufferBuilder buffer = Tesselator.getInstance().begin(
                    VertexFormat.Mode.QUADS,
                    DefaultVertexFormat.NEW_ENTITY
            );
            addFluidIconVertex(buffer, pose, 0.0F, 0.0F, 0.0F, sprite.getU0(), sprite.getV1(), fluidColor);
            addFluidIconVertex(buffer, pose, 1.0F, 0.0F, 0.0F, sprite.getU1(), sprite.getV1(), fluidColor);
            addFluidIconVertex(buffer, pose, 1.0F, 1.0F, 0.0F, sprite.getU1(), sprite.getV0(), fluidColor);
            addFluidIconVertex(buffer, pose, 0.0F, 1.0F, 0.0F, sprite.getU0(), sprite.getV0(), fluidColor);

            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } finally {
            pose.popPose();
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    private static void addFluidIconVertex(
            BufferBuilder buffer,
            PoseStack pose,
            float x,
            float y,
            float z,
            float u,
            float v,
            Color fluidColor
    ) {
        buffer.addVertex(pose.last(), x, y, z)
                .setColor(fluidColor.getRGB())
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose.last(), 0.0F, 0.0F, 1.0F);
    }

    public static void renderDistortedItemIcon(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            ItemStack stack,
            int x,
            int y,
            float phaseOffset,
            float fadeAlpha
    ) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, fadeAlpha);
        try {
            float centerX = x + ICON_CELL_SIZE / 2.0F;
            float centerY = y + ICON_CELL_SIZE / 2.0F;
            float scale = ICON_DRAW_SIZE / (float) ICON_CELL_SIZE;
            pose.translate(centerX, centerY, 0.0F);
            pose.scale(scale, scale, 1.0F);
            pose.translate(-centerX, -centerY, 0.0F);
            guiGraphics.renderItem(stack, x, y);
        } finally {
            pose.popPose();
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        ExtendedShaderInstance shader = VERTEX_DISTORTED_ITEM_SHADER.getShaderInstance();
        BakedModel model = minecraft.getItemRenderer().getModel(stack, minecraft.level, null, 0);
        if (shader == null || model.isCustomRenderer()) {
            return;
        }

        renderDistortedItemIconPass(
                guiGraphics,
                minecraft,
                stack,
                model,
                x,
                y,
                shader,
                0.2F * fadeAlpha,
                phaseOffset,
                ITEM_ICON_ADDITIVE_Z
        );
        renderDistortedItemIconPass(
                guiGraphics,
                minecraft,
                stack,
                model,
                x,
                y,
                shader,
                0.05F * fadeAlpha,
                phaseOffset + 5000.0F,
                ITEM_ICON_ADDITIVE_Z + ITEM_ICON_ADDITIVE_Z_STEP
        );
        renderDistortedItemIconPass(
                guiGraphics,
                minecraft,
                stack,
                model,
                x,
                y,
                shader,
                0.05F * fadeAlpha,
                phaseOffset + 10000.0F,
                ITEM_ICON_ADDITIVE_Z + ITEM_ICON_ADDITIVE_Z_STEP * 2.0F
        );
    }

    private static void renderDistortedItemIconPass(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            ItemStack stack,
            BakedModel model,
            int x,
            int y,
            ExtendedShaderInstance shader,
            float additiveAlpha,
            float phaseOffset,
            float zOffset
    ) {
        setInspectionItemShaderUniforms(shader, additiveAlpha, phaseOffset);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        try {
            pose.translate(x + ICON_CELL_SIZE / 2.0F, y + ICON_CELL_SIZE / 2.0F, zOffset);
            pose.scale(ICON_DRAW_SIZE, -ICON_DRAW_SIZE, ICON_DRAW_SIZE);
            model = ClientHooks.handleCameraTransforms(pose, model, ItemDisplayContext.GUI, false);
            pose.translate(-0.5F, -0.5F, -0.5F);
            TextureAtlasSprite sprite = model.getParticleIcon();
            BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
            minecraft.getItemRenderer().renderModelLists(
                    model,
                    stack,
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    pose,
                    buffer
            );
            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } finally {
            pose.popPose();
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    private static void setInspectionItemShaderUniforms(
            ExtendedShaderInstance shader,
            float additiveAlpha,
            float phaseOffset
    ) {
        shader.safeGetUniform("Alpha").set(additiveAlpha);
        shader.safeGetUniform("Speed").set(TEXT_DISTORTION_SPEED);
        shader.safeGetUniform("TimeOffset").set(phaseOffset);
        shader.safeGetUniform("XFrequency").set(0.10F);
        shader.safeGetUniform("YFrequency").set(0.08F);
        shader.safeGetUniform("Amplitude").set(0.85F);
    }
}

