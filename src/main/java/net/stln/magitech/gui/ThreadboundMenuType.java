package net.stln.magitech.gui;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.ThreadBoundItem;
import net.stln.magitech.item.ThreadPageItem;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.CuriosHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThreadboundMenuType extends AbstractContainerMenu {
    private final ItemStack threadbound;
    private final SimpleContainer container = new SimpleContainer(15);
    private final int containerRows = 3;
    private final int containerColumns = 5;

    public ThreadboundMenuType(int containerId, Inventory playerInv) {
        this(containerId, playerInv, playerInv.player, playerInv.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadBoundItem ? playerInv.player.getItemInHand(InteractionHand.MAIN_HAND) : playerInv.player.getItemInHand(InteractionHand.OFF_HAND));
    }

    public ThreadboundMenuType(int containerId, Inventory playerInv, Player player, ItemStack threadbound) {
        super(GuiInit.THREADBOUND_MENU.get(), containerId);

        if (!(threadbound.getItem() instanceof ThreadBoundItem)) {
            threadbound = CuriosHelper.getThreadBoundStack(player).orElse(ItemStack.EMPTY);
        }
        this.threadbound = threadbound;

        for (int i = 0; i < 15; i++) {
            this.addSlot(new Slot(container, i, 44 + 18 * (i % 5), 27 + 18 * (i / 5)) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getItem() instanceof ThreadPageItem;
                }

                @Override
                public void set(@NotNull ItemStack stack) {
                    this.setChanged();
                    super.set(stack);
                }
            });
        }

        addInventory(playerInv);
        addHotbar(playerInv);
        
        List<Spell> spells = ComponentHelper.getSpells(threadbound).spells();
        for (int i = 0; i < Math.min(spells.size(), container.getContainerSize()); i++) {
            ItemStack stack = new ItemStack(ItemInit.THREAD_PAGE.get());
            stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(spells.get(i)));
            container.setItem(i, stack);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.containerRows * containerColumns) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * containerColumns, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * containerColumns, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return CuriosHelper.getThreadBoundStack(player).isPresent();
    }

    private void addInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 112 + i * 18));
            }
        }
    }

    private void addHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        updateComponent();
    }

    @Override
    public void removed(@NotNull Player player) {
        // プレイヤーが閉じた時にカーソルにあるアイテムをドロップさせる
        ItemStack carried = this.getCarried();
        if (!carried.isEmpty() && !player.level().isClientSide) {
            player.drop(carried.copy(), false);
            this.setCarried(ItemStack.EMPTY);
        }

        super.removed(player);
    }

    public void updateComponent() {
        List<Spell> spells = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ComponentHelper.getThreadPageSpell(container.getItem(i)).ifPresent(spells::add);
        }
        threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spells));
    }
}
