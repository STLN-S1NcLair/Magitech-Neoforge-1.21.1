package net.stln.magitech.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class InfuserScreen extends ManaContainerScreen<InfuserMenu> {
    int progressBarX = 68;
    int progressBarY = 107;
    int progressBarWidth = 40;
    int progressBarHeight = 12;
    int progressBarU = 176;

    public InfuserScreen(InfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 253;
        this.inventoryLabelY = 160;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // Render progress bar
        int barLength = (int) (menu.getProgressRatio() * progressBarWidth);
        guiGraphics.blit(getBgTexture(), this.leftPos + progressBarX, this.topPos + progressBarY,
                progressBarU, 0, barLength, progressBarHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null && !hoveredSlot.hasItem() && hoveredSlot.getSlotIndex() < 2) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(
                            switch (hoveredSlot.getSlotIndex()) {
                                case ManaVesselBlockEntity.INPUT -> "gui.magitech.input";
                                case ManaVesselBlockEntity.OUTPUT -> "gui.magitech.output";
                                default -> "";
                            }).withColor(0xFFFFFF),
                    x, y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return Magitech.id("textures/gui/infuser.png");
    }
}
