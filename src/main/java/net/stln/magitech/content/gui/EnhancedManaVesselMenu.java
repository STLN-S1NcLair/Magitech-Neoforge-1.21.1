package net.stln.magitech.content.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class EnhancedManaVesselMenu extends ManaVesselMenu {

    public EnhancedManaVesselMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public EnhancedManaVesselMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.ENHANCED_MANA_VESSEL_MENU.get(), containerId, playerInventory, container, access, containerData);
    }

    @Override
    protected Block getBlock() {
        return BlockInit.ENHANCED_MANA_VESSEL.get();
    }
}
