package net.stln.magitech.api.machine.inspection;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.api.machine.inspection.client.render.MachineInspectionDistortedFrameRenderer;
import net.stln.magitech.api.machine.inspection.client.render.MachineInspectionDistortedItemRenderer;
import net.stln.magitech.api.machine.inspection.client.render.MachineInspectionDistortedTextRenderer;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EnergyFormatter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 機械情報表示の共通 HUD 描画処理を提供します。
 * Provides shared HUD rendering for machine inspection data.
 */
public final class MachineInspectionRenderer {
    private static final ResourceLocation INSPECTION_BACKGROUND =
            Magitech.id("textures/gui/inspection_background.png");
    private static final int INSPECTION_BACKGROUND_TEXTURE_SIZE = 32;
    private static final int INSPECTION_BACKGROUND_TEXTURE_BORDER = 8;
    private static final int INSPECTION_BACKGROUND_TILE_SIZE = 16;
    private static final int INSPECTION_BORDER_DRAW_TILE_SIZE = 32;
    private static final int INSPECTION_BORDER_INSET = 6;
    private static final int ICON_CELL_SIZE = 16;
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
            MachineInspectionDistortedFrameRenderer.MANA_DRAW_HEIGHT + MANA_GAUGE_VERTICAL_MARGIN * 2;
    private static final int PANEL_OFFSET_X = 32;
    private static final int PANEL_OFFSET_Y = -24;

    private MachineInspectionRenderer() {
    }

    /**
     * 検査HUDの文字に使う頂点歪みシェーダーを登録します。
     * Registers the vertex-distortion shader used by the inspection HUD text.
     */
    public static void registerShaders(RegisterShadersEvent event) {
        MachineInspectionDistortedTextRenderer.registerShaders(event);
        MachineInspectionDistortedFrameRenderer.registerShaders(event);
        MachineInspectionDistortedItemRenderer.registerShaders(event);
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
        boolean hasMana = data.maxMana() > 0L;
        ResourceLocation fieldEffectId = data.fieldEffect();
        boolean showFieldEffect = fieldEffectId != null || data.fieldInfluences() != null;
        FieldEffectType fieldEffectType = fieldEffectId == null
                ? null
                : MagitechRegistries.FIELD_EFFECT_TYPE.get(fieldEffectId);
        Component fieldEffectText = !showFieldEffect
                ? null
                : fieldEffectId == null
                        ? Component.translatable("gui.magitech.field_effect.none")
                        : FieldEffectIconRenderer.getDisplayName(fieldEffectId);
        FieldInfluenceInstance fieldInfluences = data.fieldInfluences();
        if (fieldInfluences == null && fieldEffectType != null) {
            fieldInfluences = fieldEffectType.getCondition();
        }
        Component fieldEffectInfluencesText = createFieldInfluencesText(fieldInfluences);
        if (fieldEffectId != null && fieldEffectText != null && fieldEffectInfluencesText != null) {
            fieldEffectText = fieldEffectText.copy()
                    .append(Component.literal(" <- "))
                    .append(fieldEffectInfluencesText);
        }
        TimeGaugeData timeGauge = createTimeGaugeData(data);
        Component manaText = hasMana
                ? Component.translatable("gui.magitech.mana_capacity").append(
                        Component.literal(": ").append(EnergyFormatter.formatEnergy(data.mana(), data.maxMana()))
                )
                : null;
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
        Component flowText = hasMana
                ? Component.translatable("gui.magitech.mana_flow").append(
                        Component.literal(": ").append(EnergyFormatter.formatFlow(data.flowRate(), data.maxFlow()))
                )
                : null;
        List<Component> infoLines = hasMana ? createManaRateLines(data) : new ArrayList<>();
        infoLines.addAll(data.extraLines());

        int itemRows = (visibleItems.size() + ITEM_COLUMNS - 1) / ITEM_COLUMNS;
        int fluidRows = (visibleFluids.size() + ITEM_COLUMNS - 1) / ITEM_COLUMNS;
        int contentHeight = TITLE_HEIGHT
                + 10
                + (hasMana ? MANA_GAUGE_ROW_HEIGHT + 11 : 0)
                + (timeGauge == null ? 0 : 10 + MANA_GAUGE_ROW_HEIGHT + 11)
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
        MachineInspectionDistortedFrameRenderer.renderBorder(guiGraphics, borderX, borderY, borderWidth, borderHeight, fadeAlpha);

        int contentX = borderX + PANEL_HORIZONTAL_PADDING;
        int y = borderY + PANEL_PADDING;
        if (!icon.isEmpty()) {
            MachineInspectionDistortedItemRenderer.renderDistortedItemIcon(guiGraphics, minecraft, icon, contentX, y + 1, 0.0F, fadeAlpha);
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
        MachineInspectionDistortedTextRenderer.draw(guiGraphics, minecraft, title, titleX, y + 5, textColor, glow, dark, darker, 0xFF, animationPhase, 0.0F, fadeAlpha);
        y += TITLE_HEIGHT;
        if (hasMana) {
            MachineInspectionDistortedFrameRenderer.renderManaGauge(
                    guiGraphics,
                    contentX,
                    y + MANA_GAUGE_VERTICAL_MARGIN,
                    MANA_GAUGE_HEIGHT,
                    data.manaRatio(),
                    partialTick,
                    fadeAlpha
            );
            y += MANA_GAUGE_ROW_HEIGHT;

            MachineInspectionDistortedTextRenderer.draw(guiGraphics, minecraft, manaText, contentX + TEXT_HORIZONTAL_MARGIN, y, textColor, glow, dark, darker, 0xFF, animationPhase, 0.7F, fadeAlpha);
            y += 10;

            MachineInspectionDistortedTextRenderer.draw(guiGraphics, minecraft, flowText, contentX + TEXT_HORIZONTAL_MARGIN, y, textColor, glow, dark, darker, 0xFF, animationPhase, 1.4F, fadeAlpha);
            y += 11;
        }

        for (int index = 0; index < infoLines.size(); index++) {
            Component line = infoLines.get(index);
            MachineInspectionDistortedTextRenderer.draw(
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
            MachineInspectionDistortedFrameRenderer.renderManaGauge(
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
            MachineInspectionDistortedTextRenderer.draw(
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
            MachineInspectionDistortedTextRenderer.draw(
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
            MachineInspectionDistortedTextRenderer.draw(
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
            MachineInspectionDistortedTextRenderer.draw(
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
            MachineInspectionDistortedTextRenderer.draw(
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
        if (manaText != null) {
            width = Math.max(width, minecraft.font.width(manaText.getString()));
        }
        if (flowText != null) {
            width = Math.max(width, minecraft.font.width(flowText.getString()));
        }
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
        return Math.max(MachineInspectionDistortedFrameRenderer.MANA_DRAW_WIDTH, width);
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
     * inspection_manaのゲージを歪み描画込みで描画します。
     * Renders the inspection mana gauge with distorted rendering.
     */
    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            double ratio
    ) {
        MachineInspectionDistortedFrameRenderer.renderManaGauge(
                guiGraphics,
                x,
                y,
                width,
                height,
                ratio
        );
    }

    /**
     * 後方互換用に色引数を受け取るゲージ描画メソッドです。
     * Compatibility overload accepting a legacy color argument.
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
        MachineInspectionDistortedFrameRenderer.renderManaGauge(
                guiGraphics,
                x,
                y,
                width,
                height,
                ratio,
                ignoredFillColor
        );
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

            MachineInspectionDistortedItemRenderer.renderDistortedItemIcon(guiGraphics, minecraft, stack, itemX, itemY, index * 7.0F, fadeAlpha);
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
            MachineInspectionDistortedTextRenderer.draw(
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

            MachineInspectionDistortedItemRenderer.renderDistortedFluidIcon(guiGraphics, minecraft, fluid, fluidX, fluidY, index * 7.0F, fadeAlpha);

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
            MachineInspectionDistortedTextRenderer.draw(
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



    private static float getAnimationPhase(Minecraft minecraft, float partialTick) {
        return minecraft.level == null
                ? 0.0F
                : minecraft.level.getGameTime() + partialTick;
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



    private static int roundUpToMultiple(int value, int multiple) {
        return Math.max(multiple, ((value + multiple - 1) / multiple) * multiple);
    }



}


