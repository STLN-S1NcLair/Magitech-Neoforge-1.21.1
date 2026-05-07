package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.ManaCollectorBlockEntity;
import net.stln.magitech.helper.VoxelShapeHelper;
import org.jetbrains.annotations.Nullable;

public class ManaCollectorBlock extends ManaContainerBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(2, 4, 2, 14, 15, 14),
            Block.box(1, 14, 1, 15, 16, 3),
            Block.box(1, 14, 1, 3, 16, 15),
            Block.box(13, 14, 1, 15, 16, 15),
            Block.box(1, 14, 13, 15, 16, 15)
    );
    public static final VoxelShape SHAPE_DOWN = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.DOWN);
    public static final VoxelShape SHAPE_NORTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.NORTH);
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.WEST);

    public static final MapCodec<ManaCollectorBlock> CODEC = simpleCodec(ManaCollectorBlock::new);

    public ManaCollectorBlock(Properties properties, int maxMana, int maxFlow) {
        super(properties, maxMana, maxFlow);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(WATERLOGGED, false));
    }

    protected ManaCollectorBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState p_154346_, BlockGetter p_154347_, BlockPos p_154348_, CollisionContext p_154349_) {
        return switch (p_154346_.getValue(FACING)) {
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
        };
    }

    @Override
    protected BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return p_154354_.setValue(FACING, p_154355_.rotate(p_154354_.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return p_154351_.setValue(FACING, p_154352_.mirror(p_154351_.getValue(FACING)));
    }

    @Override
    protected boolean isPathfindable(BlockState p_154341_, PathComputationType p_154344_) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean water = WaterloggedBlockUtil.isWaterAtPlacement(context);
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, water);
    }

    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        WaterloggedBlockUtil.scheduleWaterTickIfNeeded(state, WATERLOGGED, level, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level instanceof net.minecraft.server.level.ServerLevel serverlevel) {
            this.checkPowered(state, serverlevel, pos);
        }
    }

    public void checkPowered(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos) {
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, powered), 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaCollectorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTicker(level, type, BlockInit.MANA_COLLECTOR_ENTITY.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof ManaCollectorBlockEntity) {
                player.openMenu((MenuProvider) blockentity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, WATERLOGGED);
    }
}
