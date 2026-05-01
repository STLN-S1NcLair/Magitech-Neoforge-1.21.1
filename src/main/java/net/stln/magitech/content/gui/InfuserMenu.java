package net.stln.magitech.content.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class InfuserMenu extends InfusionAltarMenu {

    public InfuserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public InfuserMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.INFUSER_MENU.get(), containerId, playerInventory, access, containerData);
        this.hasConsumption = true;
    }

    @Override
    protected Block getBlock() {
        return BlockInit.INFUSER.get();
    }
}
