package net.stln.magitech.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public class ManaVesselMenu extends AbstractContainerMenu {
    final Slot receiveSlot;
    final Slot extractSlot;
    private final Level level;
    private final Container container;
    private final ContainerLevelAccess access;
    private final LongContainerData data;

    public ManaVesselMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL, new SimpleLongContainerData(4));
    }

    public ManaVesselMenu(int containerId, Inventory playerInventory, Container container, final ContainerLevelAccess access, LongContainerData containerData) {
        super(GuiInit.MANA_VESSEL_MENU.get(), containerId);
        this.access = access;
        this.level = playerInventory.player.level();
        this.container = container;
        this.receiveSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.RECEIVE_SLOT, 149, 51));
        this.extractSlot = this.addSlot(new Slot(container, ManaVesselBlockEntity.EXTRACT_SLOT, 149, 69));
        this.data = containerData;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 117 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 175));
        }
        this.addDataSlots(data);
    }

    @Override
    public void slotsChanged(Container container) {
        container.setChanged();
        super.slotsChanged(container);
    }

    public long getMana() {
        return this.data.getLong(0);
    }

    public long getMaxMana() {
        return this.data.getLong(1);
    }

    public long getFlowRate() {
        return this.data.getLong(2);
    }

    public long getMaxManaFlow() {
        return this.data.getLong(3);
    }

    public double getManaRatio() {
        return ((double) this.data.getLong(0)) / this.data.getLong(1);
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockInit.MANA_VESSEL.get());
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

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
//        this.access.execute((p_40313_, p_40314_) -> this.clearContainer(player, this.container));
    }
}
