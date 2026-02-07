package net.stln.magitech.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.util.LongContainerData;
import net.stln.magitech.util.SimpleLongContainerData;

public abstract class ManaContainerMenu extends AbstractContainerMenu {
    private final Level level;
    private final ContainerLevelAccess access;
    private final LongContainerData data;
    protected boolean hasProduction = false;
    protected boolean hasConsumption = false;
    protected int inventoryOffsetY = 117;

    public ManaContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInventory) {
        this(menuType, containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleLongContainerData(6));
    }

    public ManaContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData) {
        this(menuType, containerId, playerInventory, access, containerData, 117);
    }

    public ManaContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, final ContainerLevelAccess access, LongContainerData containerData, int inventoryOffsetY) {
        super(menuType, containerId);
        this.access = access;
        this.level = playerInventory.player.level();
        this.data = containerData;
        this.inventoryOffsetY = inventoryOffsetY;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, inventoryOffsetY + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18,  inventoryOffsetY + 58));
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

    public long getProductionRate() {
        return this.data.getLong(4);
    }

    public long getConsumptionRate() {
        return this.data.getLong(5);
    }

    public double getManaRatio() {
        return ((double) this.data.getLong(0)) / this.data.getLong(1);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    protected abstract Block getBlock();

    public boolean hasProduction() {
        return hasProduction;
    }

    public boolean hasConsumption() {
        return hasConsumption;
    }
}
