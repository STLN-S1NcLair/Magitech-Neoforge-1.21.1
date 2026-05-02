package net.stln.magitech.content.block;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

import javax.annotation.Nullable;

public abstract class ManaPumpBlock extends ManaContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected ManaPumpBlock(Properties properties, int maxMana, int maxFlow) {
        super(properties, maxMana, maxFlow);
    }

    protected ManaPumpBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected abstract MapCodec<? extends BaseEntityBlock> codec();

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return rotatePillar(state, rot);
    }

    public static BlockState rotatePillar(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_90:
                switch (state.getValue(FACING)) {
                    case NORTH:
                        return state.setValue(FACING, Direction.EAST);
                    case EAST:
                        return state.setValue(FACING, Direction.SOUTH);
                    case SOUTH:
                        return state.setValue(FACING, Direction.WEST);
                    case WEST:
                        return state.setValue(FACING, Direction.NORTH);
                    default:
                        return state;
                }
            case COUNTERCLOCKWISE_90:
                switch (state.getValue(FACING)) {
                    case NORTH:
                        return state.setValue(FACING, Direction.WEST);
                    case EAST:
                        return state.setValue(FACING, Direction.NORTH);
                    case SOUTH:
                        return state.setValue(FACING, Direction.EAST);
                    case WEST:
                        return state.setValue(FACING, Direction.SOUTH);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> switch (state.getValue(FACING)) {
                case NORTH:
                    yield state.setValue(FACING, Direction.SOUTH);
                case SOUTH:
                    yield state.setValue(FACING, Direction.NORTH);
                default:
                    yield state;
            };
            case FRONT_BACK -> switch (state.getValue(FACING)) {
                case EAST:
                    yield state.setValue(FACING, Direction.WEST);
                case WEST:
                    yield state.setValue(FACING, Direction.EAST);
                default:
                    yield state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    @Override
    public abstract @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType);

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
        Vec3 dir = switch (state.getValue(FACING).getAxis().choose(0, 1, 2)) {
            case 0 -> new Vec3(1, 0, 0);
            case 1 -> new Vec3(0, 1, 0);
            case 2 -> new Vec3(0, 0, 1);
            default -> Vec3.ZERO;
        };
        Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier = (lvl, vec, elm) -> PresetHelper.longer(SquareParticles.squareShrinkParticle(lvl, vec, elm));
        PointVFX.ring(level, pos.getCenter().add(dir.scale(0.5)), Element.MANA, supplier, dir, 1, 0.05F, 0.05F, 0.0F);
        PointVFX.ring(level, pos.getCenter().add(dir.scale(-0.5)), Element.MANA, supplier, dir.reverse(), 1, 0.05F, 0.05F, 0.0F);
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
