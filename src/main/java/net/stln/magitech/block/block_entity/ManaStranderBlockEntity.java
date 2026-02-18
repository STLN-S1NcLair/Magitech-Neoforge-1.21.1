package net.stln.magitech.block.block_entity;

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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.mana.flow.ManaFlowRule;
import net.stln.magitech.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ManaStranderBlock;
import net.stln.magitech.entity.mana.mana_parcel.ManaParcelEntity;
import net.stln.magitech.gui.ManaStranderMenu;
import net.stln.magitech.network.ShootManaParcelTransferPayload;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

public class ManaStranderBlockEntity extends ManaMachineBlockEntity {

    private int tickCount = 0;
    private static final long MANA_PARCEL_ENERGY = 50000;

    public ManaStranderBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.MANA_STRANDER_ENTITY.get(), pos, blockState, 100000, 5000);
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
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        tickCount++;
        if (this.tickCount >= 10) {
            shootManaParcel(level, pos, state);
        }
    }

    private void shootManaParcel(Level level, BlockPos pos, BlockState state) {
        MachineBlockEntityManaHandler handler = getManaHandler(null);
        if (handler.getMana() >= MANA_PARCEL_ENERGY) {
            handler.consumeMana(MANA_PARCEL_ENERGY);
            Direction direction = state.getValue(ManaStranderBlock.FACING);
            Vec3 summonPos = pos.getCenter();
            Entity entity = new ManaParcelEntity(level, summonPos, MANA_PARCEL_ENERGY);
            entity.setDeltaMovement(Vec3.atLowerCornerOf(direction.getNormal()).scale(0.5F));
            entity.setPos(summonPos.subtract(0, entity.getBbHeight() / 2, 0));
            level.addFreshEntity(entity);
            PacketDistributor.sendToPlayersTrackingChunk(
                    (ServerLevel) level,
                    new ChunkPos(pos),
                    new ShootManaParcelTransferPayload(pos, direction)
            );
            level.playSound(null, pos, SoundInit.MANA_PARCEL.get(), SoundSource.BLOCKS, 0.3F, Mth.randomBetween(level.random, 0.5F, 1.0F));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaStranderMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_strander");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    public int getContainerSize() {
        return 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side == state.getValue(ManaStranderBlock.FACING).getOpposite()) {
            return ManaFlowRule.BothWays(-1.0F);
        }
        return ManaFlowRule.None();
    }
}
