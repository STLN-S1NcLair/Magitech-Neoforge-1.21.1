package net.stln.magitech.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ManaPumpScreen extends ManaContainerScreen<ManaPumpMenu> {

    public ManaPumpScreen(ManaPumpMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
