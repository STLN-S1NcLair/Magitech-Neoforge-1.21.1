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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.HeatBurnerBlock;
import net.stln.magitech.content.block.ThermalManaFurnaceBlock;
import net.stln.magitech.content.field_effect.effect.FieldEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.api.machine.inspection.IMachineInspectionTarget;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;
import net.stln.magitech.core.api.field_effect.ColoredFieldEffectType;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.Nullable;

/**
 * 熱式マナ炉の燃料、燃焼時間、マナ生成を管理する BlockEntity です。
 * Block entity that manages fuel, burn time, and mana production for the Thermal Mana Furnace.
 */
public class ThermalManaFurnaceBlockEntity extends ManaMachineBlockEntity implements IItemHandlerBlockEntity, IMachineInspectionTarget {
    public static final int FUEL_SLOT = 0;
    public static final long MANA_PER_TICK = 1000L;

    private int burnTime;
    private int soundTickCounter;
    private int burnDuration;
    private boolean contentsDropped;

    /**
     * かまど燃料だけを受け入れる1スロットインベントリです。
     * A one-slot inventory that accepts furnace fuels only.
     */
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            ThermalManaFurnaceBlockEntity.this.onInventoryChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == FUEL_SLOT && isFuel(stack);
        }
    };

    public ThermalManaFurnaceBlockEntity(BlockPos pos, BlockState state, long mana) {
        super(BlockInit.THERMAL_MANA_FURNACE_ENTITY.get(), pos, state, mana);
    }

    public ThermalManaFurnaceBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 0L);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (!isLowerPart() || level.isClientSide) {
            return;
        }

        if (burnTime <= 0) {
            startBurning(level);

            if (burnTime <= 0) {
                burnDuration = 0;
                updateLitState(false);
            }
        } else {
            updateLitState(true);
            getManaHandler(null).produceMana(MANA_PER_TICK);
            burnTime--;
            if (soundTickCounter % 40 == 0) {
                level.playSound(null, pos, SoundInit.BURNER.get(), SoundSource.BLOCKS, 0.3F, 1.0F);
                soundTickCounter = 0;
            }
            soundTickCounter++;
            setChanged();
        }
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        addVFX(level, pos, state);
    }

    private void addVFX(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(HeatBurnerBlock.LIT)) {
            Vec3 center = pos.above(2).getCenter().add(0, -0.2, 0);
            PointVFX.spray(level, center, Element.EMBER, ElementParticles::smokeParticle, new Vec3(0, 1, 0), 1, 0.15F, 0.05F);
            PointVFX.spraySquare(level, center, Element.MANA, new Vec3(0, 1, 0), 1, 0.05F, 0.05F);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide && isLowerPart()) {
            updateLitState(burnTime > 0);
        }
    }

    private void startBurning(Level level) {
        ItemStack fuel = inventory.getStackInSlot(FUEL_SLOT);
        int duration = getBurnDuration(fuel);
        if (duration <= 0) {
            updateLitState(false);
            return;
        }

        ItemStack remainder = fuel.getCraftingRemainingItem();
        ItemStack remainingFuel = fuel.copyWithCount(fuel.getCount() - 1);
        if (remainingFuel.isEmpty()) {
            inventory.setStackInSlot(FUEL_SLOT, remainder);
        } else {
            inventory.setStackInSlot(FUEL_SLOT, remainingFuel);
            if (!remainder.isEmpty()) {
                Containers.dropItemStack(level, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, remainder);
            }
        }
        burnTime = duration;
        burnDuration = duration;
        setChanged();
    }

    public static boolean isFuel(ItemStack stack) {
        return getBurnDuration(stack) > 0;
    }

    public static int getBurnDuration(ItemStack stack) {
        return stack.getBurnTime(RecipeType.SMELTING);
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getBurnDuration() {
        return burnDuration;
    }

    @Override
    public int getInputSlot() {
        return FUEL_SLOT;
    }

    @Override
    public MachineBlockEntityManaHandler getManaHandler(Direction side) {
        if (!isLowerPart()) {
            return null;
        }
        if (side != null && getManaFlowRule(getBlockState(), side).isNone()) {
            return null;
        }
        return super.getManaHandler(side);
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (state.getValue(ThermalManaFurnaceBlock.PART) == ThermalManaFurnaceBlock.Part.LOWER
                && (side == null || side == state.getValue(ThermalManaFurnaceBlock.FACING))) {
            return ManaFlowRule.extractOnly(1.0F);
        }
        return ManaFlowRule.none();
    }

    public ItemStackHandler getItemHandler() {
        ThermalManaFurnaceBlockEntity master = getMaster();
        return master == null ? inventory : master.inventory;
    }

    @Override
    public BlockPos getInspectionPosition() {
        ThermalManaFurnaceBlockEntity master = getMaster();
        return master == null ? worldPosition : master.worldPosition;
    }

    @Override
    public IBlockManaHandler getInspectionManaHandler() {
        ThermalManaFurnaceBlockEntity master = getMaster();
        return master == null ? null : master.getManaHandler(null);
    }

    @Override
    public ItemStackHandler getInspectionItemHandler() {
        return getItemHandler();
    }

    @Override
    public long getInspectionFlowRate() {
        ThermalManaFurnaceBlockEntity master = getMaster();
        return master == null ? 0L : master.getFlowRate();
    }

    @Override
    public IManaMachineBlockEntity getInspectionMachine() {
        return getMaster();
    }

    @Override
    public void appendInspectionData(MachineInspectionData.Builder builder) {
        ThermalManaFurnaceBlockEntity master = getMaster();
        if (master == null) {
            return;
        }
        ItemStack fuel = master.inventory.getStackInSlot(FUEL_SLOT);
        long queuedTime = (long) getBurnDuration(fuel) * fuel.getCount();
        long duration = master.getBurnDuration();
        if (duration <= 0) {
            duration = getBurnDuration(fuel);
        }
        builder.setTimeGauge(
                master.getBurnTime(),
                duration,
                master.getBurnTime() + queuedTime,
                MachineInspectionData.TIME_GAUGE_EMBER
        );
    }

    @Nullable
    private ThermalManaFurnaceBlockEntity getMaster() {
        if (isLowerPart()) {
            return this;
        }
        if (level == null) {
            return null;
        }
        int offset = getBlockState().getValue(ThermalManaFurnaceBlock.PART) == ThermalManaFurnaceBlock.Part.MIDDLE ? 1 : 2;
        BlockPos lowerPos = worldPosition.below(offset);
        if (level.getBlockEntity(lowerPos) instanceof ThermalManaFurnaceBlockEntity master
                && master.isLowerPart()) {
            return master;
        }
        return null;
    }

    private boolean isLowerPart() {
        return getBlockState().getValue(ThermalManaFurnaceBlock.PART) == ThermalManaFurnaceBlock.Part.LOWER;
    }

    private void updateLitState(boolean lit) {
        if (level == null || getBlockState().getValue(ThermalManaFurnaceBlock.LIT) == lit) {
            return;
        }
        for (int offset = 0; offset < 3; offset++) {
            BlockPos partPos = worldPosition.above(offset);
            BlockState partState = level.getBlockState(partPos);
            ThermalManaFurnaceBlock.Part expected = switch (offset) {
                case 0 -> ThermalManaFurnaceBlock.Part.LOWER;
                case 1 -> ThermalManaFurnaceBlock.Part.MIDDLE;
                default -> ThermalManaFurnaceBlock.Part.UPPER;
            };
            if (partState.is(BlockInit.THERMAL_MANA_FURNACE.get())
                    && partState.getValue(ThermalManaFurnaceBlock.PART) == expected
                    && partState.getValue(ThermalManaFurnaceBlock.LIT) != lit) {
                level.setBlock(partPos, partState.setValue(ThermalManaFurnaceBlock.LIT, lit), Block.UPDATE_CLIENTS);
            }
        }
        if (lit) {
            level.playSound(null, worldPosition, SoundEvents.FIRECHARGE_USE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            soundTickCounter = 0;
        }
    }

    private void onInventoryChanged() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public int getContainerSize() {
        return getItemHandler().getSlots();
    }

    @Override
    public boolean isEmpty() {
        return getItemHandler().getStackInSlot(FUEL_SLOT).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return getItemHandler().getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = getItemHandler().extractItem(index, count, false);
        if (!result.isEmpty()) {
            onInventoryChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack result = getItemHandler().getStackInSlot(index);
        if (!result.isEmpty()) {
            getItemHandler().setStackInSlot(index, ItemStack.EMPTY);
            onInventoryChanged();
        }
        return result;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int index = 0; index < getContainerSize(); index++) {
            items.set(index, getItem(index));
        }
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int index = 0; index < getContainerSize() && index < items.size(); index++) {
            getItemHandler().setStackInSlot(index, items.get(index));
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        getItemHandler().setStackInSlot(index, stack.copyWithCount(Math.min(stack.getCount(), getMaxStackSize())));
        onInventoryChanged();
    }

    @Override
    public void clearContent() {
        getItemHandler().setStackInSlot(FUEL_SLOT, ItemStack.EMPTY);
        onInventoryChanged();
    }

    public void drops() {
        ThermalManaFurnaceBlockEntity master = getMaster();
        if (master != this) {
            if (master != null) {
                master.drops();
            }
            return;
        }
        if (level == null || contentsDropped) {
            return;
        }
        contentsDropped = true;
        SimpleContainer container = new SimpleContainer(getContainerSize());
        for (int index = 0; index < getContainerSize(); index++) {
            container.setItem(index, getItem(index));
        }
        Containers.dropContents(level, worldPosition, container);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("burn_time", burnTime);
        tag.putInt("burn_duration", burnDuration);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        burnTime = Math.max(0, tag.getInt("burn_time"));
        burnDuration = Math.max(0, tag.getInt("burn_duration"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.thermal_mana_furnace");
    }
}
