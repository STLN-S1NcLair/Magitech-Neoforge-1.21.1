package net.stln.magitech.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaReceiverScreen extends ManaContainerScreen<ManaReceiverMenu> {

    public ManaReceiverScreen(ManaReceiverMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
