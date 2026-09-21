package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PedestalBlockEntity extends BlockEntity implements IPedestalBlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public int tickCounter = 0;

    public PedestalBlockEntity(BlockEntityType<? extends PedestalBlockEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity pedestal) {
        tickCounter++;
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity pedestal) {

    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.set(i, inventory.getStackInSlot(i));
        }
        return stacks;
    }

    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, items.get(i));
        }
    }

    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.getItems().set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public int getTickCounter() {
        return tickCounter;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }
}
