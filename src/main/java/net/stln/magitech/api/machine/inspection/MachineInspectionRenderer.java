package net.stln.magitech.api.machine.inspection;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EnergyFormatter;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.LodestoneBufferWrapper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 機械情報表示の共通 HUD 描画処理を提供します。
 * Provides shared HUD rendering for machine inspection data.
 */
public final class MachineInspectionRenderer {
    private static final ResourceLocation INSPECTION_MANA =
            Magitech.id("textures/gui/inspection_mana.png");
    private static final ResourceLocation MANA_DISTORTED =
            Magitech.id("textures/gui/mana_distorted.png");
    private static final ResourceLocation INSPECTION_BACKGROUND =
            Magitech.id("textures/gui/inspection_background.png");
    private static final ResourceLocation INSPECTION_BORDER =
            Magitech.id("textures/gui/inspection_border.png");
    private static final int INSPECTION_BACKGROUND_TEXTURE_SIZE = 32;
    private static final int INSPECTION_BACKGROUND_TEXTURE_BORDER = 8;
    private static final int INSPECTION_BACKGROUND_TILE_SIZE = 16;
    private static final int INSPECTION_BORDER_TEXTURE_SIZE = 64;
    private static final int INSPECTION_BORDER_TEXTURE_BORDER = 16;
    private static final int INSPECTION_BORDER_TEXTURE_TILE_SIZE = 32;
    private static final int INSPECTION_BORDER_DRAW_TILE_SIZE = 32;
    private static final int INSPECTION_BORDER_INSET = 6;
    private static final float INSPECTION_BORDER_PIXEL_SCALE = 0.5F;
    private static final float INSPECTION_BORDER_DISTORTION_INTENSITY = 120.0F;
    private static final int INSPECTION_BORDER_DISTORTION_MARGIN_MIN = 4;
    private static final int INSPECTION_MANA_TEXTURE_WIDTH = 256;
    private static final int INSPECTION_MANA_TEXTURE_HEIGHT = 32;
    private static final int INSPECTION_MANA_DRAW_WIDTH = 128;
    private static final int INSPECTION_MANA_DRAW_HEIGHT = 16;
    private static final int INSPECTION_MANA_DRAW_BORDER = 4;
    private static final int INSPECTION_MANA_DRAW_INNER_HEIGHT =
            INSPECTION_MANA_DRAW_HEIGHT - INSPECTION_MANA_DRAW_BORDER * 2;
    private static final int MANA_DISTORTED_TEXTURE_SIZE = 16;
    private static final int MANA_DISTORTED_FRAME_COUNT = 16;
    private static final float MANA_DISTORTED_FRAME_TIME = 2.0F;
    private static final float TEXT_DISTORTION_SPEED = 2400F;
    private static final float TEXT_DISTORTION_AMPLITUDE = 1.25F;
    private static final int ICON_CELL_SIZE = 16;
    private static final int ICON_DRAW_SIZE = 12;
    private static final int ICON_DRAW_OFFSET = (ICON_CELL_SIZE - ICON_DRAW_SIZE) / 2;
    private static final float ITEM_ICON_ADDITIVE_Z = 175.0F;
    private static final float ITEM_ICON_ADDITIVE_Z_STEP = 5.0F;
    private static final ShaderHolder VERTEX_DISTORTED_TEXT_SHADER = new ShaderHolder(
            Magitech.id("inspection_text"),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
    );
    private static final ShaderHolder DISTORTED_INSPECTION_BORDER_SHADER = new ShaderHolder(
            Magitech.id("inspection_border"),
            DefaultVertexFormat.POSITION_TEX_COLOR
    );
    private static final ShaderHolder VERTEX_DISTORTED_ITEM_SHADER = new ShaderHolder(
            Magitech.id("inspection_item"),
            DefaultVertexFormat.NEW_ENTITY
    );
    private static final RenderTypeProvider DISTORTED_TEXT = new RenderTypeProvider(
            token -> LodestoneRenderTypes.createAdditiveRenderType(
                    "magitech_distorted_text",
                    token,
                    VERTEX_DISTORTED_TEXT_SHADER
            )
    );
    private static final Function<GuiGraphics, LodestoneBufferWrapper> ADDITIVE_TEXT_BUFFER =
            Util.memoize(guiGraphics -> new LodestoneBufferWrapper(LodestoneRenderTypes.ADDITIVE_TEXT, guiGraphics.bufferSource()));
    private static final Function<GuiGraphics, LodestoneBufferWrapper> DISTORTED_TEXT_BUFFER =
            Util.memoize(guiGraphics -> new LodestoneBufferWrapper(DISTORTED_TEXT, guiGraphics.bufferSource()));
    private static final int PANEL_PADDING = 8;
    private static final int PANEL_HORIZONTAL_PADDING = 12;
    private static final int TEXT_HORIZONTAL_MARGIN = 0;
    private static final int DEFAULT_ITEM_CONTENT_WIDTH = 196;
    private static final int TITLE_HEIGHT = 20;
    private static final int ITEM_COLUMNS = 2;
    private static final int ITEM_ROW_HEIGHT = ICON_CELL_SIZE;
    private static final int ITEM_COLUMN_GAP = 2;
    private static final int ITEM_TEXT_OFFSET = ICON_CELL_SIZE;
    private static final int ITEM_NAME_RIGHT_PADDING = 2;
    private static final int MANA_GAUGE_HEIGHT = 8;
    private static final int MANA_GAUGE_VERTICAL_MARGIN = 4;
    private static final int MANA_GAUGE_ROW_HEIGHT =
            INSPECTION_MANA_DRAW_HEIGHT + MANA_GAUGE_VERTICAL_MARGIN * 2;
    private static final int PANEL_OFFSET_X = 32;
    private static final int PANEL_OFFSET_Y = -24;

    private MachineInspectionRenderer() {
    }

    /**
     * 検査HUDの文字に使う頂点歪みシェーダーを登録します。
     * Registers the vertex-distortion shader used by the inspection HUD text.
     */
    public static void registerShaders(RegisterShadersEvent event) {
        VERTEX_DISTORTED_TEXT_SHADER.register(event);
        DISTORTED_INSPECTION_BORDER_SHADER.register(event);
        VERTEX_DISTORTED_ITEM_SHADER.register(event);
    }

    /**
     * 機械名、マナ、流量、追加情報、アイテムスタック、液体内容をゴーグル風 HUD に描画します。
     * Renders the machine name, mana, flow, extra information, item stacks, and fluid contents in a goggles-style HUD.
     */
    public static void render(GuiGraphics guiGraphics, Component title, MachineInspectionData data) {
        render(guiGraphics, title, ItemStack.EMPTY, data);
    }

    /**
     * 機械アイコン付きで機械情報を視点の横へ描画します。
     * Renders machine information beside the crosshair with a machine icon.
     */
    public static void render(
            GuiGraphics guiGraphics,
            Component title,
            ItemStack icon,
            MachineInspectionData data
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        render(
                guiGraphics,
                title,
                icon,
                data,
                minecraft.getTimer().getGameTimeDeltaPartialTick(false)
        );
    }

    /**
     * フレーム補間値を指定して機械情報を描画します。
     * Renders machine information using the supplied frame interpolation value.
     */
    public static void render(
            GuiGraphics guiGraphics,
            Component title,
            ItemStack icon,
            MachineInspectionData data,
            float partialTick
    ) {
        render(guiGraphics, title, icon, data, partialTick, 1.0F);
    }

    public static void render(
            GuiGraphics guiGraphics,
            Component title,
            ItemStack icon,
            MachineInspectionData data,
            float partialTick,
            float fadeAlpha
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        fadeAlpha = Math.clamp(fadeAlpha, 0.0F, 1.0F);
        float animationPhase = getAnimationPhase(minecraft, partialTick);
        List<ItemStack> visibleItems = data.items().stream()
                .filter(stack -> !stack.isEmpty())
                .toList();
        List<FluidStack> visibleFluids = data.fluids().stream()
                .filter(fluid -> !fluid.isEmpty())
                .toList();
        ResourceLocation fieldEffectId = data.fieldEffect();
        FieldEffectType fieldEffectType = fieldEffectId == null
                ? null
                : MagitechRegistries.FIELD_EFFECT_TYPE.get(fieldEffectId);
        Component fieldEffectText = fieldEffectId == null
                ? null
                : FieldEffectIconRenderer.getDisplayName(fieldEffectId);
        FieldInfluenceInstance fieldInfluences = data.fieldInfluences();
        if (fieldInfluences == null && fieldEffectType != null) {
            fieldInfluences = fieldEffectType.getCondition();
        }
        Component fieldEffectInfluencesText = createFieldInfluencesText(fieldInfluences);
        if (fieldEffectText != null && fieldEffectInfluencesText != null) {
            fieldEffectText = fieldEffectText.copy()
                    .append(Component.literal(" <- "))
                    .append(fieldEffectInfluencesText);
        }
        TimeGaugeData timeGauge = createTimeGaugeData(data);
        Component manaText = Component.translatable(
                "gui.magitech.mana_capacity").append(
                Component.literal(": ").append(EnergyFormatter.formatEnergy(data.mana(), data.maxMana()))
        );
        Component remainingTimeText = timeGauge == null ? null : createTimeText(
                "gui.magitech.remaining_time",
                timeGauge.remainingTicks(),
                timeGauge.durationTicks()
        );
        Component totalRemainingTimeText = timeGauge == null ? null : Component.translatable(
                "gui.magitech.total_remaining_time").append(
                Component.literal(": ").append(formatTime(timeGauge.totalRemainingTicks()))
        );
        List<Component> timeLines = timeGauge == null
                ? List.of()
                : List.of(remainingTimeText, totalRemainingTimeText);
        Component flowText = Component.translatable(
                "gui.magitech.mana_flow").append(
                Component.literal(": ").append(EnergyFormatter.formatFlow(data.flowRate(), data.maxFlow()))
        );
        List<Component> infoLines = createManaRateLines(data);
        infoLines.addAll(data.extraLines());

        int itemRows = (visibleItems.size() + ITEM_COLUMNS - 1) / ITEM_COLUMNS;
        int fluidRows = (visibleFluids.size() + ITEM_COLUMNS - 1) / ITEM_COLUMNS;
        int contentHeight = TITLE_HEIGHT
                + 10
                + MANA_GAUGE_ROW_HEIGHT
                + (timeGauge == null ? 0 : 10 + MANA_GAUGE_ROW_HEIGHT + 11)
                + 11
                + infoLines.size() * 10;
        if (!visibleItems.isEmpty()) {
            contentHeight += 3 + 11 + itemRows * ITEM_ROW_HEIGHT;
        }
        if (!visibleFluids.isEmpty()) {
            contentHeight += 3 + fluidRows * ITEM_ROW_HEIGHT;
        }
        if (fieldEffectText != null) {
            contentHeight += 3 + 11 + ITEM_ROW_HEIGHT;
        }

        int contentWidth = calculateContentWidth(
                minecraft,
                title,
                manaText,
                flowText,
                infoLines,
                timeLines,
                visibleItems,
                visibleFluids,
                icon,
                fieldEffectText
        );
        int borderWidth = roundUpToMultiple(
                contentWidth + PANEL_HORIZONTAL_PADDING * 2,
                INSPECTION_BORDER_DRAW_TILE_SIZE
        );
        int borderHeight = roundUpToMultiple(contentHeight + PANEL_PADDING * 2, INSPECTION_BORDER_DRAW_TILE_SIZE);
        int backgroundWidth = borderWidth + INSPECTION_BORDER_INSET * 2;
        int backgroundHeight = borderHeight + INSPECTION_BORDER_INSET * 2;

        int backgroundX = guiGraphics.guiWidth() / 2 + PANEL_OFFSET_X - INSPECTION_BORDER_INSET;
        backgroundX = Math.min(backgroundX, guiGraphics.guiWidth() - backgroundWidth - 8);
        backgroundX = Math.max(8, backgroundX);
        int backgroundY = guiGraphics.guiHeight() / 2 + PANEL_OFFSET_Y - INSPECTION_BORDER_INSET;
        if (backgroundY + backgroundHeight + 8 > guiGraphics.guiHeight()) {
            backgroundY = guiGraphics.guiHeight() - backgroundHeight - 8;
        }
        backgroundY = Math.max(8, backgroundY);
        int borderX = backgroundX + INSPECTION_BORDER_INSET;
        int borderY = backgroundY + INSPECTION_BORDER_INSET;

        renderInspectionBackground(guiGraphics, backgroundX, backgroundY, backgroundWidth, backgroundHeight, fadeAlpha);
        renderDistortedInspectionBorder(guiGraphics, borderX, borderY, borderWidth, borderHeight, fadeAlpha);

        int contentX = borderX + PANEL_HORIZONTAL_PADDING;
        int y = borderY + PANEL_PADDING;
        if (!icon.isEmpty()) {
            renderDistortedItemIcon(guiGraphics, minecraft, icon, contentX, y + 1, 0.0F, fadeAlpha);
        }
        int titleX = contentX + (icon.isEmpty() ? 0 : ICON_CELL_SIZE);
        int textColor = Element.MANA.getTextColor().getRGB() + 0xFF000000;
        int glow = Element.MANA.getPrimary().getRGB() + 0xFF000000;
        int dark = Element.MANA.getSecondary().getRGB() + 0xFF000000;
        int darker = Element.MANA.getDark().getRGB() + 0xFF000000;
        Color fieldEffectPrimary = fieldEffectType == null ? Color.WHITE : fieldEffectType.getPrimary();
        Color fieldEffectSecondary = fieldEffectType == null ? Color.WHITE : fieldEffectType.getSecondary();
        int fieldEffectTextColor = fieldEffectPrimary.getRGB() + 0xFF000000;
        int fieldEffectGlow = fieldEffectPrimary.getRGB() + 0xFF000000;
        int fieldEffectDark = fieldEffectSecondary.getRGB() + 0xFF000000;
        int fieldEffectDarker = fieldEffectSecondary.getRGB() + 0xFF000000;
        drawWavyText(guiGraphics, minecraft, title, titleX, y + 5, textColor, glow, dark, darker, 0xFF, animationPhase, 0.0F, fadeAlpha);
        y += TITLE_HEIGHT;
        renderManaGauge(
                guiGraphics,
                contentX,
                y + MANA_GAUGE_VERTICAL_MARGIN,
                MANA_GAUGE_HEIGHT,
                data.manaRatio(),
                partialTick,
                fadeAlpha
        );
        y += MANA_GAUGE_ROW_HEIGHT;

        drawWavyText(guiGraphics, minecraft, manaText, contentX + TEXT_HORIZONTAL_MARGIN, y, textColor, glow, dark, darker, 0xFF, animationPhase, 0.7F, fadeAlpha);
        y += 10;

        drawWavyText(guiGraphics, minecraft, flowText, contentX + TEXT_HORIZONTAL_MARGIN, y, textColor, glow, dark, darker, 0xFF, animationPhase, 1.4F, fadeAlpha);
        y += 11;

        for (int index = 0; index < infoLines.size(); index++) {
            Component line = infoLines.get(index);
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    line,
                    contentX + TEXT_HORIZONTAL_MARGIN,
                    y,
                    textColor,
                    glow,
                    dark,
                    darker,
                    0xFF,
                    animationPhase,
                    1.8F + index * 0.35F,
                    fadeAlpha
            );
            y += 10;
        }

        if (timeGauge != null) {
            Element timeElement = timeGauge.element();
            int timeTextColor = timeElement.getTextColor().getRGB() + 0xFF000000;
            int timeGlow = timeElement.getPrimary().getRGB() + 0xFF000000;
            int timeDark = timeElement.getSecondary().getRGB() + 0xFF000000;
            int timeDarker = timeElement.getDark().getRGB() + 0xFF000000;
            double timeRatio = timeGauge.durationTicks() > 0
                    ? (double) timeGauge.remainingTicks() / timeGauge.durationTicks()
                    : 0.0D;
            renderManaGauge(
                    guiGraphics,
                    contentX,
                    y + MANA_GAUGE_VERTICAL_MARGIN,
                    MANA_GAUGE_HEIGHT,
                    timeRatio,
                    partialTick,
                    timeElement,
                    fadeAlpha
            );
            y += MANA_GAUGE_ROW_HEIGHT;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    remainingTimeText,
                    contentX + TEXT_HORIZONTAL_MARGIN,
                    y,
                    timeTextColor,
                    timeGlow,
                    timeDark,
                    timeDarker,
                    0xFF,
                    animationPhase,
                    2.5F,
                    fadeAlpha
            );
            y += 10;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    totalRemainingTimeText,
                    contentX + TEXT_HORIZONTAL_MARGIN,
                    y,
                    timeTextColor,
                    timeGlow,
                    timeDark,
                    timeDarker,
                    0xFF,
                    animationPhase,
                    2.9F,
                    fadeAlpha
            );
            y += 11;
        }

        if (!visibleItems.isEmpty()) {
            y += 3;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    Component.translatable("gui.magitech.contents"),
                    contentX + TEXT_HORIZONTAL_MARGIN,
                    y,
                    textColor,
                    glow, dark, darker, 0xFF, animationPhase,
                    3.3F,
                    fadeAlpha
            );
            y += 11;
            renderItemStacks(guiGraphics, minecraft, contentX, y, visibleItems, contentWidth, animationPhase, fadeAlpha);
            y += itemRows * ITEM_ROW_HEIGHT;
        }

        if (!visibleFluids.isEmpty()) {
            y += 3;
            renderFluidStacks(guiGraphics, minecraft, contentX, y, visibleFluids, contentWidth, animationPhase, fadeAlpha);
            y += fluidRows * ITEM_ROW_HEIGHT;
        }

        if (fieldEffectText != null) {
            y += 3;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    Component.translatable("gui.magitech.field_effect"),
                    contentX + TEXT_HORIZONTAL_MARGIN,
                    y,
                    fieldEffectTextColor,
                    fieldEffectGlow,
                    fieldEffectDark,
                    fieldEffectDarker,
                    0xFF,
                    animationPhase,
                    3.7F,
                    fadeAlpha
            );
            y += 11;
            FieldEffectIconRenderer.render(guiGraphics, fieldEffectId, contentX, y, ICON_CELL_SIZE, fadeAlpha);
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    fieldEffectText,
                    contentX + ITEM_TEXT_OFFSET + TEXT_HORIZONTAL_MARGIN,
                    y + 5,
                    fieldEffectTextColor,
                    fieldEffectGlow,
                    fieldEffectDark,
                    fieldEffectDarker,
                    0xFF,
                    animationPhase,
                    4.1F,
                    fadeAlpha
            );
        }
    }

    private static List<Component> createManaRateLines(MachineInspectionData data) {
        List<Component> lines = new ArrayList<>(2);
        if (data.productionRate() > 0) {
            lines.add(Component.translatable("gui.magitech.mana_production").append(": ")
                    .append(EnergyFormatter.formatAverage(data.productionRate())));
        }
        if (data.consumptionRate() > 0) {
            lines.add(Component.translatable("gui.magitech.mana_consumption").append(": ")
                    .append(EnergyFormatter.formatAverage(data.consumptionRate())));
        }
        return lines;
    }

    private static Component createFieldInfluencesText(FieldInfluenceInstance fieldInfluences) {
        if (fieldInfluences == null || fieldInfluences.fieldInfluences() == null
                || fieldInfluences.fieldInfluences().isEmpty()) {
            return null;
        }

        List<FieldInfluence> influences = fieldInfluences.fieldInfluences().stream()
                .filter(influence -> influence != null && influence.type() != null && influence.intensity() > 0)
                .sorted(Comparator.comparingInt(FieldInfluence::intensity).reversed()
                        .thenComparingInt(MachineInspectionRenderer::getFieldInfluenceOrder))
                .toList();
        MutableComponent result = null;
        for (FieldInfluence influence : influences) {
            ResourceLocation key = MagitechRegistries.FIELD_INFLUENCE_TYPE.getKey(influence.type());
            if (key == null) {
                continue;
            }

            MutableComponent entry = Component.translatable(
                    "field_influence." + key.getNamespace() + "." + key.getPath()
            );
            if (influence.intensity() > 1) {
                entry.append(Component.literal(" x" + influence.intensity()));
            }
            result = result == null
                    ? entry
                    : result.append(Component.literal(" + ")).append(entry);
        }
        return result;
    }

    private static int getFieldInfluenceOrder(FieldInfluence influence) {
        int order = 0;
        for (FieldInfluenceType registeredType : MagitechRegistries.FIELD_INFLUENCE_TYPE) {
            if (registeredType == influence.type()) {
                return order;
            }
            order++;
        }
        return Integer.MAX_VALUE;
    }

    private static TimeGaugeData createTimeGaugeData(MachineInspectionData data) {
        Element element = switch (data.timeGaugeElement()) {
            case MachineInspectionData.TIME_GAUGE_EMBER -> Element.EMBER;
            case MachineInspectionData.TIME_GAUGE_GLACE -> Element.GLACE;
            default -> null;
        };
        if (element == null) {
            return null;
        }
        return new TimeGaugeData(
                Math.max(0L, data.remainingTimeTicks()),
                Math.max(0L, data.remainingTimeDurationTicks()),
                Math.max(0L, data.totalRemainingTimeTicks()),
                element
        );
    }

    private static Component createTimeText(String key, long remainingTicks, long durationTicks) {
        return Component.translatable(key).append(
                Component.literal(": ")
                        .append(formatTime(remainingTicks))
                        .append(Component.literal(" / "))
                        .append(formatTime(durationTicks))
        );
    }

    private static Component formatTime(long ticks) {
        long totalSeconds = (Math.max(0L, ticks) + 19L) / 20L;
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        return Component.literal(minutes + ":" + (seconds < 10L ? "0" : "") + seconds);
    }

    private record TimeGaugeData(
            long remainingTicks,
            long durationTicks,
            long totalRemainingTicks,
            Element element
    ) {
    }

    private static int calculateContentWidth(
            Minecraft minecraft,
            Component title,
            Component manaText,
            Component flowText,
            List<Component> extraLines,
            List<Component> timeLines,
            List<ItemStack> visibleItems,
            List<FluidStack> visibleFluids,
            ItemStack icon,
            Component fieldEffectText
    ) {
        int width = minecraft.font.width(title.getString()) + (icon.isEmpty() ? 0 : ICON_CELL_SIZE);
        width = Math.max(width, minecraft.font.width(manaText.getString()));
        width = Math.max(width, minecraft.font.width(flowText.getString()));
        for (Component line : extraLines) {
            width = Math.max(width, minecraft.font.width(line.getString()));
        }
        for (Component line : timeLines) {
            width = Math.max(width, minecraft.font.width(line.getString()));
        }
        if (!visibleItems.isEmpty() || !visibleFluids.isEmpty()) {
            int[] itemColumnWidths = calculateItemColumnWidths(minecraft, visibleItems);
            int[] fluidColumnWidths = calculateFluidColumnWidths(minecraft, visibleFluids);
            width = Math.max(
                    width,
                    Math.max(itemColumnWidths[0], fluidColumnWidths[0])
                            + Math.max(itemColumnWidths[1], fluidColumnWidths[1])
                            + ITEM_COLUMN_GAP
            );
        }
        if (!visibleItems.isEmpty()) {
            width = Math.max(width, minecraft.font.width(Component.translatable("gui.magitech.contents").getString()));
        }
        if (fieldEffectText != null) {
            width = Math.max(
                    width,
                    ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING + minecraft.font.width(fieldEffectText.getString())
            );
        }
        return Math.max(INSPECTION_MANA_DRAW_WIDTH, width);
    }

    private static int[] calculateItemColumnWidths(Minecraft minecraft, List<ItemStack> items) {
        int minimumColumnWidth = ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING;
        int[] columnWidths = {minimumColumnWidth, minimumColumnWidth};
        for (int index = 0; index < items.size(); index++) {
            ItemStack stack = items.get(index);
            int column = index % ITEM_COLUMNS;
            int nameWidth = minecraft.font.width(getItemDisplayName(stack));
            columnWidths[column] = Math.max(
                    columnWidths[column],
                    ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING + nameWidth
            );
        }
        return columnWidths;
    }

    private static int[] calculateFluidColumnWidths(Minecraft minecraft, List<FluidStack> fluids) {
        int minimumColumnWidth = ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING;
        int[] columnWidths = {minimumColumnWidth, minimumColumnWidth};
        for (int index = 0; index < fluids.size(); index++) {
            FluidStack fluid = fluids.get(index);
            int column = index % ITEM_COLUMNS;
            int nameWidth = minecraft.font.width(getFluidDisplayName(fluid));
            columnWidths[column] = Math.max(
                    columnWidths[column],
                    ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING + nameWidth
            );
        }
        return columnWidths;
    }

    private static String getItemDisplayName(ItemStack stack) {
        String itemName = stack.getHoverName().getString();
        return stack.getCount() == 1 ? itemName : itemName + " x" + stack.getCount();
    }

    private static String getFluidDisplayName(FluidStack fluid) {
        return fluid.getHoverName().getString() + " " + fluid.getAmount() + " mB";
    }

    /**
     * inspection_mana のフレームと mana_distorted のアニメーションでマナゲージを描画します。
     * Renders the mana gauge with the inspection_mana frame and animated mana_distorted texture.
     */
    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            double ratio
    ) {
        renderManaGauge(
                guiGraphics,
                x,
                y,
                height,
                ratio
        );
    }

    private static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        renderManaGauge(
                guiGraphics,
                x,
                y,
                height,
                ratio,
                minecraft.getTimer().getGameTimeDeltaPartialTick(false)
        );
    }

    private static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, Element.MANA, 1.0F);
    }

    private static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            float fadeAlpha
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, Element.MANA, fadeAlpha);
    }

    private static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            Element element
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, element, 1.0F);
    }

    private static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            Element element,
            float fadeAlpha
    ) {
        double clampedRatio = Math.clamp(ratio, 0.0D, 1.0D);
        int gaugeWidth = INSPECTION_MANA_DRAW_WIDTH;
        int fillHeight = Math.min(MANA_GAUGE_HEIGHT, Math.max(0, height));
        int innerWidth = Math.max(0, gaugeWidth - INSPECTION_MANA_DRAW_BORDER * 2);
        float currentWidth = (float) (innerWidth * clampedRatio);
        renderManaFrame(guiGraphics, x, y, element, fadeAlpha);
        if (currentWidth > 0 && fillHeight > 0) {
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getPrimary(),
                    fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    0.0F
            );
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getSecondary(),
                    0.5F * fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    1000.0F
            );
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getDark(),
                    0.5F * fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    2000.0F
            );
        }
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y) {
        renderManaFrame(guiGraphics, x, y, Element.MANA);
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y, Element element) {
        renderManaFrame(guiGraphics, x, y, element, 1.0F);
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y, Element element, float fadeAlpha) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, fadeAlpha);
            try {
                guiGraphics.blit(
                        INSPECTION_MANA,
                        x,
                        y,
                        INSPECTION_MANA_DRAW_WIDTH,
                        INSPECTION_MANA_DRAW_HEIGHT,
                        0,
                        0,
                        INSPECTION_MANA_TEXTURE_WIDTH,
                        INSPECTION_MANA_TEXTURE_HEIGHT,
                        INSPECTION_MANA_TEXTURE_WIDTH,
                        INSPECTION_MANA_TEXTURE_HEIGHT
                );
            } finally {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            return;
        }

        int width = INSPECTION_MANA_DRAW_WIDTH;
        int height = INSPECTION_MANA_DRAW_HEIGHT;
        int marginX = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        int marginY = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        float textureBorder = INSPECTION_MANA_DRAW_BORDER / INSPECTION_BORDER_PIXEL_SCALE;
        float tileWidth = INSPECTION_MANA_TEXTURE_WIDTH - textureBorder * 2.0F;
        float tileHeight = INSPECTION_MANA_TEXTURE_HEIGHT - textureBorder * 2.0F;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(770, 1);
        try {
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getPrimary(),
                    0.7F * fadeAlpha,
                    0.0F
            );
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getSecondary(),
                    0.5F * fadeAlpha,
                    1000.0F
            );
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getDark(),
                    0.5F * fadeAlpha,
                    2000.0F
            );
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private static void renderManaFramePass(
            GuiGraphics guiGraphics,
            ExtendedShaderInstance shader,
            int x,
            int y,
            int width,
            int height,
            int marginX,
            int marginY,
            float tileWidth,
            float tileHeight,
            Color color,
            float alpha,
            float phaseOffset
    ) {
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                INSPECTION_MANA_TEXTURE_WIDTH,
                INSPECTION_MANA_TEXTURE_HEIGHT,
                INSPECTION_MANA_DRAW_BORDER / INSPECTION_BORDER_PIXEL_SCALE,
                tileWidth,
                tileHeight,
                1.0F,
                0,
                0.0F,
                phaseOffset
        );
        shader.safeGetUniform("Speed").set(420.0F);
        VFXBuilders.createScreen()
                .setShader(shader)
                .setTexture(INSPECTION_MANA)
                .setColor(color, alpha)
                .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                .setPositionWithWidth(
                        x - marginX,
                        y - marginY,
                        width + marginX * 2,
                        height + marginY * 2
                )
                .blit(guiGraphics.pose());
        guiGraphics.bufferSource().endBatch();
    }

    private static void renderDistortedManaFill(
            GuiGraphics guiGraphics,
            int x,
            int y,
            Color color,
            float alpha,
            float width,
            int height,
            float partialTick,
            float phaseOffset
    ) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null || width <= 0 || height <= 0) {
            return;
        }

        int marginX = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        int marginY = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        double animationFrame = getManaAnimationFrame(partialTick);
        long animationFrameIndex = (long) Math.floor(animationFrame);
        int frameIndex = (int) Math.floorMod(
                animationFrameIndex,
                (long) MANA_DISTORTED_FRAME_COUNT
        );
        float frameBlend = (float) (animationFrame - animationFrameIndex);
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_TEXTURE_SIZE,
                0.0F,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_FRAME_COUNT,
                frameIndex,
                frameBlend,
                phaseOffset
        );
        Minecraft.getInstance().getTextureManager()
                .getTexture(MANA_DISTORTED)
                .setFilter(true, false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        try {
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(MANA_DISTORTED)
                    .setColor(color, alpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(
                            x - marginX,
                            y - marginY,
                            width + marginX * 2,
                            height + marginY * 2
                    )
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    /**
     * 後方互換用に色引数を受け取るマナゲージ描画メソッドです。色はテクスチャとエフェクトで決まります。
     * Compatibility overload accepting a color argument; the textures and effects determine the displayed color.
     */
    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            double ratio,
            int ignoredFillColor
    ) {
        renderManaGauge(guiGraphics, x, y, width, height, ratio);
    }

    /**
     * アイテムスタックを Create の情報行に近いアイコンと名称の形式で描画します。
     * Renders item stacks as icon-and-name information rows similar to Create's overlay.
     */
    public static void renderItemStacks(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            int x,
            int y,
            List<ItemStack> items
    ) {
        renderItemStacks(guiGraphics, minecraft, x, y, items, DEFAULT_ITEM_CONTENT_WIDTH, getAnimationPhase(minecraft, 0.0F), 1.0F);
    }

    private static void renderItemStacks(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            int x,
            int y,
            List<ItemStack> items,
            int width,
            float animationPhase,
            float fadeAlpha
    ) {
        int[] itemColumnWidths = calculateItemColumnWidths(minecraft, items);
        int requiredWidth = itemColumnWidths[0] + itemColumnWidths[1] + ITEM_COLUMN_GAP;
        if (requiredWidth > width) {
            int columnWidth = Math.max(ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING,
                    (width - ITEM_COLUMN_GAP) / ITEM_COLUMNS);
            itemColumnWidths[0] = columnWidth;
            itemColumnWidths[1] = columnWidth;
        }
        String ellipsis = "...";
        int ellipsisWidth = minecraft.font.width(ellipsis);
        for (int index = 0; index < items.size(); index++) {
            ItemStack stack = items.get(index);
            int column = index % ITEM_COLUMNS;
            int row = index / ITEM_COLUMNS;
            int itemX = x;
            for (int previousColumn = 0; previousColumn < column; previousColumn++) {
                itemX += itemColumnWidths[previousColumn] + ITEM_COLUMN_GAP;
            }
            int itemY = y + row * ITEM_ROW_HEIGHT;

            renderDistortedItemIcon(guiGraphics, minecraft, stack, itemX, itemY, index * 7.0F, fadeAlpha);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, fadeAlpha);
            try {
                guiGraphics.renderItemDecorations(minecraft.font, stack, itemX, itemY, "");
            } finally {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            String itemName = getItemDisplayName(stack);
            int textWidth = itemColumnWidths[column] - ITEM_TEXT_OFFSET - ITEM_NAME_RIGHT_PADDING;
            if (minecraft.font.width(itemName) > textWidth) {
                int substringWidth = Math.max(1, textWidth - ellipsisWidth);
                itemName = minecraft.font.plainSubstrByWidth(itemName, substringWidth) + ellipsis;
            }
            int textColor = Element.MANA.getTextColor().getRGB() + 0xFF000000;
            int glow = Element.MANA.getPrimary().getRGB() + 0xFF000000;
            int dark = Element.MANA.getSecondary().getRGB() + 0xFF000000;
            int darker = Element.MANA.getDark().getRGB() + 0xFF000000;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    Component.literal(itemName),
                    itemX + ITEM_TEXT_OFFSET + TEXT_HORIZONTAL_MARGIN,
                    itemY + 5,
                    textColor,
                    glow,
                    dark,
                    darker,
                    0xFF,
                    animationPhase,
                    2.8F + index * 0.35F,
                    fadeAlpha
            );
        }
    }

    private static void renderFluidStacks(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            int x,
            int y,
            List<FluidStack> fluids,
            int width,
            float animationPhase,
            float fadeAlpha
    ) {
        int[] fluidColumnWidths = calculateFluidColumnWidths(minecraft, fluids);
        int requiredWidth = fluidColumnWidths[0] + fluidColumnWidths[1] + ITEM_COLUMN_GAP;
        if (requiredWidth > width) {
            int columnWidth = Math.max(ITEM_TEXT_OFFSET + ITEM_NAME_RIGHT_PADDING,
                    (width - ITEM_COLUMN_GAP) / ITEM_COLUMNS);
            fluidColumnWidths[0] = columnWidth;
            fluidColumnWidths[1] = columnWidth;
        }
        String ellipsis = "...";
        int ellipsisWidth = minecraft.font.width(ellipsis);
        for (int index = 0; index < fluids.size(); index++) {
            FluidStack fluid = fluids.get(index);
            int column = index % ITEM_COLUMNS;
            int row = index / ITEM_COLUMNS;
            int fluidX = x;
            for (int previousColumn = 0; previousColumn < column; previousColumn++) {
                fluidX += fluidColumnWidths[previousColumn] + ITEM_COLUMN_GAP;
            }
            int fluidY = y + row * ITEM_ROW_HEIGHT;

            renderDistortedFluidIcon(guiGraphics, minecraft, fluid, fluidX, fluidY, index * 7.0F, fadeAlpha);

            String fluidName = getFluidDisplayName(fluid);
            int textWidth = fluidColumnWidths[column] - ITEM_TEXT_OFFSET - ITEM_NAME_RIGHT_PADDING;
            if (minecraft.font.width(fluidName) > textWidth) {
                int substringWidth = Math.max(1, textWidth - ellipsisWidth);
                fluidName = minecraft.font.plainSubstrByWidth(fluidName, substringWidth) + ellipsis;
            }
            int textColor = Element.MANA.getTextColor().getRGB() + 0xFF000000;
            int glow = Element.MANA.getPrimary().getRGB() + 0xFF000000;
            int dark = Element.MANA.getSecondary().getRGB() + 0xFF000000;
            int darker = Element.MANA.getDark().getRGB() + 0xFF000000;
            drawWavyText(
                    guiGraphics,
                    minecraft,
                    Component.literal(fluidName),
                    fluidX + ITEM_TEXT_OFFSET + TEXT_HORIZONTAL_MARGIN,
                    fluidY + 5,
                    textColor,
                    glow,
                    dark,
                    darker,
                    0xFF,
                    animationPhase,
                    2.8F + index * 0.35F,
                    fadeAlpha
            );
        }
    }

    private static void renderDistortedFluidIcon(
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

    private static void renderDistortedItemIcon(
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

    private static double getManaAnimationFrame(float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return 0.0D;
        }
        return (minecraft.level.getGameTime() + (double) partialTick)
                / MANA_DISTORTED_FRAME_TIME;
    }

    private static float getAnimationPhase(Minecraft minecraft, float partialTick) {
        return minecraft.level == null
                ? 0.0F
                : minecraft.level.getGameTime() + partialTick;
    }

    private static void drawWavyText(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            Component text,
            int x,
            int y,
            int color,
            int glowColor,
            int darkColor,
            int darkerColor,
            int baseAlpha,
            float animationPhase,
            float phaseOffset,
            float fadeAlpha
    ) {
        int fadedBaseAlpha = Mth.floor(baseAlpha * fadeAlpha);
        guiGraphics.drawString(minecraft.font, text, x, y, color + (fadedBaseAlpha << 24), false);

        float phase = animationPhase + phaseOffset;
        int alpha = Mth.floor(255.0F * fadeAlpha
                * (0.7F + Mth.abs(0.3F * Mth.sin((float) ((phase / 20.0F) % Math.TAU)))));
        int base = (alpha << 24) | glowColor;
        int dim = (base & 0xFFFFFF) | ((alpha / 3) << 24);
        int baseDark = (alpha << 24) | darkColor;
        int dimmer = (baseDark & 0xFFFFFF) | ((alpha / 4) << 24);
        int baseDarker = (alpha << 24) | darkerColor;
        int darkerDimmer = (baseDarker & 0xFFFFFF) | ((alpha / 4) << 24);
        String textValue = text.getString();
        ExtendedShaderInstance distortedShader = VERTEX_DISTORTED_TEXT_SHADER.getShaderInstance();
        LodestoneBufferWrapper buffer = distortedShader == null
                ? ADDITIVE_TEXT_BUFFER.apply(guiGraphics)
                : DISTORTED_TEXT_BUFFER.apply(guiGraphics);
        var pose = guiGraphics.pose().last().pose();
        Font font = minecraft.font;
        RenderSystem.enableBlend();
        if (distortedShader != null) {
            setTextVertexShaderUniforms(distortedShader, phaseOffset);
            font.drawInBatch(textValue, x, y, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();

            setTextVertexShaderUniforms(distortedShader, phaseOffset + 5000F);
            font.drawInBatch(textValue, x, y, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();

            setTextVertexShaderUniforms(distortedShader, phaseOffset + 10000F);
            font.drawInBatch(textValue, x, y, darkerDimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();
        } else {
            float offsetMultiplier = Mth.sin((float) ((phase / 10.0F) % Math.TAU));
            float xOffset = 2.0F * offsetMultiplier;
            float yOffset = 1.0F * offsetMultiplier;
            font.drawInBatch(textValue, x + xOffset, y, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x - xOffset, y, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x, y + yOffset, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x, y - yOffset, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
        }
        RenderSystem.defaultBlendFunc();
    }

    private static void renderInspectionBackground(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            float fadeAlpha
    ) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, fadeAlpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        try {
            int textureBorder = INSPECTION_BACKGROUND_TEXTURE_BORDER;
            int interiorWidth = width - textureBorder * 2;
            int interiorHeight = height - textureBorder * 2;

            blitInspectionBackground(guiGraphics, x, y, textureBorder, textureBorder, 0, 0, textureBorder, textureBorder);
            blitInspectionBackground(guiGraphics, x + width - textureBorder, y,
                    textureBorder, textureBorder, INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder, 0, textureBorder, textureBorder);
            blitInspectionBackground(guiGraphics, x, y + height - textureBorder,
                    textureBorder, textureBorder, 0, INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder, textureBorder, textureBorder);
            blitInspectionBackground(guiGraphics, x + width - textureBorder, y + height - textureBorder,
                    textureBorder, textureBorder,
                    INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder,
                    INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder,
                    textureBorder,
                    textureBorder);

            for (int offset = 0; offset < interiorWidth; offset += INSPECTION_BACKGROUND_TILE_SIZE) {
                int tileWidth = Math.min(INSPECTION_BACKGROUND_TILE_SIZE, interiorWidth - offset);
                blitInspectionBackground(guiGraphics, x + textureBorder + offset, y,
                        tileWidth, textureBorder, textureBorder, 0, tileWidth, textureBorder);
                blitInspectionBackground(guiGraphics, x + textureBorder + offset, y + height - textureBorder,
                        tileWidth, textureBorder, textureBorder, INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder, tileWidth, textureBorder);
            }
            for (int offset = 0; offset < interiorHeight; offset += INSPECTION_BACKGROUND_TILE_SIZE) {
                int tileHeight = Math.min(INSPECTION_BACKGROUND_TILE_SIZE, interiorHeight - offset);
                blitInspectionBackground(guiGraphics, x, y + textureBorder + offset,
                        textureBorder, tileHeight, 0, textureBorder, textureBorder, tileHeight);
                blitInspectionBackground(guiGraphics, x + width - textureBorder, y + textureBorder + offset,
                        textureBorder, tileHeight, INSPECTION_BACKGROUND_TEXTURE_SIZE - textureBorder, textureBorder, textureBorder, tileHeight);
            }
            for (int offsetX = 0; offsetX < interiorWidth; offsetX += INSPECTION_BACKGROUND_TILE_SIZE) {
                int tileWidth = Math.min(INSPECTION_BACKGROUND_TILE_SIZE, interiorWidth - offsetX);
                for (int offsetY = 0; offsetY < interiorHeight; offsetY += INSPECTION_BACKGROUND_TILE_SIZE) {
                    int tileHeight = Math.min(INSPECTION_BACKGROUND_TILE_SIZE, interiorHeight - offsetY);
                    blitInspectionBackground(guiGraphics,
                            x + textureBorder + offsetX,
                            y + textureBorder + offsetY,
                            tileWidth,
                            tileHeight,
                            textureBorder,
                            textureBorder,
                            tileWidth,
                            tileHeight);
                }
            }
        } finally {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    private static void blitInspectionBackground(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            int u,
            int v,
            int uWidth,
            int vHeight
    ) {
        guiGraphics.blit(INSPECTION_BACKGROUND, x, y, width, height, u, v, uWidth, vHeight,
                INSPECTION_BACKGROUND_TEXTURE_SIZE, INSPECTION_BACKGROUND_TEXTURE_SIZE);
    }

    private static void renderDistortedInspectionBorder(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            float fadeAlpha
    ) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(770, 1);
        try {
            int marginX = Math.max(
                    INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                    Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
            );
            int marginY = Math.max(
                    INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                    Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
            );
            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 0.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getPrimary(), 0.7F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();

            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 1000.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getSecondary(), 0.5F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();

            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 2000.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getDark(), 0.5F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private static void setInspectionBorderShaderUniforms(
            ExtendedShaderInstance shader,
            int width,
            int height,
            int marginX,
            int marginY,
            float phaseOffset
    ) {
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                INSPECTION_BORDER_TEXTURE_SIZE,
                INSPECTION_BORDER_TEXTURE_SIZE,
                INSPECTION_BORDER_TEXTURE_BORDER,
                INSPECTION_BORDER_TEXTURE_TILE_SIZE,
                INSPECTION_BORDER_TEXTURE_TILE_SIZE,
                1.0F,
                0.0F,
                0.0F,
                phaseOffset
        );
    }

    private static void setInspectionTextureShaderUniforms(
            ExtendedShaderInstance shader,
            float width,
            int height,
            int marginX,
            int marginY,
            float textureWidth,
            float textureHeight,
            float borderSize,
            float tileWidth,
            float tileHeight,
            float frameCount,
            float frameIndex,
            float frameBlend,
            float phaseOffset
    ) {
        shader.safeGetUniform("Width").set((float) width);
        shader.safeGetUniform("Height").set((float) height);
        shader.safeGetUniform("MarginX").set((float) marginX);
        shader.safeGetUniform("MarginY").set((float) marginY);
        shader.safeGetUniform("BorderSize").set(borderSize);
        shader.safeGetUniform("TileWidth").set(tileWidth);
        shader.safeGetUniform("TileHeight").set(tileHeight);
        shader.safeGetUniform("TextureSize").set(textureWidth);
        shader.safeGetUniform("TextureWidth").set(textureWidth);
        shader.safeGetUniform("TextureHeight").set(textureHeight);
        shader.safeGetUniform("PixelScale").set(INSPECTION_BORDER_PIXEL_SCALE);
        shader.safeGetUniform("Speed").set(420.0F);
        shader.safeGetUniform("Intensity").set(INSPECTION_BORDER_DISTORTION_INTENSITY);
        shader.safeGetUniform("YFrequency").set(5.0F);
        shader.safeGetUniform("XFrequency").set(5.0F);
        shader.safeGetUniform("FrameCount").set(frameCount);
        shader.safeGetUniform("FrameIndex").set(frameIndex);
        shader.safeGetUniform("FrameBlend").set(frameBlend);
        shader.safeGetUniform("TimeOffset").set(phaseOffset);
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

    private static int roundUpToMultiple(int value, int multiple) {
        return Math.max(multiple, ((value + multiple - 1) / multiple) * multiple);
    }

    private static void setTextVertexShaderUniforms(
            ExtendedShaderInstance shader,
            float phaseOffset
    ) {
        shader.safeGetUniform("Speed").set(TEXT_DISTORTION_SPEED);
        shader.safeGetUniform("TimeOffset").set(phaseOffset);
        shader.safeGetUniform("XFrequency").set(0.10F);
        shader.safeGetUniform("YFrequency").set(0.08F);
        shader.safeGetUniform("Amplitude").set(TEXT_DISTORTION_AMPLITUDE);
    }

}
