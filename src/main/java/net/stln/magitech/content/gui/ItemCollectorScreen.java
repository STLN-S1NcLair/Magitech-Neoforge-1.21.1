package net.stln.magitech.content.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemCollectorScreen extends ManaContainerScreen<ItemCollectorMenu> {

    public ItemCollectorScreen(ItemCollectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
