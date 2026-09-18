package net.stln.magitech.effect.visual;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.Color;

/**
 * フィールド効果のGUIアイコンを共通の歪み描画で表示します。
 * Renders field-effect GUI icons with shared distortion rendering.
 */
@OnlyIn(Dist.CLIENT)
public final class FieldEffectIconRenderer {
    private static final int DEFAULT_CELL_SIZE = 16;
    private static final float ICON_SIZE_RATIO = 0.75F;
    private static final float PRIMARY_ALPHA = 1.0F;
    private static final float DISTORTED_PRIMARY_ALPHA = 0.5F;
    private static final float DISTORTED_SECONDARY_ALPHA = 0.5F;
    private static final float FIRST_PASS_Z = 175.0F;
    private static final float Z_STEP = 5.0F;
    private static final int FULL_BRIGHT = 15728880;

    private FieldEffectIconRenderer() {
    }

    /**
     * フィールド効果IDを解決してGUIアイコンを描画します。
     * Resolves a field-effect ID and renders its GUI icon.
     */
    public static void render(
            GuiGraphics guiGraphics,
            ResourceLocation fieldEffectId,
            int x,
            int y
    ) {
        render(guiGraphics, fieldEffectId, x, y, DEFAULT_CELL_SIZE, 1.0F);
    }

    /**
     * フィールド効果タイプを直接指定して標準サイズのアイコンを描画します。
     * Renders a directly supplied field-effect type at the standard icon size.
     */
    public static void render(
            GuiGraphics guiGraphics,
            FieldEffectType effect,
            int x,
            int y
    ) {
        render(guiGraphics, effect, x, y, DEFAULT_CELL_SIZE, 1.0F);
    }

    /**
     * ブロックの指定面にフィールド効果アイコンを描画します。
     * Renders a field-effect icon on the specified block face.
     */
    public static void renderWorld(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Direction facing,
            FieldEffectType effect,
            float size,
            float alpha
    ) {
        if (poseStack == null || bufferSource == null || facing == null
                || effect == null || effect.getIconTexture() == null) {
            return;
        }

        float safeSize = Math.max(0.001F, size);
        float safeAlpha = Math.clamp(alpha, 0.0F, 1.0F);
        if (safeAlpha <= 0.0F) {
            return;
        }

        poseStack.pushPose();
        try {
            poseStack.translate(
                    0.5F + facing.getStepX() * 0.51F,
                    0.5F + facing.getStepY() * 0.51F,
                    0.5F + facing.getStepZ() * 0.51F
            );
            orientWorldIcon(poseStack, facing);

            ResourceLocation texture = effect.getIconTexture();
            RenderTypeToken primaryToken = RenderTypeToken.createToken(texture);
            RenderTypeToken distortedPrimaryToken = RenderTypeTokenInit.createDistortedIconToken(texture, 5000.0F);
            RenderTypeToken distortedSecondaryToken = RenderTypeTokenInit.createDistortedIconToken(texture, 10000.0F);
            renderWorldPass(bufferSource, poseStack, RenderTypeTokenInit.ADDITIVE_ICON, primaryToken,
                    safeSize, effect.getPrimary(), PRIMARY_ALPHA * safeAlpha);
            renderWorldPass(bufferSource, poseStack, RenderTypeTokenInit.DISTORTED_ICON, distortedPrimaryToken,
                    safeSize, effect.getPrimary(), DISTORTED_PRIMARY_ALPHA * safeAlpha);
            renderWorldPass(bufferSource, poseStack, RenderTypeTokenInit.DISTORTED_ICON, distortedSecondaryToken,
                    safeSize, effect.getSecondary(), DISTORTED_SECONDARY_ALPHA * safeAlpha);
        } finally {
            poseStack.popPose();
        }
    }

    /**
     * 指定したセルサイズとアルファ値でフィールド効果アイコンを描画します。
     * Renders a field-effect icon with the supplied cell size and alpha.
     */
    public static void render(
            GuiGraphics guiGraphics,
            ResourceLocation fieldEffectId,
            int x,
            int y,
            int cellSize,
            float alpha
    ) {
        if (fieldEffectId == null) {
            return;
        }

        FieldEffectType effect = MagitechRegistries.FIELD_EFFECT_TYPE.get(fieldEffectId);
        if (effect != null) {
            render(guiGraphics, effect, x, y, cellSize, alpha);
            return;
        }

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                fieldEffectId.getNamespace(),
                "textures/field_effect/" + fieldEffectId.getPath() + ".png"
        );
        renderTexture(guiGraphics, texture, x, y, cellSize, alpha);
    }

    /**
     * フィールド効果タイプを直接指定してGUIアイコンを描画します。
     * Renders a GUI icon for a directly supplied field-effect type.
     */
    public static void render(
            GuiGraphics guiGraphics,
            FieldEffectType effect,
            int x,
            int y,
            int cellSize,
            float alpha
    ) {
        if (effect == null || effect.getIconTexture() == null) {
            return;
        }
        renderTexture(
                guiGraphics,
                effect.getIconTexture(),
                x,
                y,
                cellSize,
                alpha,
                effect.getPrimary(),
                effect.getSecondary()
        );
    }

    /**
     * テクスチャIDを指定して、任意のGUIで再利用できる歪みアイコンを描画します。
     * Renders a distorted icon reusable by any GUI when given a texture ID.
     */
    public static void renderTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int cellSize,
            float alpha
    ) {
        renderTexture(
                guiGraphics,
                texture,
                x,
                y,
                cellSize,
                alpha,
                Color.WHITE,
                Color.WHITE
        );
    }

    /**
     * 指定した色で、任意のGUIで再利用できる歪みアイコンを描画します。
     * Renders a distorted icon reusable by any GUI with the supplied colors.
     */
    public static void renderTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int cellSize,
            float alpha,
            Color primary,
            Color secondary
    ) {
        int safeCellSize = Math.max(1, cellSize);
        float safeAlpha = Math.clamp(alpha, 0.0F, 1.0F);
        if (safeAlpha <= 0.0F) {
            return;
        }

        int drawSize = Math.max(1, Math.round(safeCellSize * ICON_SIZE_RATIO));
        int drawOffset = (safeCellSize - drawSize) / 2;
        int drawX = x + drawOffset;
        int drawY = y + drawOffset;

        RenderTypeToken primaryToken = RenderTypeToken.createToken(texture);
        RenderTypeToken distortedPrimaryToken = RenderTypeTokenInit.createDistortedIconToken(texture, 5000.0F);
        RenderTypeToken distortedSecondaryToken = RenderTypeTokenInit.createDistortedIconToken(texture, 10000.0F);
        renderPass(guiGraphics, RenderTypeTokenInit.ADDITIVE_ICON, primaryToken, drawX, drawY, drawSize,
                primary, PRIMARY_ALPHA * safeAlpha, FIRST_PASS_Z);
        renderPass(guiGraphics, RenderTypeTokenInit.DISTORTED_ICON, distortedPrimaryToken, drawX, drawY, drawSize,
                primary, DISTORTED_PRIMARY_ALPHA * safeAlpha, FIRST_PASS_Z + Z_STEP);
        renderPass(guiGraphics, RenderTypeTokenInit.DISTORTED_ICON, distortedSecondaryToken, drawX, drawY, drawSize,
                secondary, DISTORTED_SECONDARY_ALPHA * safeAlpha, FIRST_PASS_Z + Z_STEP * 2.0F);
    }

    /**
     * フィールド効果IDに対応する表示名を返します。
     * Returns the display name associated with a field-effect ID.
     */
    public static Component getDisplayName(ResourceLocation fieldEffectId) {
        if (fieldEffectId == null) {
            return Component.empty();
        }
        FieldEffectType effect = MagitechRegistries.FIELD_EFFECT_TYPE.get(fieldEffectId);
        return effect == null
                ? Component.translatable("field_effect." + fieldEffectId.getNamespace() + "." + fieldEffectId.getPath())
                : effect.getDisplayName();
    }

    private static void renderPass(
            GuiGraphics guiGraphics,
            RenderTypeProvider renderTypeProvider,
            RenderTypeToken textureToken,
            int x,
            int y,
            int size,
            Color color,
            float alpha,
            float z
    ) {
        var renderType = renderTypeProvider.apply(textureToken).getRenderType();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        try {
            poseStack.translate(x + size * 0.5F, y + size * 0.5F, z);
            poseStack.scale(1.0F, -1.0F, 1.0F);
            new VFXBuilders.WorldVFXBuilder()
                    .replaceBufferSource(guiGraphics.bufferSource())
                    .setRenderType(renderType)
                    .setColor(color, alpha)
                    .setLight(FULL_BRIGHT)
                    .renderQuad(poseStack, size * 0.5F, size * 0.5F);
            guiGraphics.bufferSource().endBatch(renderType);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderWorldPass(
            MultiBufferSource bufferSource,
            PoseStack poseStack,
            RenderTypeProvider renderTypeProvider,
            RenderTypeToken textureToken,
            float size,
            Color color,
            float alpha
    ) {
        var renderType = renderTypeProvider.apply(textureToken).getRenderType();
        new VFXBuilders.WorldVFXBuilder()
                .replaceBufferSource(bufferSource)
                .setRenderType(renderType)
                .setColor(color, alpha)
                .setLight(FULL_BRIGHT)
                .renderQuad(poseStack, size * 0.5F, size * 0.5F);
    }

    private static void orientWorldIcon(PoseStack poseStack, Direction facing) {
        switch (facing) {
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            case SOUTH -> {
            }
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        }
    }
}
