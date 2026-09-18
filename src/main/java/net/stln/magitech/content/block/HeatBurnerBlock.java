package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.HeatBurnerBlockEntity;
import net.stln.magitech.helper.MachineInteractionHelper;
import net.stln.magitech.helper.MachinePlacementHelper;
import org.jetbrains.annotations.Nullable;

/**
 * 2ブロック高の加熱機械ブロックです。
 * A two-block-tall heat-producing machine block.
 */
public class HeatBurnerBlock extends ManaContainerBlock {
    public static final MapCodec<HeatBurnerBlock> CODEC = simpleCodec(HeatBurnerBlock::new);
    public static final net.minecraft.world.level.block.state.properties.DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = BlockStateProperties.LIT;

    /**
     * モデルの下半分に対応する形状です。
     * Shape matching the lower-half model.
     */
    private static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.box(2, 2, 2, 14, 14, 14),
            Block.box(3, 14, 3, 13, 16, 13),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(4, 4, 0, 12, 12, 2),
            Block.box(0, 4, 4, 2, 12, 12),
            Block.box(14, 4, 4, 16, 12, 12),
            Block.box(4, 4, 14, 12, 12, 16)
    );

    /**
     * モデルの上半分に対応する形状です。
     * Shape matching the upper-half model.
     */
    private static final VoxelShape UPPER_SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 4, 14),
            Block.box(6, 4, 12, 10, 6, 14),
            Block.box(2, 4, 6, 4, 6, 10),
            Block.box(6, 4, 2, 10, 6, 4),
            Block.box(12, 4, 6, 14, 6, 10)
    );

    protected HeatBurnerBlock(Properties properties, long maxMana, long maxFlow) {
        super(properties, maxMana, maxFlow);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(LIT, false));
    }

    protected HeatBurnerBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (pos.getY() >= level.getMaxBuildHeight() - 1) {
            return null;
        }
        BlockState state = defaultBlockState()
                .setValue(FACING, MachinePlacementHelper.getFacing(context, context.getHorizontalDirection()))
                .setValue(HALF, DoubleBlockHalf.LOWER);
        BlockPos upperPos = pos.above();
        BlockState upperState = state.setValue(HALF, DoubleBlockHalf.UPPER);
        if (!level.getBlockState(upperPos).canBeReplaced(context)
                || !level.isUnobstructed(upperState, upperPos, CollisionContext.of(context.getPlayer()))) {
            return null;
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            // 下半分の状態から上半分を派生させ、クライアント予測とサーバー状態を一致させます。
            // Derive the upper state from the lower state so client prediction and server state stay aligned.
            level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (half == DoubleBlockHalf.LOWER) {
            return level.getBlockState(pos.above()).canBeReplaced();
        }

        BlockState below = level.getBlockState(pos.below());
        return below.is(this)
                && below.getValue(HALF) == DoubleBlockHalf.LOWER
                && below.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (direction == Direction.UP && half == DoubleBlockHalf.LOWER) {
            return neighborState.is(this) && neighborState.getValue(HALF) == DoubleBlockHalf.UPPER
                    ? state
                    : Blocks.AIR.defaultBlockState();
        }
        if (direction == Direction.DOWN && half == DoubleBlockHalf.UPPER) {
            return neighborState.is(this) && neighborState.getValue(HALF) == DoubleBlockHalf.LOWER
                    ? state
                    : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
            BlockState lowerState = level.getBlockState(lowerPos);
            if (lowerState.is(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    if (level.getBlockEntity(lowerPos) instanceof HeatBurnerBlockEntity heatBurner) {
                        heatBurner.drops();
                    }
                    level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                    level.levelEvent(player, 2001, lowerPos, Block.getId(lowerState));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeatBurnerBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof HeatBurnerBlockEntity heatBurner) {
            return MachineInteractionHelper.interact(stack, level, pos, player, hand, heatBurner);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return createTicker(level, blockEntityType, BlockInit.HEAT_BURNER_ENTITY.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()
                && state.getValue(HALF) == DoubleBlockHalf.LOWER
                && level.getBlockEntity(pos) instanceof HeatBurnerBlockEntity heatBurner) {
            heatBurner.drops();
            heatBurner.removeFieldEffects();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
