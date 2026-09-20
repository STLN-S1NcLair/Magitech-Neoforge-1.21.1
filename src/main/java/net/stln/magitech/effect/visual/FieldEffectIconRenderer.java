package net.stln.magitech.effect.visual;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.Color;
import java.util.List;

/**
 * フィールド効果のGUIアイコンを共通の歪み描画で表示します。
 * Renders field-effect GUI icons with shared distortion rendering.
 */
@OnlyIn(Dist.CLIENT)
public final class FieldEffectIconRenderer {
    private static final int DEFAULT_CELL_SIZE = 16;
    private static final float ICON_SIZE_RATIO = 0.75F;
    private static final float DEFAULT_DISPLAY_SCALE = ICON_SIZE_RATIO;
    private static final float PRIMARY_ALPHA = 1.0F;
    private static final float DISTORTED_PRIMARY_ALPHA = 0.5F;
    private static final float DISTORTED_SECONDARY_ALPHA = 0.5F;
    private static final int FIELD_EFFECT_ICON_TEXTURE_SIZE = 16;
    private static final float FIELD_EFFECT_DISTORTION_INTENSITY = 30.0F;
    private static final float FIELD_EFFECT_DISTORTION_MARGIN =
            1.0F / FIELD_EFFECT_DISTORTION_INTENSITY + 1.0F / FIELD_EFFECT_ICON_TEXTURE_SIZE;
    private static final double WORLD_FACE_OFFSET = 0.30D;
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
        render(guiGraphics, fieldEffectId, x, y, DEFAULT_CELL_SIZE, 1.0F, DEFAULT_DISPLAY_SCALE);
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
        render(guiGraphics, effect, x, y, DEFAULT_CELL_SIZE, 1.0F, DEFAULT_DISPLAY_SCALE);
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
        renderWorld(poseStack, bufferSource, null, facing, effect, size, alpha);
    }

    /**
     * ワールド座標を歪み基準へ渡してフィールド効果アイコンを描画します。
     * Renders a field-effect icon while supplying its world position as the distortion basis.
     */
    public static void renderWorld(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Vec3 blockCenter,
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
            Vec3 localCenter = new Vec3(
                    0.5D + facing.getStepX() * WORLD_FACE_OFFSET,
                    0.5D + facing.getStepY() * WORLD_FACE_OFFSET,
                    0.5D + facing.getStepZ() * WORLD_FACE_OFFSET
            );
            poseStack.translate(
                    localCenter.x,
                    localCenter.y,
                    localCenter.z
            );
            orientWorldIcon(poseStack, facing);

            ResourceLocation texture = effect.getIconTexture();
            Vec3 worldCenter = blockCenter == null ? localCenter : blockCenter.add(localCenter.subtract(new Vec3(0.5D, 0.5D, 0.5D)));
            Vec3[] worldAxes = getWorldPlaneAxes(facing);
            float renderSize = safeSize * (1.0F + FIELD_EFFECT_DISTORTION_MARGIN * 2.0F);
            List<AdditiveRectangleRenderer.LocalWorldPlane> planes = List.of(
                    new AdditiveRectangleRenderer.LocalWorldPlane(
                            Vec3.ZERO,
                            new Vec3(1.0D, 0.0D, 0.0D),
                            new Vec3(0.0D, 1.0D, 0.0D),
                            worldCenter,
                            worldAxes[0],
                            worldAxes[1],
                            renderSize,
                            renderSize,
                            0,
                            1
                    )
            );
            AdditiveRectangleRenderer.renderLocalWorldPlanesDoubleSidedTileGradientBatch(
                    poseStack,
                    bufferSource,
                    planes,
                    RenderTypeTokenInit.createWorldDistortedToken(
                            texture,
                            1,
                            5000.0F,
                            FIELD_EFFECT_DISTORTION_MARGIN
                    ),
                    effect.getPrimary(),
                    effect.getPrimary(),
                    DISTORTED_PRIMARY_ALPHA * safeAlpha,
                    RenderTypeTokenInit.FIELD_EFFECT_DISTORTED,
                    FIELD_EFFECT_ICON_TEXTURE_SIZE
            );
            AdditiveRectangleRenderer.renderLocalWorldPlanesDoubleSidedTileGradientBatch(
                    poseStack,
                    bufferSource,
                    planes,
                    RenderTypeTokenInit.createWorldDistortedToken(
                            texture,
                            1,
                            10000.0F,
                            FIELD_EFFECT_DISTORTION_MARGIN
                    ),
                    effect.getSecondary(),
                    effect.getSecondary(),
                    DISTORTED_SECONDARY_ALPHA * safeAlpha,
                    RenderTypeTokenInit.FIELD_EFFECT_DISTORTED,
                    FIELD_EFFECT_ICON_TEXTURE_SIZE
            );
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
        render(guiGraphics, fieldEffectId, x, y, cellSize, alpha, DEFAULT_DISPLAY_SCALE);
    }

    /**
     * 表示倍率を指定してフィールド効果アイコンを描画します。
     * Renders a field-effect icon with an explicit display scale.
     */
    public static void render(
            GuiGraphics guiGraphics,
            ResourceLocation fieldEffectId,
            int x,
            int y,
            int cellSize,
            float alpha,
            float displayScale
    ) {
        if (fieldEffectId == null) {
            return;
        }

        FieldEffectType effect = MagitechRegistries.FIELD_EFFECT_TYPE.get(fieldEffectId);
        if (effect != null) {
            render(guiGraphics, effect, x, y, cellSize, alpha, displayScale);
            return;
        }

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                fieldEffectId.getNamespace(),
                "textures/field_effect/" + fieldEffectId.getPath() + ".png"
        );
        renderTexture(guiGraphics, texture, x, y, cellSize, alpha, displayScale);
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
        render(guiGraphics, effect, x, y, cellSize, alpha, DEFAULT_DISPLAY_SCALE);
    }

    /**
     * 表示倍率を指定してフィールド効果タイプのアイコンを描画します。
     * Renders a field-effect type icon with an explicit display scale.
     */
    public static void render(
            GuiGraphics guiGraphics,
            FieldEffectType effect,
            int x,
            int y,
            int cellSize,
            float alpha,
            float displayScale
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
                displayScale,
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
        renderTexture(guiGraphics, texture, x, y, cellSize, alpha, DEFAULT_DISPLAY_SCALE);
    }

    /**
     * 表示倍率を指定して歪みテクスチャアイコンを描画します。
     * Renders a distorted texture icon with an explicit display scale.
     */
    public static void renderTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int cellSize,
            float alpha,
            float displayScale
    ) {
        renderTexture(
                guiGraphics,
                texture,
                x,
                y,
                cellSize,
                alpha,
                displayScale,
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
        renderTexture(
                guiGraphics,
                texture,
                x,
                y,
                cellSize,
                alpha,
                DEFAULT_DISPLAY_SCALE,
                primary,
                secondary
        );
    }

    /**
     * 指定色と表示倍率で、任意のGUIに歪みアイコンを描画します。
     * Renders a distorted icon in any GUI with the supplied colors and display scale.
     */
    public static void renderTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int cellSize,
            float alpha,
            float displayScale,
            Color primary,
            Color secondary
    ) {
        int safeCellSize = Math.max(1, cellSize);
        float safeAlpha = Math.clamp(alpha, 0.0F, 1.0F);
        if (safeAlpha <= 0.0F) {
            return;
        }

        float safeDisplayScale = Math.max(0.001F, displayScale);
        int drawSize = Math.max(1, Math.round(safeCellSize * safeDisplayScale));
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

    private static Vec3[] getWorldPlaneAxes(Direction facing) {
        return switch (facing) {
            case DOWN -> new Vec3[]{
                    new Vec3(1.0D, 0.0D, 0.0D),
                    new Vec3(0.0D, 0.0D, 1.0D)
            };
            case UP -> new Vec3[]{
                    new Vec3(1.0D, 0.0D, 0.0D),
                    new Vec3(0.0D, 0.0D, -1.0D)
            };
            case NORTH -> new Vec3[]{
                    new Vec3(-1.0D, 0.0D, 0.0D),
                    new Vec3(0.0D, 1.0D, 0.0D)
            };
            case SOUTH -> new Vec3[]{
                    new Vec3(1.0D, 0.0D, 0.0D),
                    new Vec3(0.0D, 1.0D, 0.0D)
            };
            case EAST -> new Vec3[]{
                    new Vec3(0.0D, 0.0D, -1.0D),
                    new Vec3(0.0D, 1.0D, 0.0D)
            };
            case WEST -> new Vec3[]{
                    new Vec3(0.0D, 0.0D, 1.0D),
                    new Vec3(0.0D, 1.0D, 0.0D)
            };
        };
    }
}
