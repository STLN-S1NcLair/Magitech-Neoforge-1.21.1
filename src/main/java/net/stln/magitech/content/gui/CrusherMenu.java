package net.stln.magitech.content.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class CrusherMenu extends ManaContainerMenu {
    final Slot inputSlot;
    final Slot outputSlot;

    public CrusherMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(7));
    }

    public CrusherMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        this(GuiInit.CRUSHER_MENU.get(), containerId, playerInventory, container, access, containerData);
    }

    public CrusherMenu(MenuType<? extends CrusherMenu> menuType, int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access, LongContainerData containerData) {
        super(menuType, containerId, playerInventory, access, containerData, 117);
        this.hasConsumption = true;
        this.inputSlot = this.addSlot(new Slot(container, CrusherBlockEntity.INPUT, 43, 60));
        this.outputSlot = this.addSlot(new Slot(container, CrusherBlockEntity.OUTPUT, 117, 60) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }

    @Override
    protected Block getBlock() {
        return BlockInit.CRUSHER.get();
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            // 先頭36スロットがプレイヤー、末尾2スロットが装置。
            if (index < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
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
        return (int) this.data.getLong(6);
    }
}
