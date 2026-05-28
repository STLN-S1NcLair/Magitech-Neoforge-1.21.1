package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.entity.mana.mana_parcel.ManaParcelEntity;
import net.stln.magitech.content.network.EntanglerEntanglePayload;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.ItemHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntanglerBlockEntity extends BlockEntity {

    // ItemStackHandlerの変更を監視してサーバ側で同期を取る
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            EntanglerBlockEntity.this.onInventoryChanged();
        }
    };
    protected final ItemStackHandler lockedInv = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            EntanglerBlockEntity.this.onInventoryChanged();
        }
    };

    public EntanglerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.ENTANGLER_ENTITY.get(), pos, blockState);
    }

    public static void clientTicker(Level level, BlockPos pos, BlockState state, EntanglerBlockEntity blockEntity) {
        blockEntity.clientTick(level, pos, state);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, EntanglerBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {

    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        unlockItem();
        checkManaParcel(level, pos, state);
        extractItem(level, pos, state);
    }

    private void checkManaParcel(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.POWERED)) return; // レッドストーン信号がある場合は動作しない
        List<Entity> entities = CombatHelper.getEntitiesInBox(level, null, pos.getCenter(), new Vec3(0.8, 0.8, 0.8));
        for (Entity entity : entities) {
            if (entity instanceof ManaParcelEntity manaParcel) {
                if (manaParcel.getStack().isEmpty() && !isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack stack = this.getItem(0).split(16);
                        manaParcel.setStack(stack);
                        SoundHelper.broadcastSound(level, pos.getCenter(), SoundInit.MANA_PARCEL.get(), SoundSource.BLOCKS);
                        PacketDistributor.sendToAllPlayers(new EntanglerEntanglePayload(pos));
                    }
                }
            }
        }
    }

    private void extractItem(Level level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 5 == 0) {
            Direction direction = state.getValue(BlockStateProperties.FACING).getOpposite();
            BlockPos targetPos = pos.offset(direction.getNormal());
            IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, direction);
            if (handler == null) return;
            if (!level.isClientSide) {
                if (isEmpty()) {
                    ItemStack stack = ItemHelper.extractStack(lockedInv.getStackInSlot(0), handler, 64, false);
                    BlockEntity target = level.getBlockEntity(targetPos);
                    if (target != null) {
                        target.setChanged();
                        BlockState targetState = level.getBlockState(targetPos);
                        this.level.sendBlockUpdated(targetPos, targetState, targetState, 3);
                    }
                    lockedInv.setStackInSlot(0, stack);
                }
            }
        }
    }

    private void unlockItem() {
        if (inventory.getStackInSlot(0).isEmpty() && !lockedInv.getStackInSlot(0).isEmpty()) {
            inventory.setStackInSlot(0, lockedInv.getStackInSlot(0));
            lockedInv.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    // Container メソッド実装 - ItemStackHandler へ直接委譲して、GUI側の操作を実体に反映
    private void onInventoryChanged() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty() && lockedInv.getStackInSlot(0).isEmpty();
    }

    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.extractItem(index, count, false);
        if (!stack.isEmpty()) {
            this.onInventoryChanged();
        }
        return stack;
    }

    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        this.onInventoryChanged();
        return stack;
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

    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack.copyWithCount(Math.min(stack.getCount(), this.getMaxStackSize())));
        this.onInventoryChanged();
    }

    private int getMaxStackSize() {
        return 99;
    }

    public void clearContent() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.onInventoryChanged();
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(2);
        inv.setItem(0, inventory.getStackInSlot(0));
        inv.setItem(1, lockedInv.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("locked", lockedInv.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        lockedInv.deserializeNBT(registries, tag.getCompound("locked"));
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
}
