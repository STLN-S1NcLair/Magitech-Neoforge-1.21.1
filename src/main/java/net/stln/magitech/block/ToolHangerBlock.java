package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.block.block_entity.ToolHangerBlockEntity;
import net.stln.magitech.item.ItemTagKeys;
import net.stln.magitech.util.TickScheduler;
import net.stln.magitech.util.VoxelShapeUtil;

import javax.annotation.Nullable;

public class ToolHangerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(0, 0, 14, 16, 16, 16),
            Block.box(0, 12, 6, 16, 16, 14)
    );
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.WEST);
    public static final MapCodec<ToolHangerBlock> CODEC = simpleCodec(ToolHangerBlock::new);

    public ToolHangerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case Direction.NORTH -> SHAPE_NORTH;
            case Direction.SOUTH -> SHAPE_SOUTH;
            case Direction.EAST -> SHAPE_EAST;
            case Direction.WEST -> SHAPE_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ToolHangerBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ToolHangerBlockEntity pylonBlockEntity) {
                pylonBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof ToolHangerBlockEntity toolHangerBlockEntity) {
            ItemStack itemInHand = player.getItemInHand(hand);
            if (itemInHand.isEmpty() || itemInHand.getTags().anyMatch(itemTagKey -> itemTagKey.equals(ItemTagKeys.TOOLS))) {
                toolHangerBlockEntity.addItem(player, itemInHand, getClickedSlot(hitResult, state));
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    private int getClickedSlot(BlockHitResult hitResult, BlockState blockState) {
        Vec3 hitVec = hitResult.getLocation().subtract(hitResult.getBlockPos().getX(),
                hitResult.getBlockPos().getY(),
                hitResult.getBlockPos().getZ()).add(-0.5, -0.5, -0.5);
        Direction direction = blockState.getValue(FACING);
        double rotation = Math.toRadians(direction.toYRot());
        Vec3 localHitVec = hitVec.yRot((float) rotation);

        double x = localHitVec.x;
        if (x < -0.15) {
            return 0;
        } else if (x < 0.15) {
            return 1;
        } else {
            return 2;
        }
    }

    public static Vec3 getToolRenderPos(int slot, BlockState blockState) { // ツールの描画位置を取得(ブロックの中心が原点)
        Vec3 offset = new Vec3(-0.3, 0.0, -0.3);
        offset = offset.add(0.3 * slot, 0.0, 0.15 * slot);

        Direction direction = blockState.getValue(FACING);
        double rotation = Math.toRadians(direction.toYRot());
        return offset.yRot((float) -rotation);
    }
}
