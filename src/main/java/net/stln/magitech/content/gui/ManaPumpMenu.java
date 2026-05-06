package net.stln.magitech.content.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class ManaPumpMenu extends ManaContainerMenu {

    public ManaPumpMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public ManaPumpMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.MANA_PUMP_MENU.get(), containerId, playerInventory, access, containerData);
    }

    @Override
    protected Block getBlock() {
        return BlockInit.MANA_PUMP.get();
    }
}
