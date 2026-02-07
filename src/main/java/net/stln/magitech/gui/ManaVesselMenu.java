package net.stln.magitech.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public class ManaVesselMenu extends ManaContainerMenu {
    final Slot receiveSlot;
    final Slot extractSlot;
    private final Container container;

    public ManaVesselMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public ManaVesselMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.MANA_VESSEL_MENU.get(), containerId, playerInventory, access, containerData);
        this.container = container;
        this.receiveSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.INPUT, 149, 51));
        this.extractSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.OUTPUT, 149, 69));
    }

    @Override
    protected Block getBlock() {
        return BlockInit.MANA_VESSEL.get();
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.MANA_VESSEL_MENU.get();
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 29) {
                if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }
}
