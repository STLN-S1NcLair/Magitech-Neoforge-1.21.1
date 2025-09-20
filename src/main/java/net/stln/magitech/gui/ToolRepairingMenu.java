package net.stln.magitech.gui;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemTagKeys;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import net.stln.magitech.recipe.input.MultiStackRecipeInput;
import net.stln.magitech.util.ComponentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ToolRepairingMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 10;
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_SLOT_START = 37;
    private static final int USE_ROW_SLOT_END = 46;
    private final Container inputSlots = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            ToolRepairingMenu.this.slotsChanged(this);
        }
    };
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;
    private final Level level;
    private boolean placingRecipe;

    public ToolRepairingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ToolRepairingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(GuiInit.TOOL_REPAIRING_MENU.get(), containerId);
        this.level = playerInventory.player.level();
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new Slot(this.resultSlots, 0, 134, 49) {
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
            stack.onCraftedBy(player.level(), player, stack.getCount());
            ToolRepairingMenu.this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());
            removeCount();
            super.onTake(player, stack);
            ToolRepairingMenu.this.slotsChanged(inputSlots);
        }

        private List<ItemStack> getRelevantItems() {
            return createRecipeInput(inputSlots).stacks();
        };
        });
            for (int i = 0; i < 3; i++) {
                this.addSlot(new Slot(this.inputSlots, i, 20 + i * 18, 49));
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

    private void removeCount() {
        int repairCount = 0;
        ItemStack stack = inputSlots.getItem(0).copy();
        for (int i = 0; i < Math.min(inputSlots.getItem(1).getCount(), inputSlots.getItem(2).getCount()); i++) {
            if (stack.getDamageValue() > 0) {
                stack.setDamageValue(stack.getDamageValue() - stack.getMaxDamage() / 5);
                ((PartToolItem) stack.getItem()).callOnRepair(level, player, stack.getMaxDamage() / 5, stack);
                repairCount++;
            }
        }
        inputSlots.removeItem(0, 1);
        inputSlots.removeItem(1, repairCount);
        inputSlots.removeItem(2, repairCount);
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, Container craftSlots, ResultContainer resultSlots) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MultiStackRecipeInput recipeInput = createRecipeInput(craftSlots);
            ItemStack stack = recipeInput.getItem(0).copy();
            SingleRecipeInput input = new SingleRecipeInput(recipeInput.getItem(1));
            AtomicBoolean isRepairable = new AtomicBoolean(false);
            ItemStack stack1 = stack;
            level.getRecipeManager()
                    .getRecipeFor(RecipeInit.TOOL_MATERIAL_TYPE.get(), input, level)
                    .ifPresent(holder -> {
                        if (!recipeInput.getItem(2).is(ItemTagKeys.REPAIR_COMPONENT)) {
                            var recipe = holder.value();
                            if (resultSlots.setRecipeUsed(level, serverPlayer, holder)) {
                                ToolMaterial toolMaterial = recipe.getToolMaterial();
                                if (ComponentHelper.getPartMaterials(stack1).contains(toolMaterial)) {
                                    for (int i = 0; i < Math.min(recipeInput.getItem(1).getCount(), recipeInput.getItem(2).getCount()); i++) {
                                        if (stack1.getDamageValue() > 0 && (stack1.getItem() instanceof PartToolItem partToolItem)) {
                                            stack1.setDamageValue(stack1.getDamageValue() - stack1.getMaxDamage() / 5);
                                            partToolItem.callTestRepair(level, player, stack1.getMaxDamage() / 5, stack1);
                                            partToolItem.reloadComponent(player, level, stack1);
                                            isRepairable.set(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
            if (!isRepairable.get()) {
                stack = ItemStack.EMPTY;
            }
            resultSlots.setItem(0, stack);
            menu.setRemoteSlot(0, stack);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, stack));
        }
    }

    private static MultiStackRecipeInput createRecipeInput(Container container) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            stacks.add(container.getItem(i));
        }
        return new MultiStackRecipeInput(stacks);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void slotsChanged(@NotNull Container inventory) {
        if (!this.placingRecipe) {
            this.access.execute((p_344363_, p_344364_) -> slotChangedCraftingGrid(this, p_344363_, this.player, this.inputSlots, this.resultSlots));
        }
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player, this.inputSlots));
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, BlockInit.REPAIRING_WORKBENCH.get());
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     */
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_39378_, p_39379_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player));
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 4 && index < 40) {
                if (!moveToSlots(itemstack1)) {
                    if (index < 31) {
                        if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
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

    private boolean moveToSlots(ItemStack itemStack) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(inputSlots.getItem(1));
        List<RecipeHolder<ToolMaterialRecipe>> optional = level
                .getRecipeManager()
                .getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get());

        if (itemStack.getItem() instanceof PartToolItem) {
            return this.moveItemStackTo(itemStack, 1, 2, false);
        } else if (optional.stream().anyMatch(recipe -> Arrays.stream(recipe.value().getIngredients().get(0).getItems()).anyMatch(stack -> stack.is(itemStack.getItem())))) {
            return this.moveItemStackTo(itemStack, 2, 3, false);
        } else if (itemStack.getTags().anyMatch(Predicate.isEqual(ItemTagKeys.REPAIR_COMPONENT))) {
            return this.moveItemStackTo(itemStack, 3, 4, false);
        }
        return false;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }
}
