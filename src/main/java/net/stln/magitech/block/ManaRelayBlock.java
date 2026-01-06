package net.stln.magitech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.util.VoxelShapeUtil;
import org.jetbrains.annotations.Nullable;

public class ManaRelayBlock extends ManaNodeBlock {
    public static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(4, 3, 4, 12, 11, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );
    public static final VoxelShape SHAPE_DOWN = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.DOWN);
    public static final VoxelShape SHAPE_NORTH = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.NORTH);
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeUtil.rotateShape(SHAPE_UP, Direction.UP, Direction.WEST);

    public ManaRelayBlock(Properties properties) {
        super(properties);
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

    // ★ EntityBlockの実装: BlockEntityを生成する
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    // ★ EntityBlockの実装: Tick処理を紐付ける
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
