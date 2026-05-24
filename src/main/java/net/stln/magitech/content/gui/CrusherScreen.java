package net.stln.magitech.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;

@OnlyIn(Dist.CLIENT)
public class CrusherScreen extends ManaContainerScreen<CrusherMenu> {

    public CrusherScreen(CrusherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 199;
        this.inventoryLabelY = 106;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        int i = this.leftPos;
        int j = this.topPos;
        int progress = this.menu.getProgress();
        float progressRatio = (progress + (progress == 0 ? 0 : partialTick)) / CrusherBlockEntity.MAX_PROGRESS;
        guiGraphics.blit(getBgTexture(), i + 63, j + 60, 176, 16, (int) (50 * progressRatio), 16);
        if (this.menu.getMana() > 0) {
            guiGraphics.blit(getBgTexture(), i + 80, j + 60, 176, 0, 16, 16);
        }
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return Magitech.id("textures/gui/crusher.png");
    }
}
