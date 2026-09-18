package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.ThermalManaFurnaceBlockEntity;
import net.stln.magitech.helper.MachineInteractionHelper;
import net.stln.magitech.helper.MachinePlacementHelper;
import org.jetbrains.annotations.Nullable;

/**
 * 3ブロック高の熱式マナ炉ブロックです。
 * A three-block-tall thermal mana furnace block.
 */
public class ThermalManaFurnaceBlock extends ManaContainerBlock {
    public static final MapCodec<ThermalManaFurnaceBlock> CODEC = simpleCodec(ThermalManaFurnaceBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.box(0, 4, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(1, 2, 1, 15, 4, 15)
    );
    private static final VoxelShape MIDDLE_SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 8, 14),
            Block.box(4, 8, 4, 12, 16, 12),
            Block.box(4, 0, 0, 12, 4, 2),
            Block.box(14, 0, 4, 16, 4, 12),
            Block.box(4, 0, 14, 12, 4, 16),
            Block.box(0, 0, 4, 2, 4, 12),
            Block.box(2, 8, 10, 6, 12, 14),
            Block.box(2, 8, 2, 6, 12, 6),
            Block.box(10, 8, 2, 14, 12, 6),
            Block.box(10, 8, 10, 14, 12, 14)
    );
    private static final VoxelShape UPPER_SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 6, 12),
            Block.box(3, 6, 3, 13, 8, 13)
    );

    protected ThermalManaFurnaceBlock(Properties properties, long maxMana, long maxFlow) {
        super(properties, maxMana, maxFlow);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, Part.LOWER)
                .setValue(LIT, false));
    }

    protected ThermalManaFurnaceBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (pos.getY() > level.getMaxBuildHeight() - 3) {
            return null;
        }

        BlockState lower = defaultBlockState()
                .setValue(FACING, MachinePlacementHelper.getFacing(context, context.getHorizontalDirection()))
                .setValue(PART, Part.LOWER);
        BlockState middle = lower.setValue(PART, Part.MIDDLE);
        BlockState upper = lower.setValue(PART, Part.UPPER);
        if (!canPlacePart(level, pos, lower, context)
                || !canPlacePart(level, pos.above(), middle, context)
                || !canPlacePart(level, pos.above(2), upper, context)) {
            return null;
        }
        return lower;
    }

    private boolean canPlacePart(Level level, BlockPos pos, BlockState state, BlockPlaceContext context) {
        return level.getBlockState(pos).canBeReplaced(context)
                && level.isUnobstructed(state, pos, CollisionContext.of(context.getPlayer()));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (state.getValue(PART) == Part.LOWER) {
            level.setBlock(pos.above(), state.setValue(PART, Part.MIDDLE), Block.UPDATE_ALL);
            level.setBlock(pos.above(2), state.setValue(PART, Part.UPPER), Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        BlockState below = level.getBlockState(pos.below());
        return switch (state.getValue(PART)) {
            case LOWER -> (above.canBeReplaced() && level.getBlockState(pos.above(2)).canBeReplaced())
                    || (above.is(this) && above.getValue(PART) == Part.MIDDLE
                    && level.getBlockState(pos.above(2)).is(this)
                    && level.getBlockState(pos.above(2)).getValue(PART) == Part.UPPER);
            case MIDDLE -> below.is(this)
                    && below.getValue(PART) == Part.LOWER
                    && below.getValue(FACING) == state.getValue(FACING)
                    && (above.canBeReplaced()
                    || (above.is(this) && above.getValue(PART) == Part.UPPER
                    && above.getValue(FACING) == state.getValue(FACING)));
            case UPPER -> below.is(this)
                    && below.getValue(PART) == Part.MIDDLE
                    && below.getValue(FACING) == state.getValue(FACING);
        };
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
        Part part = state.getValue(PART);
        if (direction == Direction.UP && part != Part.UPPER) {
            Part expected = part == Part.LOWER ? Part.MIDDLE : Part.UPPER;
            return neighborState.is(this) && neighborState.getValue(PART) == expected
                    ? state
                    : Blocks.AIR.defaultBlockState();
        }
        if (direction == Direction.DOWN && part != Part.LOWER) {
            Part expected = part == Part.UPPER ? Part.MIDDLE : Part.LOWER;
            return neighborState.is(this) && neighborState.getValue(PART) == expected
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
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) != Part.LOWER) {
            BlockPos lowerPos = lowerPos(pos, state);
            BlockState lowerState = level.getBlockState(lowerPos);
            if (lowerState.is(this) && lowerState.getValue(PART) == Part.LOWER) {
                if (level.getBlockEntity(lowerPos) instanceof ThermalManaFurnaceBlockEntity furnace) {
                    furnace.drops();
                }
                removeAllParts(level, lowerPos);
                level.levelEvent(player, 2001, pos, Block.getId(state));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private BlockPos lowerPos(BlockPos pos, BlockState state) {
        return switch (state.getValue(PART)) {
            case LOWER -> pos;
            case MIDDLE -> pos.below();
            case UPPER -> pos.below(2);
        };
    }

    private void removeAllParts(Level level, BlockPos lowerPos) {
        for (int offset = 0; offset < 3; offset++) {
            BlockPos partPos = lowerPos.above(offset);
            if (level.getBlockState(partPos).is(this)) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ThermalManaFurnaceBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (level.getBlockEntity(pos) instanceof ThermalManaFurnaceBlockEntity furnace) {
            return MachineInteractionHelper.interact(stack, level, pos, player, hand, furnace);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (state.getValue(PART) != Part.LOWER) {
            return null;
        }
        return createTicker(level, blockEntityType, BlockInit.THERMAL_MANA_FURNACE_ENTITY.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(PART)) {
            case LOWER -> LOWER_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case UPPER -> UPPER_SHAPE;
        };
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock() && state.getValue(PART) == Part.LOWER) {
            if (level.getBlockEntity(pos) instanceof ThermalManaFurnaceBlockEntity furnace) {
                furnace.drops();
            }
            removeAllParts(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public enum Part implements StringRepresentable {
        LOWER("lower"),
        MIDDLE("middle"),
        UPPER("upper");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
