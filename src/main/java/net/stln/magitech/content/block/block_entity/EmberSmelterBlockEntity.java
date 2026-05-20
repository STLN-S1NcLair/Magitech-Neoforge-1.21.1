package net.stln.magitech.content.block.block_entity;

import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.BlockStatePropertyInit;
import net.stln.magitech.content.block.ManaVesselBlock;
import net.stln.magitech.content.block.ZardiusCrucibleBlock;
import net.stln.magitech.content.gui.InfuserMenu;
import net.stln.magitech.content.gui.ManaVesselMenu;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.helper.LongContainerData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class EmberSmelterBlockEntity extends ManaMachineBlockEntity {
    public static final int FUEL = 0;
    public static final int INPUT = 1;
    public static final int OUTPUT = 2;
    private static final int MAX_FUEL = 8;

    private int fuel = 0;
    // ItemStackHandlerの変更を監視してサーバ側で同期を取る
    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            EmberSmelterBlockEntity.this.onInventoryChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == FUEL) {
                return stack.is(ItemInit.EMBER_CRYSTAL);
            }
            return super.isItemValid(slot, stack);
        }

    };

    public EmberSmelterBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_VESSEL_ENTITY.get(), pos, blockState, mana);
        dataAccess = new LongContainerData() {

            @Override
            public long getLong(int index) {
                return switch (index) {
                    case 0 -> EmberSmelterBlockEntity.this.getMana();
                    case 1 -> EmberSmelterBlockEntity.this.getMaxMana();
                    case 2 -> EmberSmelterBlockEntity.this.getFlowRate();
                    case 3 -> EmberSmelterBlockEntity.this.getMaxFlow();
                    case 4 -> EmberSmelterBlockEntity.this.getFuel();
                    default -> 0;
                };
            }

            @Override
            public void setLong(int index, long value) {
                if (index == 0) {
                    EmberSmelterBlockEntity.this.mana = Math.clamp(value, 0, EmberSmelterBlockEntity.this.maxMana);
                }
            }

            @Override
            public int getLongCount() {
                return 4;
            }
        };
    }

    public EmberSmelterBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        ItemStack fuelItem = inventory.getStackInSlot(FUEL);
        if (fuelItem.is(ItemInit.EMBER_CRYSTAL) && fuel == 0) {
            fuelItem.shrink(1);
            fuel = MAX_FUEL;
        }
        if (fuel > 0) {
            ItemStack input = inventory.getStackInSlot(INPUT);
            Optional<RecipeHolder<SmeltingRecipe>> holder = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level);
            if (holder.isPresent()) {
                SmeltingRecipe recipe = holder.get().value();
                ItemStack result = recipe.getResultItem(level.registryAccess());
                if (ItemStack.isSameItemSameComponents(result, inventory.getStackInSlot(OUTPUT))) {
                    if (inventory.getStackInSlot(OUTPUT).getCount() + result.getCount() <= result.getMaxStackSize()) {
                        inventory.extractItem(INPUT, 1, false);
                        inventory.insertItem(OUTPUT, result.copy(), false);
                        fuel--;
                        getManaHandler(null).produceMana((long) (recipe.getExperience() * 10000)); // 経験値をマナに変換して生成
                    }
                } else if (inventory.getStackInSlot(OUTPUT).isEmpty()) {
                    inventory.extractItem(INPUT, 1, false);
                    inventory.insertItem(OUTPUT, result.copy(), false);
                    fuel--;
                }
            }
            if (!state.getValue(BlockStatePropertyInit.ACTIVE)) {
                level.setBlock(pos, state.setValue(BlockStatePropertyInit.ACTIVE, true), 3);
                setChanged();
            }
        } else {
            if (state.getValue(BlockStatePropertyInit.ACTIVE)) {
                level.setBlock(pos, state.setValue(BlockStatePropertyInit.ACTIVE, false), 3);
                setChanged();
            }
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, inv);
        }
    }

    // Container メソッド実装 - ItemStackHandler へ直接委譲して、GUI側の操作を実体に反映
    private void onInventoryChanged() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.extractItem(index, count, false);
        if (!stack.isEmpty()) {
            this.onInventoryChanged();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        this.onInventoryChanged();
        return stack;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.set(i, inventory.getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, items.get(i));
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack.copyWithCount(Math.min(stack.getCount(), this.getMaxStackSize())));
        this.onInventoryChanged();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.onInventoryChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("fuel", fuel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        fuel = tag.getInt("fuel");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaVesselMenu(containerId, inventory, this, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.ember_smelter");
    }

    public int getFuel() {
        return fuel;
    }


    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side.getAxis() == state.getValue(ManaVesselBlock.AXIS)) {
            return ManaFlowRule.bothWays(1.0F);
        }
        return ManaFlowRule.none();
    }
}
