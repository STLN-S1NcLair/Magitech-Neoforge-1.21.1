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
import net.stln.magitech.content.block.block_entity.EmberSmelterBlockEntity;
import net.stln.magitech.helper.LongContainerData;
import net.stln.magitech.helper.SimpleLongContainerData;

public class EmberSmelterMenu extends ManaContainerMenu {
    final Slot inputSlot;
    final Slot outputSlot;
    final Slot fuelSlot;

    public EmberSmelterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public EmberSmelterMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        this(GuiInit.EMBER_SMELTER_MENU.get(), containerId, playerInventory, container, access, containerData);
    }

    public EmberSmelterMenu(MenuType<? extends EmberSmelterMenu> menuType, int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access, LongContainerData containerData) {
        super(menuType, containerId, playerInventory, access, containerData, 117);
        this.hasProduction = true;
        // moveItemStackTo は追加順で探索するため、燃料スロットを先に登録して優先投入させる
        this.fuelSlot = this.addSlot(new Slot(container, EmberSmelterBlockEntity.FUEL, 11, 60) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EmberSmelterBlockEntity.isFuel(stack);
            }
        });
        this.inputSlot = this.addSlot(new Slot(container, EmberSmelterBlockEntity.INPUT, 43, 60));
        this.outputSlot = this.addSlot(new Slot(container, EmberSmelterBlockEntity.OUTPUT, 117, 60) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }

    @Override
    protected Block getBlock() {
        return BlockInit.EMBER_SMELTER.get();
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
            // 先頭36スロットがプレイヤー、末尾3スロットが装置。
            if (index < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 39, false)) {
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

    public int getFuel() {
        return (int) this.data.getLong(4);
    }

    public int getProgress() {
        return (int) this.data.getLong(5);
    }
}
