package net.stln.magitech.content.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class InfusionAltarMenu extends ManaContainerMenu {

    public InfusionAltarMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public InfusionAltarMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.INFUSION_ALTAR_MENU.get(), containerId, playerInventory, access, containerData);
        this.hasConsumption = true;
    }

    @Override
    protected Block getBlock() {
        return BlockInit.INFUSION_ALTAR.get();
    }
}
