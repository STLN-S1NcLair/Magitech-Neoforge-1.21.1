package net.stln.magitech.gui;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.ThreadPageItem;
import net.stln.magitech.item.ThreadBoundItem;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.magic.spell.Spell;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ThreadboudMenuType extends AbstractContainerMenu {
    private final Player player;
    private ItemStack threadbound;
    private SimpleContainer container = new SimpleContainer(15);
    private int containerRows = 3;
    private int containerColumns = 5;

    public ThreadboudMenuType(int containerId, Inventory playerInv) {
        this(containerId, playerInv, playerInv.player, playerInv.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadBoundItem ? playerInv.player.getItemInHand(InteractionHand.MAIN_HAND) : playerInv.player.getItemInHand(InteractionHand.OFF_HAND));
    }

    public ThreadboudMenuType(int containerId, Inventory playerInv, Player player, ItemStack threadbound) {
        super(GuiInit.THREADBOUND_MENU.get(), containerId);
        this.player = player;

        if (!(threadbound.getItem() instanceof ThreadBoundItem) && CuriosApi.getCuriosInventory(player).isPresent()) {
            threadbound = CuriosApi.getCuriosInventory(player).get().getCurios().get("threadbound").getStacks().getStackInSlot(0);
        }
        this.threadbound = threadbound;

        for (int i = 0; i < 15; i++) {
            this.addSlot(new Slot(container, i, 44 + 18 * (i % 5), 27 + 18 * (i / 5)) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ThreadPageItem;
                }

                @Override
                public void set(ItemStack stack) {
                    this.setChanged();
                    super.set(stack);
                }
            });
        }

        addInventory(playerInv);
        addHotbar(playerInv);

        List<Spell> spells = threadbound.get(ComponentInit.SPELL_COMPONENT).spells();
        for (int i = 0; i < Math.min(spells.size(), container.getContainerSize()); i++) {
            ItemStack stack = new ItemStack(ItemInit.THREAD_PAGE.get());
            stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(spells.get(i)));
            container.setItem(i, stack);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
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
    public boolean stillValid(Player player) {
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        return player.getInventory().contains(threadbound) || !curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0).isEmpty();
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
    public void removed(Player player) {
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
            if (!container.getItem(i).isEmpty() && container.getItem(i).getItem() instanceof ThreadPageItem pageItem && container.getItem(i).has(ComponentInit.THREAD_PAGE_COMPONENT)) {
                spells.add(container.getItem(i).get(ComponentInit.THREAD_PAGE_COMPONENT).spell());
            }
        }
        threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spells, 0));
    }
}