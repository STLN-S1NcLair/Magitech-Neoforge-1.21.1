package net.stln.magitech.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public class InfuserMenu extends ManaContainerMenu {
    final Slot inputSlot;
    final Slot outputSlot;
    private final Container container;
    private ContainerData machineData;

    public InfuserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(6), new SimpleContainerData(2));
    }

    public InfuserMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData manaContainerData, ContainerData machineData) {
        super(GuiInit.INFUSER_MENU.get(), containerId, playerInventory, access, manaContainerData, 171);
        this.hasConsumption = true;
        this.machineData = machineData;
        this.container = container;
        this.inputSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.INPUT, 44, 105));
        this.outputSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.OUTPUT, 116, 105) {

                                           @Override
                                           public boolean mayPlace(ItemStack stack) {
                                               return false;
                                           }
                                       }
        );
        this.addDataSlots(machineData);
    }

    @Override
    protected Block getBlock() {
        return BlockInit.INFUSER.get();
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.INFUSER_MENU.get();
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

    public int getProgress() {
        return this.machineData.get(0);
    }

    public int getMaxProgress() {
        return this.machineData.get(1);
    }

    public float getProgressRatio() {
        return getMaxProgress() == 0 ? 0 : (float) getProgress() / getMaxProgress();
    }
}
