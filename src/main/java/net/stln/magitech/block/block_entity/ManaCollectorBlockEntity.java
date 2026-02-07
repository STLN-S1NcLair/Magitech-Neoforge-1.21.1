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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.api.mana.ManaNodeLogicHelper;
import net.stln.magitech.block.*;
import net.stln.magitech.entity.mana.mana_parcel.ManaParcelEntity;
import net.stln.magitech.gui.ManaCollectorMenu;
import net.stln.magitech.gui.ManaStranderMenu;
import net.stln.magitech.network.ShootManaParcelTransferPayload;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ManaCollectorBlockEntity extends ManaContainerBlockEntity {

    private int tickCount = 0;
    protected long collectionRate = 1000;

    public ManaCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.MANA_COLLECTOR_ENTITY.get(), pos, blockState, 40000, 5000);
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

    public static void clientTicker(Level level, BlockPos pos, BlockState state, ManaCollectorBlockEntity blockEntity) {
        blockEntity.clientTick(level, pos, state);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        Vec3 center = pos.getCenter();
        RandomSource random = level.getRandom();
        for (int i = 0; i < 2; i++) {
            double x2 = center.x + Mth.nextDouble(random, -3, 3);
            double y2 = center.y + Mth.nextDouble(random, -3, 3);
            double z2 = center.z + Mth.nextDouble(random, -3, 3);
            double dx = (center.x - x2) / 10;
            double dy = (center.y - y2) / 10;
            double dz = (center.z - z2) / 10;
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, random.nextInt(5, 10), Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.05F), x2, y2, z2, dx, dy, dz);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        collectMana(level, pos, state);
    }

    protected void collectMana(Level level, BlockPos pos, BlockState state) {
        int collectorCount = 1;
        // 周囲を探索
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    if (i == 0 && j == 0 && k == 0) continue;

                    BlockPos targetPos = pos.offset(i, j, k);


                    BlockState targetState = level.getBlockState(targetPos);
                    BlockEntity targetBe = level.getBlockEntity(targetPos);

                    if (!(targetState.getBlock() instanceof ManaCollectorBlock) || !(targetBe instanceof ManaCollectorBlockEntity))
                        continue;

                    collectorCount++;
                }
            }
        }
        // マナ収集
        long collect = collectionRate / collectorCount;
        this.produceMana(collect);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaCollectorMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magitech.mana_collector");
    }

    @Override
    protected Component getDefaultName() {
        return null;
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
    public float getFlowBias() {
        return 1.0f;
    }

    // 排出のみ
    @Override
    public boolean canReceiveMana(Direction direction, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean canExtractMana(Direction direction, BlockPos pos, BlockState state) {
        return state.getValue(ManaStranderBlock.FACING).getOpposite() == direction;
    }
}
