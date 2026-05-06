package net.stln.magitech.content.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class CreativeManaSinkMenu extends ManaVesselMenu {

    public CreativeManaSinkMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public CreativeManaSinkMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.CREATIVE_MANA_SINK_MENU.get(), containerId, playerInventory, container, access, containerData);
    }

    @Override
    protected Block getBlock() {
        return BlockInit.CREATIVE_MANA_SINK.get();
    }
}
