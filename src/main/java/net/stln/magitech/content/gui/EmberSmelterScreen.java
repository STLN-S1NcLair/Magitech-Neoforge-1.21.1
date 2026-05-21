package net.stln.magitech.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EmberSmelterScreen extends ManaContainerScreen<EmberSmelterMenu> {

    public EmberSmelterScreen(EmberSmelterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 199;
        this.inventoryLabelY = 106;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        int i = this.leftPos;
        int j = this.topPos;
        int fuel = this.menu.getFuel();
        int progress = this.menu.getProgress();
        float fuelRatio = (fuel + 1 - (float) progress / EmberSmelterBlockEntity.MAX_PROGRESS) / (float) EmberSmelterBlockEntity.MAX_FUEL;
        int fireHeight = (int) (fuelRatio * 16);
        guiGraphics.blit(getBgTexture(), i + 80, j + 60 + 16 - fireHeight, 176, 16 - fireHeight, 16, fireHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null && !hoveredSlot.hasItem() && hoveredSlot.getSlotIndex() == EmberSmelterBlockEntity.FUEL) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(
                            switch (hoveredSlot.getSlotIndex()) {
                                case EmberSmelterBlockEntity.FUEL -> "gui.magitech.ember_crystal";
                                default -> "";
                            }).withColor(0xFFFFFF),
                    x, y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return Magitech.id("textures/gui/ember_smelter.png");
    }
}
