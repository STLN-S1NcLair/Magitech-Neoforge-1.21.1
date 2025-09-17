package net.stln.magitech.gui;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemTagKeys;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.recipe.MultiStackRecipeInput;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.ToolMaterialRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ToolRepairingMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 10;
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_SLOT_START = 37;
    private static final int USE_ROW_SLOT_END = 46;
    private final ResultContainer resultSlots = new ResultContainer();    private final Container inputSlots = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            ToolRepairingMenu.this.slotsChanged(this);
        }
    };
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
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            @Override
            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
                ToolRepairingMenu.this.resultSlots.awardUsedRecipes(p_150672_, this.getRelevantItems());
                removeCount();
                super.onTake(p_150672_, p_150673_);
                ToolRepairingMenu.this.slotsChanged(inputSlots);
            }

            private List<ItemStack> getRelevantItems() {
                return createRecipeInput(inputSlots).stacks();
            }

            ;
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
            ItemStack itemstack = craftinginput.getItem(0).copy();
            SingleRecipeInput input = new SingleRecipeInput(craftinginput.getItem(1));
            Optional<RecipeHolder<ToolMaterialRecipe>> optional = level.getServer()
                    .getRecipeManager()
                    .getRecipeFor(RecipeInit.TOOL_MATERIAL_TYPE.get(), input, level);
            boolean isRepairable = false;
            if (!itemstack.isEmpty() && optional.isPresent() && craftinginput.getItem(2).getTags().anyMatch(Predicate.isEqual(ItemTagKeys.REPAIR_COMPONENT))) {
                RecipeHolder<ToolMaterialRecipe> recipeholder = optional.get();
                ToolMaterialRecipe craftingrecipe = recipeholder.value();
                if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
                    ResourceLocation material = craftingrecipe.getResultId();
                    if (itemstack.get(ComponentInit.PART_MATERIAL_COMPONENT.get()).getMaterials().contains(ToolMaterialRegister.getMaterial(material))) {
                        for (int i = 0; i < Math.min(craftinginput.getItem(1).getCount(), craftinginput.getItem(2).getCount()); i++) {
                            if (itemstack.getDamageValue() > 0) {
                                itemstack.setDamageValue(itemstack.getDamageValue() - itemstack.getMaxDamage() / 5);
                                ((PartToolItem) itemstack.getItem()).callTestRepair(level, player, itemstack.getMaxDamage() / 5, itemstack);
                                ((PartToolItem) itemstack.getItem()).reloadComponent(player, level, itemstack);
                                isRepairable = true;
                            }
                        }
                    }
                }
            }
            if (!isRepairable) {
                itemstack = ItemStack.EMPTY;
            }

            resultSlots.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }

    private static MultiStackRecipeInput createRecipeInput(Container container) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            stacks.add(container.getItem(i));
        }
        return new MultiStackRecipeInput(stacks);
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
        return stillValid(this.access, player, BlockInit.REPAIRING_WORKBENCH.get());
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

    private boolean moveToSlots(ItemStack itemstack1) {
        SingleRecipeInput craftinginput = new SingleRecipeInput(inputSlots.getItem(1));
        List<RecipeHolder<ToolMaterialRecipe>> optional = level
                .getRecipeManager()
                .getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get());

        if (itemstack1.getItem() instanceof PartToolItem) {
            return this.moveItemStackTo(itemstack1, 1, 2, false);
        } else if (optional.stream().anyMatch(recipe -> Arrays.stream(recipe.value().getIngredients().get(0).getItems()).anyMatch(stack -> stack.is(itemstack1.getItem())))) {
            return this.moveItemStackTo(itemstack1, 2, 3, false);
        } else if (itemstack1.getTags().anyMatch(Predicate.isEqual(ItemTagKeys.REPAIR_COMPONENT))) {
            return this.moveItemStackTo(itemstack1, 3, 4, false);
        }
        return false;
    }

    @Override
    public MenuType<?> getType() {
        return GuiInit.TOOL_REPAIRING_MENU.get();
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }


}