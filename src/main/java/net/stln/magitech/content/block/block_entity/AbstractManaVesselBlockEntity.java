package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.ManaVesselBlock;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractManaVesselBlockEntity extends ManaContainerBlockEntity implements GeoBlockEntity {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    // ItemStackHandlerの変更を監視してサーバ側で同期を取る
    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            AbstractManaVesselBlockEntity.this.onInventoryChanged();
        }
    };

    public AbstractManaVesselBlockEntity(BlockEntityType<? extends AbstractManaVesselBlockEntity> type, BlockPos pos, BlockState blockState, long mana) {
        super(type, pos, blockState, mana);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        ItemStack source = inventory.getStackInSlot(INPUT);
        ItemStack sink = inventory.getStackInSlot(OUTPUT);
        IBasicManaHandler sinkHandler = source.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM, null);
        IBasicManaHandler sourceHandler = sink.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM, null);
        IBasicManaHandler handler = this.getManaHandler(null);
        ManaTransferHelper.transferMana(handler, sinkHandler);
        ManaTransferHelper.transferMana(sourceHandler, handler);
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
    public @NotNull ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.extractItem(index, count, false);
        if (!stack.isEmpty()) {
            this.onInventoryChanged();
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        this.onInventoryChanged();
        return stack;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.set(i, inventory.getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
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

    @Override
    protected abstract @NotNull Component getDefaultName();


    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side.getAxis() == state.getValue(ManaVesselBlock.AXIS)) {
            return ManaFlowRule.bothWays(0.0F);
        }
        return ManaFlowRule.none();
    }
}
