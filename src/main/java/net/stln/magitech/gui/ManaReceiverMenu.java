package net.stln.magitech.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public class ManaReceiverMenu extends ManaContainerMenu {

    public ManaReceiverMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public ManaReceiverMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.MANA_RECEIVER_MENU.get(), containerId, playerInventory, access, containerData);
        this.hasProduction = true;
    }

    @Override
    protected Block getBlock() {
        return BlockInit.MANA_RECEIVER.get();
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.MANA_RECEIVER_MENU.get();
    }
}
