package net.stln.magitech.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public class ManaStranderMenu extends ManaContainerMenu {

    public ManaStranderMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public ManaStranderMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.MANA_STRANDER_MENU.get(), containerId, playerInventory, access, containerData);
        this.hasConsumption = true;
    }

    @Override
    protected Block getBlock() {
        return BlockInit.MANA_STRANDER.get();
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.MANA_STRANDER_MENU.get();
    }
}
