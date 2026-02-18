package net.stln.magitech.gui;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.input.MultiStackRecipeInput;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolAssemblyMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 10;
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_SLOT_START = 37;
    private static final int USE_ROW_SLOT_END = 46;
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;
    private boolean placingRecipe;

    public ToolAssemblyMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ToolAssemblyMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(GuiInit.TOOL_ASSEMBLY_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new Slot(this.resultSlots, 0, 134, 49) {
            @Override
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            @Override
            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
                ToolAssemblyMenu.this.resultSlots.awardUsedRecipes(p_150672_, this.getRelevantItems());
                removeCount();
                super.onTake(p_150672_, p_150673_);
                ToolAssemblyMenu.this.slotsChanged(inputSlots);
            }

            private List<ItemStack> getRelevantItems() {
                return createRecipeInput(inputSlots).stacks();
            }

            ;
        });

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.inputSlots, j + i * 3, 20 + j * 18, 40 + i * 18));
            }
        }

        for (int k = 0; k < 3; k++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 117 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 175));
        }
    }

    protected static void slotChangedCraftingGrid(
            AbstractContainerMenu menu,
            Level level,
            Player player,
            Container craftSlots,
            ResultContainer resultSlots,
            @Nullable RecipeHolder<ToolAssemblyRecipe> recipe
    ) {
        if (!level.isClientSide) {
            MultiStackRecipeInput craftinginput = createRecipeInput(craftSlots);
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<ToolAssemblyRecipe>> optional = level.getServer()
                    .getRecipeManager()
                    .getRecipeFor(RecipeInit.TOOL_ASSEMBLY_TYPE.get(), craftinginput, level);
            if (optional.isPresent()) {
                RecipeHolder<ToolAssemblyRecipe> recipeholder = optional.get();
                ToolAssemblyRecipe craftingrecipe = recipeholder.value();
                if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
                    ItemStack itemstack1 = craftingrecipe.assemble(craftinginput, level.registryAccess());
                    if (itemstack1.isItemEnabled(level.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }

            resultSlots.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }    private final Container inputSlots = new SimpleContainer(6) {
        @Override
        public void setChanged() {
            super.setChanged();
            ToolAssemblyMenu.this.slotsChanged(this);
        }
    };

    private static MultiStackRecipeInput createRecipeInput(Container container) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                stacks.add(container.getItem(i));
            }
        }
        return new MultiStackRecipeInput(stacks);
    }

    public ResultContainer getResultSlots() {
        return resultSlots;
    }

    private void removeCount() {
        for (int i = 0; i < inputSlots.getContainerSize(); i++) {
            inputSlots.removeItem(i, 1);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void slotsChanged(Container inventory) {
        if (!this.placingRecipe) {
            this.access.execute((p_344363_, p_344364_) -> slotChangedCraftingGrid(this, p_344363_, this.player, this.inputSlots, this.resultSlots, null));
        }
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player, this.inputSlots));
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockInit.ASSEMBLY_WORKBENCH.get());
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
            if (index == 0) {
                this.access.execute((p_39378_, p_39379_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player));
                if (!this.moveItemStackTo(itemstack1, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 7 && index < 43) {
                if (!this.moveItemStackTo(itemstack1, 1, 7, false)) {
                    if (index < 34) {
                        if (!this.moveItemStackTo(itemstack1, 34, 43, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 7, 34, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 7, 43, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.TOOL_ASSEMBLY_MENU.get();
    }

    /**
     * Called child determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }




}
