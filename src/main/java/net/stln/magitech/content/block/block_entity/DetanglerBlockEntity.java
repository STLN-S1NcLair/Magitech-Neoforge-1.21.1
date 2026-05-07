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
import net.stln.magitech.content.block.ManaCollectorBlock;
import net.stln.magitech.content.entity.mana.mana_parcel.ManaParcelEntity;
import net.stln.magitech.content.network.EntanglerEntanglePayload;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.ItemHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DetanglerBlockEntity extends BlockEntity {

    public DetanglerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.DETANGLER_ENTITY.get(), pos, blockState);
    }

    public static void clientTicker(Level level, BlockPos pos, BlockState state, DetanglerBlockEntity blockEntity) {
        blockEntity.clientTick(level, pos, state);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, DetanglerBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {

    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        checkManaParcel(level, pos, state);
    }

    private void checkManaParcel(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.POWERED)) return; // レッドストーン信号がある場合は動作しない
        Direction direction = state.getValue(ManaCollectorBlock.FACING);
        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction.getOpposite()), direction);
        if (itemHandler == null) return; // アイテムハンドラーがない場合は動作しない
        List<Entity> entities = CombatHelper.getEntitiesInBox(level, null, pos.getCenter(), new Vec3(0.8, 0.8, 0.8));
        for (Entity entity : entities) {
            if (!level.isClientSide && entity instanceof ManaParcelEntity manaParcel && !manaParcel.getStack().isEmpty()) {
                ItemStack stack = manaParcel.getStack();
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    stack = itemHandler.insertItem(i, stack, false);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
                manaParcel.setStack(stack);
                level.playSound(null, pos, SoundInit.MANA_PARCEL.get(), SoundSource.BLOCKS, 0.3F, Mth.randomBetween(level.random, 0.5F, 1.0F));
                PacketDistributor.sendToAllPlayers(new EntanglerEntanglePayload(pos));
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
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
