package net.stln.magitech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.util.VoxelShapeUtil;
import org.joml.Vector3f;

public class ManaNodeBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(4, 0, 4, 12, 9, 12)
    );
    public static final VoxelShape SHAPE_DOWN = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.DOWN);
    public static final VoxelShape SHAPE_NORTH = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.NORTH);
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.WEST);

    public ManaNodeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, Boolean.valueOf(false))
        );
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
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        double x = center.x + Mth.nextDouble(random, -0.4, 0.4);
        double y = center.y + Mth.nextDouble(random, -0.4, 0.4);
        double z = center.z + Mth.nextDouble(random, -0.4, 0.4);
        level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, 0), x, y, z, 0, 0, 0);
        for (int i = 0; i < 2; i++) {
            double x2 = center.x + Mth.nextDouble(random, -0.3, 0.3);
            double y2 = center.y + Mth.nextDouble(random, -0.3, 0.3);
            double z2 = center.z + Mth.nextDouble(random, -0.3, 0.3);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F)), x2, y2, z2, 0, 0.03, 0);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}
