package net.stln.magitech.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.helper.ManaContainerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class ManaContainerScreen<T extends ManaContainerMenu> extends AbstractContainerScreen<T> {

    private static final ResourceLocation GAUGE_LOCATION = Magitech.id("mana");
    private final int bgWidth = 176;

    public ManaContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = bgWidth;
        this.imageHeight = 161;
        this.titleLabelY = 4;
        this.inventoryLabelY = 68;
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

    protected final int gaugeX = 11;
    protected final int gaugeY = 31;
    protected final int gaugeWidth = 154;
    protected final int gaugeHeight = 16;

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(getBgTexture(), i, j, 0, 0, this.bgWidth, this.imageHeight);
        renderGauge(guiGraphics, i + gaugeX, j + gaugeY, gaugeWidth, gaugeHeight, this.menu.getManaRatio());
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        if (x >= leftPos + gaugeX && x < leftPos + gaugeX + gaugeWidth && y >= topPos + gaugeY && y < topPos + gaugeY + gaugeHeight) {
            List<Component> components = new ArrayList<>();
            int titleColor = 0x5a9e91;
            int color = 0xcdffde;
            ManaContainerHelper.addManaContainerBlockInfo(menu.getMana(), menu.getMaxMana(), menu.getFlowRate(), menu.getMaxManaFlow(), menu.getProductionRate(), menu.getConsumptionRate(), components, color, titleColor, menu.hasProduction, menu.hasConsumption);
            guiGraphics.renderTooltip(Minecraft.getInstance().font, components, Optional.empty(), x, y);
        }
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
