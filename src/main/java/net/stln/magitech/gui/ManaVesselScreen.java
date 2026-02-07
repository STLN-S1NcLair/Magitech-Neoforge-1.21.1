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

@OnlyIn(Dist.CLIENT)
public class ManaVesselScreen extends ManaContainerScreen<ManaVesselMenu> {

    public ManaVesselScreen(ManaVesselMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.panelWidth = 128;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null && !hoveredSlot.hasItem() && hoveredSlot.getSlotIndex() < 2) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(
                            switch (hoveredSlot.getSlotIndex()) {
                                case ManaVesselBlockEntity.INPUT -> "gui.magitech.receive";
                                case ManaVesselBlockEntity.OUTPUT -> "gui.magitech.extract";
                                default -> "";
                            }).withColor(0xFFFFFF),
                    x, y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return Magitech.id("textures/gui/mana_vessel.png");
    }
}
