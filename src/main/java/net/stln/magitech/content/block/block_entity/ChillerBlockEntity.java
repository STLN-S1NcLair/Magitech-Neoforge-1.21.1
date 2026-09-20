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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ChillerBlock;
import net.stln.magitech.content.field_effect.effect.FieldEffectInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.data.RangeEntry;
import net.stln.magitech.core.api.field_effect.sync.FieldEffectCacheSyncManager;
import net.stln.magitech.api.machine.inspection.IMachineInspectionTarget;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.effect.visual.preset.BlockVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Chiller のマナ・インベントリ・フィールド効果を管理する BlockEntity です。
 * Block entity that manages the Chiller's mana, inventory, and field effects.
 */
public class ChillerBlockEntity extends ManaMachineBlockEntity implements IItemHandlerBlockEntity, IMachineInspectionTarget {
    public static final int CRYSTAL_SLOT = 0;
    public static final long MANA_PER_TICK = 500;
    public static final int EXPANSION_DURATION_TICKS = 5 * 60 * 20;

    private int expansionTicksRemaining;
    private int soundTickCounter;
    private boolean fieldEffectActive;
    private boolean fieldEffectBoosted;
    private boolean contentsDropped;

    /**
     * 氷結相結晶だけを受け入れる1スロットインベントリです。
     * A one-slot inventory that accepts only Glace crystals.
     */
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            ChillerBlockEntity.this.onInventoryChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == CRYSTAL_SLOT && stack.is(ItemInit.GLACE_CRYSTAL.get());
        }
    };

    public ChillerBlockEntity(BlockPos pos, BlockState state, long mana) {
        super(BlockInit.CHILLER_ENTITY.get(), pos, state, mana);
    }

    public ChillerBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 0L);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (!isLowerHalf() || level.isClientSide) {
            return;
        }

        boolean wasBoosted = isBoosted();
        boolean supplied = isSupplied();
        updateLitState(supplied);
        if (supplied) {
            getManaHandler(null).consumeMana(MANA_PER_TICK);
            if (isBoosted()) {
                expansionTicksRemaining--;
            }
            if (expansionTicksRemaining <= 0 && !inventory.getStackInSlot(CRYSTAL_SLOT).isEmpty()) {
                inventory.extractItem(CRYSTAL_SLOT, 1, false);
                expansionTicksRemaining = EXPANSION_DURATION_TICKS;
            }
            if (soundTickCounter % 40 == 0) {
                level.playSound(null, pos, SoundInit.CHILLER.get(), SoundSource.BLOCKS, 0.1F, 1.0F);
                soundTickCounter = 0;
            }
            soundTickCounter++;
        } else {
            soundTickCounter = 0;
        }

        boolean boosted = isBoosted();
        if (wasBoosted != boosted) {
            setChanged();
            Packet<ClientGamePacketListener> updatePacket = getUpdatePacket();
            if (updatePacket != null) {
                ((ServerLevel) level).getChunkSource().chunkMap
                        .getPlayers(new ChunkPos(worldPosition), false)
                        .forEach(player -> player.connection.send(updatePacket));
            }
        }
        if (supplied != fieldEffectActive || (supplied && boosted != fieldEffectBoosted) || wasBoosted != boosted) {
            updateFieldEffects((ServerLevel) level, supplied);
        }
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        addVFX(level, pos, state);
    }

    private boolean isSupplied() {
        return getMana() >= MANA_PER_TICK;
    }

    private boolean isBoosted() {
        return expansionTicksRemaining > 0;
    }

    private void addVFX(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(ChillerBlock.LIT)) {
            FieldEffectType type;
            float speed = 0.05F;
            Vec3 center = pos.above().getCenter();
            if (isBoosted()) {
                type = FieldEffectInit.FREEZING.get();
                speed = 0.08F;
                BlockVFX.horizontalSpray(level, Element.GLACE, center, ElementParticles::snowParticle, 1, 0.03F, 0.01F, 0.3F);
            } else {
                type = FieldEffectInit.COLD.get();
            }
            BlockVFX.horizontalSpray(level, type.getPrimary(), type.getSecondary(), center, 2, speed, 0.01F, 0.3F);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel && isLowerHalf()) {
            boolean supplied = isSupplied();
            updateLitState(supplied);
            updateFieldEffects(serverLevel, supplied);
        }
    }

    private void updateLitState(boolean lit) {
        if (level == null) {
            return;
        }
        setLitState(worldPosition, lit);
        BlockPos upperPos = worldPosition.above();
        BlockState upperState = level.getBlockState(upperPos);
        if (upperState.is(BlockInit.CHILLER.get())
                && upperState.getValue(ChillerBlock.HALF) == DoubleBlockHalf.UPPER) {
            setLitState(upperPos, lit);
        }
    }

    private void setLitState(BlockPos pos, boolean lit) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockInit.CHILLER.get()) && state.getValue(ChillerBlock.LIT) != lit) {
            level.setBlock(pos, state.setValue(ChillerBlock.LIT, lit), Block.UPDATE_CLIENTS);
        }
    }

    /**
     * 現在の拡大残り時間を tick 単位で返します。
     * Returns the remaining expansion time in ticks.
     */
    public int getExpansionTicksRemaining() {
        return expansionTicksRemaining;
    }

    /**
     * 拡大時間の総量を tick 単位で返します。
     * Returns the total expansion duration in ticks.
     */
    public int getExpansionDurationTicks() {
        return EXPANSION_DURATION_TICKS;
    }

    /**
     * 拡大時間の残り割合を 0.0 から 1.0 の範囲で返します。
     * Returns the remaining expansion ratio in the range 0.0 to 1.0.
     */
    public float getExpansionRemainingRatio() {
        return Math.clamp((float) expansionTicksRemaining / EXPANSION_DURATION_TICKS, 0.0F, 1.0F);
    }

    /**
     * 上半分からも下半分の共有インベントリを取得します。
     * Gets the shared lower-half inventory from the upper half as well.
     */
    public ItemStackHandler getItemHandler() {
        ChillerBlockEntity master = getMaster();
        return master == null ? inventory : master.inventory;
    }

    @Override
    public int getInputSlot() {
        return CRYSTAL_SLOT;
    }

    @Nullable
    public ItemStackHandler getItemHandler(Direction side) {
        return getItemHandler();
    }

    @Nullable
    private ChillerBlockEntity getMaster() {
        if (isLowerHalf()) {
            return this;
        }
        if (level != null && level.getBlockEntity(worldPosition.below()) instanceof ChillerBlockEntity chiller
                && chiller.isLowerHalf()) {
            return chiller;
        }
        return null;
    }

    private boolean isLowerHalf() {
        return getBlockState().getValue(ChillerBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    private void updateFieldEffects(ServerLevel serverLevel, boolean active) {
        if (!active) {
            removeFieldEffects();
            return;
        }

        FieldInfluenceInstance cold = FieldInfluenceInstance.of(
                new FieldInfluence(FieldInfluenceInit.COOLING.get(), 1)
        );
        List<RangeEntry> ranges;
        if (isBoosted()) {
            ranges = List.of(
                    new RangeEntry(worldPosition.offset(-2, 0, -2), worldPosition.offset(2, 2, 2), worldPosition, cold),
                    new RangeEntry(worldPosition.offset(-1, 0, -1), worldPosition.offset(1, 1, 1), worldPosition, cold)
            );
        } else {
            ranges = List.of(
                    new RangeEntry(worldPosition.offset(-1, 0, -1), worldPosition.offset(1, 1, 1), worldPosition, cold)
            );
        }
        FieldEffectCacheSyncManager.replaceSourceAndSync(serverLevel, worldPosition, ranges);
        fieldEffectActive = true;
        fieldEffectBoosted = isBoosted();
    }

    /**
     * この Chiller が発生させたフィールド効果を削除します。
     * Removes the field effects emitted by this Chiller.
     */
    public void removeFieldEffects() {
        if (level instanceof ServerLevel serverLevel && isLowerHalf()) {
            FieldEffectCacheSyncManager.removeSourceAndSync(serverLevel, worldPosition);
        }
        fieldEffectActive = false;
        fieldEffectBoosted = false;
    }

    @Override
    public MachineBlockEntityManaHandler getManaHandler(Direction side) {
        if (!isLowerHalf()) {
            return null;
        }
        if (side != null && getManaFlowRule(getBlockState(), side).isNone()) {
            return null;
        }
        return super.getManaHandler(side);
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (isLowerHalf() && side != null && side == state.getValue(ChillerBlock.FACING)) {
            return ManaFlowRule.insertOnly(-1.0F);
        }
        return ManaFlowRule.none();
    }

    @Override
    public BlockPos getInspectionPosition() {
        ChillerBlockEntity master = getMaster();
        return master == null ? worldPosition : master.worldPosition;
    }

    @Override
    public IBlockManaHandler getInspectionManaHandler() {
        ChillerBlockEntity master = getMaster();
        return master == null ? null : master.getManaHandler(null);
    }

    @Override
    public ItemStackHandler getInspectionItemHandler() {
        return getItemHandler();
    }

    @Override
    public long getInspectionFlowRate() {
        ChillerBlockEntity master = getMaster();
        return master == null ? 0L : master.getFlowRate();
    }

    @Override
    public IManaMachineBlockEntity getInspectionMachine() {
        return getMaster();
    }

    @Override
    public void appendInspectionData(MachineInspectionData.Builder builder) {
        ChillerBlockEntity master = getMaster();
        if (master == null) {
            return;
        }
        ItemStack crystals = master.inventory.getStackInSlot(CRYSTAL_SLOT);
        long totalRemainingTime = master.getExpansionTicksRemaining()
                + (long) crystals.getCount() * EXPANSION_DURATION_TICKS;
        builder.setTimeGauge(
                master.getExpansionTicksRemaining(),
                EXPANSION_DURATION_TICKS,
                totalRemainingTime,
                MachineInspectionData.TIME_GAUGE_GLACE
        );
    }

    @Override
    public int getContainerSize() {
        return getItemHandler().getSlots();
    }

    @Override
    public boolean isEmpty() {
        return getItemHandler().getStackInSlot(CRYSTAL_SLOT).isEmpty();
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
        for (int i = 0; i < getContainerSize(); i++) {
            items.set(i, getItem(i));
        }
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < getContainerSize() && i < items.size(); i++) {
            getItemHandler().setStackInSlot(i, items.get(i));
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        getItemHandler().setStackInSlot(index, stack.copyWithCount(Math.min(stack.getCount(), getMaxStackSize())));
        onInventoryChanged();
    }

    @Override
    public void clearContent() {
        getItemHandler().setStackInSlot(CRYSTAL_SLOT, ItemStack.EMPTY);
        onInventoryChanged();
    }

    private void onInventoryChanged() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void drops() {
        if (!isLowerHalf() || level == null || contentsDropped) {
            return;
        }
        contentsDropped = true;
        SimpleContainer container = new SimpleContainer(getContainerSize());
        for (int i = 0; i < getContainerSize(); i++) {
            container.setItem(i, getItem(i));
        }
        Containers.dropContents(level, worldPosition, container);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("expansion_ticks_remaining", expansionTicksRemaining);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        expansionTicksRemaining = Math.clamp(tag.getInt("expansion_ticks_remaining"), 0, EXPANSION_DURATION_TICKS);
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
        return Component.translatable("block.magitech.chiller");
    }
}
