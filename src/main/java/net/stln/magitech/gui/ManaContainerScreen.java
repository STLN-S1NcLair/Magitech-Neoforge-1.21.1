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
public abstract class ManaContainerScreen<T extends ManaContainerMenu> extends AbstractContainerScreen<T> {

    private static final ResourceLocation GAUGE_LOCATION = Magitech.id("mana");
    private OwoUIAdapter<FlowLayout> uiAdapter;
    private final int bgWidth = 176;
    protected int panelWidth = 152;
    protected int panelHeight = 34;
    private InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> scrollComponent = null;

    public ManaContainerScreen(T menu, Inventory playerInventory, Component title) {
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
        guiGraphics.blit(getBgTexture(), i, j, 0, 0, this.bgWidth, this.imageHeight);
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
        scrollComponent = ManaContainerPanel.addPanel(root, Positioning.absolute(leftPos + 13, topPos + 51), this.menu, scrollOffset, scrollPosition, panelWidth, panelHeight);
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

    protected ResourceLocation getBgTexture() {
        return Magitech.id("textures/gui/mana_container.png");
    }
}
