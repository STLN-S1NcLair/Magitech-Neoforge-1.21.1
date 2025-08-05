package net.stln.magitech.block;

import com.ibm.icu.impl.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.block.block_entity.AthanorPillarBlockEntity;
import net.stln.magitech.particle.particle_option.ManaZapParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.StructureHelper;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Predicate;

public class AthanorPillarBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 4, 14),
            Block.box(0, 10, 0, 16, 16, 16),
            Block.box(4, 4, 4, 12, 10, 12),
            Block.box(2, 4, 6, 4, 6, 10),
            Block.box(12, 4, 6, 14, 6, 10),
            Block.box(0, 8, 0, 4, 10, 4),
            Block.box(0, 8, 12, 4, 10, 16),
            Block.box(12, 8, 0, 16, 10, 4),
            Block.box(12, 8, 12, 16, 10, 16),
            Block.box(6, 4, 2, 10, 6, 4),
            Block.box(6, 4, 12, 10, 6, 14)
    );
    public static final MapCodec<AthanorPillarBlock> CODEC = simpleCodec(AthanorPillarBlock::new);

    public AthanorPillarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false))
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        return new AthanorPillarBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.ATHANOR_PILLAR_ENTITY.get());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AthanorPillarBlockEntity pylonBlockEntity) {
                pylonBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AthanorPillarBlockEntity pylonBlockEntity) {
            if (pylonBlockEntity.inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
                pylonBlockEntity.inventory.insertItem(0, stack.copy(), false);
                stack.shrink(1);
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 2f);
            } else if (stack.isEmpty()) {
                ItemStack stackOnPedestal = pylonBlockEntity.inventory.extractItem(0, 1, false);
                player.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                pylonBlockEntity.clearContents();
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            } else {
                ItemStack stackOnPedestal = pylonBlockEntity.inventory.extractItem(0, 1, false);
                if (stack.getCount() == 1) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                } else {
                    player.addItem(stackOnPedestal);
                }
                pylonBlockEntity.clearContents();
                pylonBlockEntity.inventory.insertItem(0, stack.copy(), false);
                stack.shrink(1);
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AthanorPillarBlockEntity> clientType
    ) {
        return level.isClientSide ? createTickerHelper(serverType, clientType, AthanorPillarBlockEntity::clientTick) : createTickerHelper(serverType, clientType, AthanorPillarBlockEntity::serverTick);
    }
}
