package net.stln.magitech.gui;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ManaVesselScreen extends AbstractContainerScreen<ManaVesselMenu> {
    private static final ResourceLocation BG_LOCATION = Magitech.id("textures/gui/mana_vessel.png");
    private static final ResourceLocation GAUGE_LOCATION = Magitech.id("mana");
    private static final int SCROLLER_WIDTH = 8;
    private static final int SCROLLER_HEIGHT = 8;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 18;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 46;
    private static final int RECIPES_Y = 31;
    ItemStack stack = null;
    private float scrollOffs;
    private OwoUIAdapter<FlowLayout> uiAdapter;
    private int bgWidth = 176;
    private InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> scrollComponent = null;

    public ManaVesselScreen(ManaVesselMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = bgWidth;
        this.imageHeight = 199;
        this.titleLabelY = 4;
        this.inventoryLabelY = 106;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        reloadUI();
    }

    /**
     * Renders the graphical user interface (GUI) element.
     *
     * @param guiGraphics the GuiGraphics object used for rendering.
     * @param mouseX      the x-coordinate of the mouse cursor.
     * @param mouseY      the y-coordinate of the mouse cursor.
     * @param partialTick the partial tick time.
     */
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.bgWidth, this.imageHeight);
        renderGauge(guiGraphics, i + 11, j + 31, 154, 16, this.menu.getManaRatio());
    }

    @Override
    protected void init() {
        super.init();
        this.uiAdapter = OwoUIAdapter.create(this, Containers::verticalFlow);
        reloadUI();
    }

    private void reloadUI() {
        FlowLayout root = this.uiAdapter.rootComponent;
        double scrollOffset = scrollComponent == null ? 0 : scrollComponent.getScrollOffset();
        double scrollPosition = scrollComponent == null ? 0 : scrollComponent.getScrollPosition();
        root.clearChildren();
        scrollComponent = ManaContainerPanel.addPanel(root, Positioning.absolute(leftPos + 13, topPos + 51), this.menu.getMana(), this.menu.getMaxMana(), this.menu.getFlowRate(), this.menu.getMaxManaFlow(), scrollOffset, scrollPosition);
        this.uiAdapter.inflateAndMount();
    }

    protected void renderGauge(GuiGraphics guiGraphics, int x, int y, int wid, int hei, double ratio) {
        int currentWidth = (int) Math.round(wid * ratio);

        if (currentWidth > 0) {
            for (int dx = 0; dx < currentWidth; dx += 16) {
                for (int dy = 0; dy < hei; dy += 16) {
                    int drawX = x + dx;
                    int drawY = y + dy;

                    // 描画サイズ計算（端数処理）
                    int w = Math.min(16, currentWidth - dx);
                    int h = Math.min(16, height - dy);

                    // Spriteを使用して描画
                    guiGraphics.blitSprite(GAUGE_LOCATION, 16, 16, 0, (16 - h) / 2, drawX, drawY, w, h);
                }
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null && !hoveredSlot.hasItem() && hoveredSlot.getSlotIndex() < 2) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(
                            switch (hoveredSlot.getSlotIndex()) {
                                case ManaVesselBlockEntity.RECEIVE_SLOT -> "gui.magitech.receive";
                                case ManaVesselBlockEntity.EXTRACT_SLOT -> "gui.magitech.extract";
                                default -> "";
                            }).withColor(0xFFFFFF),
                    x, y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }
}
