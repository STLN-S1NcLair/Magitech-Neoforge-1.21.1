package net.stln.magitech.content.block;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.core.api.mana.flow.network.manager.ManaNetworkManager;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

import javax.annotation.Nullable;

import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

public abstract class ManaContainerBlock extends BaseEntityBlock {

    protected final long maxMana;
    protected final long maxFlow;

    protected ManaContainerBlock(Properties properties, long maxMana, long maxFlow) {
        super(properties);
        this.maxMana = maxMana;
        this.maxFlow = maxFlow;
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends ManaContainerBlockEntity> clientType
    ) {
        return level.isClientSide ? createTickerHelper(serverType, clientType, ManaContainerBlockEntity::clientTicker) : createTickerHelper(serverType, clientType, ManaContainerBlockEntity::ticker);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        requestRebuildNetwork(level, pos, false);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        requestRebuildNetwork(level, pos, true);
    }

    public static void requestRebuildNetwork(Level level, BlockPos worldPosition, boolean removal) {
        if (level != null && !level.isClientSide && level instanceof ServerLevel serverLevel) {
            ManaNetworkManager.get(serverLevel).requestRebuild(serverLevel, worldPosition, removal);
        }
    }

    public long getMaxMana() {
        return maxMana;
    }

    public long getMaxFlow() {
        return maxFlow;
    }

    /**
     * マナの接続方向ごとに共通の接続パーティクルを表示します。
     * Displays the shared connector particle effect for each mana connection direction.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (level.getBlockEntity(pos) instanceof ManaContainerBlockEntity manaContainer) {
            Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier =
                    (lvl, vec, element) -> PresetHelper.longer(SquareParticles.squareShrinkParticle(lvl, vec, element));
            for (Direction direction : manaContainer.getConnectableDirections(state)) {
                Vec3 dir = Vec3.atLowerCornerOf(direction.getNormal());
                PointVFX.ring(level, pos.getCenter().add(dir.scale(0.5)), Element.MANA, supplier, dir, 1, 0.05F, 0.05F, 0.0F);
            }
        }
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
