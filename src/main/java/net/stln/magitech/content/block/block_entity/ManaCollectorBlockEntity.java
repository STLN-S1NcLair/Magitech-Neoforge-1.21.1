package net.stln.magitech.content.block.block_entity;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaCollectorBlock;
import net.stln.magitech.content.block.ManaStranderBlock;
import net.stln.magitech.content.gui.ManaCollectorMenu;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

public class ManaCollectorBlockEntity extends ManaMachineBlockEntity {

    protected long collectionRate = 1000;

    public ManaCollectorBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_COLLECTOR_ENTITY.get(), pos, blockState, mana);
    }

    public ManaCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
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
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        super.clientTick(level, pos, state);
        Vec3 center = pos.getCenter();
        RandomSource random = level.getRandom();
        for (int i = 0; i < 2; i++) {
            Function3<Level, Vec3, Element, ParticleEffectSpawner> particle = (lvl, vec, elm) -> PresetHelper.smaller(PresetHelper.modify(SquareParticles.squareParticle(lvl, vec ,elm), WorldParticleBuilder::enableNoClip));
            PointVFX.vortex(level, pos.getCenter(), Element.MANA, particle, 1, 2.0F, 0.9F, 3.0F);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        collectMana(level, pos, state);
    }

    protected void collectMana(Level level, BlockPos pos, BlockState state) {
        MachineBlockEntityManaHandler handler = getManaHandler(null);
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
        handler.produceMana(collect);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaCollectorMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.mana_collector");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side == state.getValue(ManaStranderBlock.FACING).getOpposite()) {
            return ManaFlowRule.bothWays(1.0F);
        }
        return ManaFlowRule.none();
    }
}
