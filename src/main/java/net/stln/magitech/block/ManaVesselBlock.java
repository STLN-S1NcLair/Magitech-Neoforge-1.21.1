package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ManaVesselBlock extends ManaContainerBlock {

    public static final MapCodec<AlchemetricPylonBlock> CODEC = simpleCodec(AlchemetricPylonBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("block.magitech.mana_vessel");

    protected ManaVesselBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaVesselBlockEntity(blockPos, blockState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.MANA_VESSEL_ENTITY.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof ManaVesselBlockEntity) {
                player.openMenu((MenuProvider) blockentity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        for (int i = 0; i < 2; i++) {
            double x = center.x + Mth.nextDouble(random, -0.6, 0.6);
            double y = center.y + Mth.nextDouble(random, -0.6, 0.6);
            double z = center.z + Mth.nextDouble(random, -0.6, 0.6);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, 0, 15, 1.0F), x, y, z, 0, 0, 0);
        }
        for (int i = 0; i < 2; i++) {
            int direction = i == 0 ? -1 : 1;
            double x = center.x + Mth.nextDouble(random, -0.2, 0.2);
            double y = center.y + 0.5 * direction;
            double z = center.z + Mth.nextDouble(random, -0.2, 0.2);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.0F), x, y, z, 0, 0.03 * direction, 0);
        }
        for (int i = 0; i < 4; i++) {
            double x2 = center.x + Mth.nextDouble(random, -0.5, 0.5);
            double y2 = center.y + Mth.nextDouble(random, -0.5, 0.5);
            double z2 = center.z + Mth.nextDouble(random, -0.5, 0.5);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.0F), x2, y2, z2, 0, 0.03, 0);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ManaVesselBlockEntity manaVesselBlockEntity) {
                manaVesselBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
