package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.ShockBlasterBlockEntity;
import net.stln.magitech.helper.MachinePlacementHelper;
import net.stln.magitech.helper.VoxelShapeHelper;
import org.jetbrains.annotations.Nullable;

public class ShockBlasterBlock extends ManaContainerBlock {
    public static final MapCodec<ShockBlasterBlock> CODEC = simpleCodec(ShockBlasterBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(3, 4, 3, 13, 16, 13),
            Block.box(2, 4, 2, 3, 16, 14),
            Block.box(13, 4, 2, 14, 16, 14),
            Block.box(2, 4, 2, 14, 16, 3),
            Block.box(2, 4, 13, 14, 16, 14)
    );
    private static final VoxelShape SHAPE_DOWN = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.DOWN);
    private static final VoxelShape SHAPE_NORTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.NORTH);
    private static final VoxelShape SHAPE_SOUTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.SOUTH);
    private static final VoxelShape SHAPE_EAST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.EAST);
    private static final VoxelShape SHAPE_WEST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.WEST);

    public ShockBlasterBlock(Properties properties, long maxMana, long maxFlow) {
        super(properties, maxMana, maxFlow);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(POWERED, false));
    }

    protected ShockBlasterBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends ShockBlasterBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
        };
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING,
                MachinePlacementHelper.getFacing(context, context.getNearestLookingDirection()))
                .setValue(POWERED, false);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(state.getBlock())) {
            checkPowered(state, level, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        checkPowered(state, level, pos);
    }

    private void checkPowered(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShockBlasterBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTicker(level, type, BlockInit.SHOCK_BLASTER_ENTITY.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }
}
