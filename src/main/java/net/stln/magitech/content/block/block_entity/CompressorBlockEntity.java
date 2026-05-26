package net.stln.magitech.content.block.block_entity;

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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.BlockStatePropertyInit;
import net.stln.magitech.content.block.CompressorBlock;
import net.stln.magitech.content.gui.CompressorMenu;
import net.stln.magitech.content.recipe.CompressingRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.LongContainerData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class CompressorBlockEntity extends ManaMachineBlockEntity implements GeoBlockEntity {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int MAX_PROGRESS = 100;
    public static final long MANA_PER_TICK = 500;
    private static final int ACTIVE_ANIMATION_TICKS = 100;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation ACTIVE_A = RawAnimation.begin().thenPlay("active_a");
    private static final RawAnimation ACTIVE_B = RawAnimation.begin().thenPlay("active_b");

    protected int progress = 0;
    private int activeAnimationTick = 0;
    private boolean activeAnimationVariant = false;
    private boolean stoppingAnimation = false;
    private boolean wasActiveClient = false;
    // ItemStackHandlerの変更を監視してサーバ側で同期を取る
    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            CompressorBlockEntity.this.onInventoryChanged();
        }

    };

    public CompressorBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.COMPRESSOR_ENTITY.get(), pos, blockState, mana);
        this.dataAccess = new LongContainerData() {

            @Override
            public long getLong(int index) {
                return switch (index) {
                    case 0 -> CompressorBlockEntity.this.getMana();
                    case 1 -> CompressorBlockEntity.this.getMaxMana();
                    case 2 -> CompressorBlockEntity.this.getFlowRate();
                    case 3 -> CompressorBlockEntity.this.getMaxFlow();
                    case 4 -> CompressorBlockEntity.this.getProductionRate();
                    case 5 -> CompressorBlockEntity.this.getConsumptionRate();
                    case 6 -> CompressorBlockEntity.this.getProgress();
                    default -> 0;
                };
            }

            @Override
            public void setLong(int index, long value) {
                if (index == 0) {
                    CompressorBlockEntity.this.mana = Math.clamp(value, 0, CompressorBlockEntity.this.maxMana);
                }
            }

            @Override
            public int getLongCount() {
                return 7;
            }
        };
    }

    public CompressorBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "compressor_controller", 0, (controllerState) -> {
            if (this.level != null) {
                BlockState state = this.level.getBlockState(this.worldPosition);
                if (!(state.getBlock() instanceof CompressorBlock)) return PlayState.STOP;
                if (state.getValue(BlockStatePropertyInit.ACTIVE) || this.stoppingAnimation) {
                    controllerState.setAndContinue(this.activeAnimationVariant ? ACTIVE_B : ACTIVE_A);
                    return PlayState.CONTINUE;
                }
            }
            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        ItemStack input = inventory.getStackInSlot(INPUT);
        Optional<RecipeHolder<CompressingRecipe>> holder = level.getRecipeManager().getRecipeFor(RecipeInit.COMPRESSING_TYPE.get(), new SingleRecipeInput(input), level);
        boolean shouldStop = false;
        if (holder.isPresent()) {
            CompressingRecipe recipe = holder.get().value();
            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (canProgress(result)) {
                if (getMana() >= MANA_PER_TICK) {
                    if (progress >= MAX_PROGRESS) {
                        craft(recipe, result);
                    } else {
                        progress++;
                        getManaHandler(null).consumeMana(MANA_PER_TICK);
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
            } else {
                shouldStop = true;
            }
        } else {
            shouldStop = true;
        }
        if (shouldStop) {
            if (state.getValue(BlockStatePropertyInit.ACTIVE)) {
                level.setBlock(pos, state.setValue(BlockStatePropertyInit.ACTIVE, false), 3);
                this.progress = 0;
                setChanged();
            }
        }
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        super.clientTick(level, pos, state);
        boolean active = state.getValue(BlockStatePropertyInit.ACTIVE);
        if (active) {
            this.stoppingAnimation = false;
            if (!this.wasActiveClient) {
                this.activeAnimationTick = 0;
                this.activeAnimationVariant = !this.activeAnimationVariant;
            }
            this.activeAnimationTick++;
            if (this.activeAnimationTick >= ACTIVE_ANIMATION_TICKS) {
                this.activeAnimationTick = 0;
                this.activeAnimationVariant = !this.activeAnimationVariant;
            }
        } else if (this.wasActiveClient) {
            this.stoppingAnimation = this.activeAnimationTick > 0;
        } else if (this.stoppingAnimation) {
            this.activeAnimationTick++;
            if (this.activeAnimationTick >= ACTIVE_ANIMATION_TICKS) {
                this.activeAnimationTick = 0;
                this.stoppingAnimation = false;
            }
        }
        this.wasActiveClient = active;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return inventory.isItemValid(slot, stack);
    }

    protected boolean canProgress(ItemStack result) {
        if (ItemStack.isSameItemSameComponents(result, inventory.getStackInSlot(OUTPUT))) {
            return inventory.getStackInSlot(OUTPUT).getCount() + result.getCount() <= result.getMaxStackSize();
        } else return inventory.getStackInSlot(OUTPUT).isEmpty();
    }

    private void craft(CompressingRecipe recipe, ItemStack result) {
        inventory.extractItem(INPUT, recipe.getSizedIngredient().count(), false);
        inventory.insertItem(OUTPUT, result.copy(), false);
        progress = 0;
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
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
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
        return new CompressorMenu(containerId, inventory, this, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.compressor");
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side.getAxis() == Direction.Axis.Y) {
            return ManaFlowRule.bothWays(-1.0F);
        }
        return ManaFlowRule.none();
    }
}
