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
import net.stln.magitech.api.mana.handler.ContainerBlockEntityManaHandler;
import net.stln.magitech.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ManaReceiverBlock;
import net.stln.magitech.block.ManaStranderBlock;
import net.stln.magitech.entity.mana.mana_parcel.ManaParcelEntity;
import net.stln.magitech.gui.ManaReceiverMenu;
import net.stln.magitech.gui.ManaVesselMenu;
import net.stln.magitech.network.ShootManaParcelTransferPayload;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaReceiverBlockEntity extends ManaMachineBlockEntity {

    public ManaReceiverBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.MANA_RECEIVER_ENTITY.get(), pos, blockState, 500000, 5000);
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
        checkManaParcel(level, pos, state);
    }

    private void checkManaParcel(Level level, BlockPos pos, BlockState state) {
        MachineBlockEntityManaHandler handler = getManaHandler(null);
        List<Entity> entities = EntityUtil.getEntitiesInBox(level, null, pos.getCenter(), new Vec3(0.5, 0.5, 0.5));
        for (Entity entity : entities) {
            if (entity instanceof ManaParcelEntity manaParcel) {
                handler.produceMana(manaParcel.getMana());
                manaParcel.discard();
                level.playSound(null, pos, SoundInit.MANA_PARCEL.get(), SoundSource.BLOCKS, 0.3F, Mth.randomBetween(level.random, 0.5F, 1.0F));
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaReceiverMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_receiver");
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
            return ManaFlowRule.BothWays(1.0F);
        }
        return ManaFlowRule.None();
    }
}
