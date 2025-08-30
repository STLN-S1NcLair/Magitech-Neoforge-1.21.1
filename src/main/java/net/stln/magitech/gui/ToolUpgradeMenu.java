package net.stln.magitech.gui;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.UpgradeComponent;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.item.tool.upgrade.Upgrade;
import net.stln.magitech.item.tool.upgrade.UpgradeInstance;
import net.stln.magitech.item.tool.upgrade.UpgradeUtil;
import net.stln.magitech.util.ToolMaterialUtil;

import java.util.List;
import java.util.Random;

public class ToolUpgradeMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;
    private final ContainerLevelAccess access;
    private final Level level;
    /**
     * Stores the game time of the last time the player took items from the the crafting result slot. This is used to prevent the sound from being played multiple times on the same tick.
     */
    List<Upgrade> upgrades = List.of();
    int upgradeSize = 3;
    long lastSoundTime;
    Runnable slotUpdateListener = () -> {
    };
    /**
     * The {@linkplain ItemStack} set in the input slot by the player.
     */
    private ItemStack input = ItemStack.EMPTY;
    public final Container container = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            ToolUpgradeMenu.this.slotsChanged(this);
            ToolUpgradeMenu.this.slotUpdateListener.run();
        }
    };

    public ToolUpgradeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ToolUpgradeMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(GuiInit.TOOL_UPGRADE_MENU.get(), containerId);
        this.access = access;
        this.level = playerInventory.player.level();
        this.addSlot(new Slot(this.container, 0, 20, 69));
        this.addSlot(new Slot(this.container, 1, 20, 30));
//        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 49) {
//            @Override
//            public boolean mayPlace(ItemStack p_40362_) {
//                return false;
//            }
//
//            @Override
//            public void onTake(Player p_150672_, ItemStack p_150673_) {
//                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
//                ToolUpgradeMenu.this.resultContainer.awardUsedRecipes(p_150672_, this.getRelevantItems());
//                ItemStack itemstack = removeCount();
//                if (!itemstack.isEmpty()) {
//                    ToolUpgradeMenu.this.setupResultSlot();
//                }
//
//                access.execute((p_40364_, p_40365_) -> {
//                    long l = p_40364_.getGameTime();
//                    if (ToolUpgradeMenu.this.lastSoundTime != l) {
//                        ToolUpgradeMenu.this.lastSoundTime = l;
//                    }
//                });
//                super.onTake(p_150672_, p_150673_);
//            }
//
//            private List<ItemStack> getRelevantItems() {
//                return List.of(ToolUpgradeMenu.this.container.getItem());
//            }
//        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 117 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 175));
        }

    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockInit.UPGRADE_WORKBENCH.get());
    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting. Name is for legacy.
     */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (isValidUpgrade(id)) {
            ItemStack stack = container.getItem(0);
            if (!stack.has(ComponentInit.UPGRADE_COMPONENT)) {
                stack.set(ComponentInit.UPGRADE_COMPONENT, new UpgradeComponent(List.of()));
            }
            stack.set(ComponentInit.UPGRADE_COMPONENT, stack.get(ComponentInit.UPGRADE_COMPONENT).addUpgrade(new UpgradeInstance(1, upgrades.get(id))));
            stack.set(ComponentInit.UPGRADE_SEED_COMPONENT, new Random().nextInt(Integer.MAX_VALUE));
            stack.set(ComponentInit.UPGRADE_POINT_COMPONENT, stack.get(ComponentInit.UPGRADE_POINT_COMPONENT) - 1);
            this.container.setItem(0, stack);
            ItemStack material = this.container.getItem(1).copy();
            material.shrink(1);
            this.container.setItem(1, material);
            player.level().playSound(player, player, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    private boolean isValidUpgrade(int upgradeIndex) {
        return upgradeIndex >= 0 && upgradeIndex < this.upgradeSize && canUpgrade();
    }

    public boolean canUpgrade() {
        ItemStack itemStack = container.getItem(0);
        return hasUpgradePoint(itemStack) && isCorrectMaterialForUpgrade(itemStack);
    }

    public boolean isCorrectMaterialForUpgrade(ItemStack itemStack) {
        return ToolMaterialUtil.isCorrectMaterialForUpgrade(itemStack.get(ComponentInit.TIER_COMPONENT), itemStack.get(ComponentInit.UPGRADE_POINT_COMPONENT), container.getItem(1).getItem());
    }

    public boolean hasUpgradePoint(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getItem() instanceof PartToolItem && itemStack.get(ComponentInit.UPGRADE_POINT_COMPONENT) > 0;
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void slotsChanged(Container inventory) {
        ItemStack itemstack = this.container.getItem(0);
        this.input = itemstack.copy();
        this.setupUpgrade(inventory, itemstack);
    }

    private void setupUpgrade(Container container, ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof PartToolItem)) {
            this.upgrades = List.of();
            return;
        }
        if (!stack.has(ComponentInit.UPGRADE_SEED_COMPONENT)) {
            stack.set(ComponentInit.UPGRADE_SEED_COMPONENT, new Random().nextInt(Integer.MAX_VALUE));
        }
        upgrades = UpgradeUtil.getUpgrades(upgradeSize, stack.get(ComponentInit.UPGRADE_SEED_COMPONENT), stack);
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.TOOL_UPGRADE_MENU.get();
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return super.canTakeItemForPickAll(stack, slot);
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
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 1) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() instanceof PartToolItem) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                ItemStack itemStack = this.container.getItem(0);
                if (this.hasUpgradePoint(itemStack) && ToolMaterialUtil.isCorrectMaterialForUpgrade(itemStack.get(ComponentInit.TIER_COMPONENT), itemStack.get(ComponentInit.UPGRADE_POINT_COMPONENT), itemstack1.getItem())) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
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
        this.access.execute((p_40313_, p_40314_) -> this.clearContainer(player, this.container));
    }
}
