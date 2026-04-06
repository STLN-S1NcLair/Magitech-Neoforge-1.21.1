package net.stln.magitech.content.block.block_entity;

import net.minecraft.world.Container;
import net.neoforged.neoforge.items.ItemStackHandler;

public interface IPedestalBlockEntity extends Container {

    void clearContents();

    int getTickCounter();

    ItemStackHandler getInventory();
}
