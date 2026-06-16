package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.stln.magitech.content.block.block_entity.PedestalBlockEntity;

import javax.annotation.Nullable;

public abstract class PedestalBlock extends BaseEntityBlock implements IPedestalBlock, SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PedestalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false))
        );
    }

    /* BLOCK ENTITY */

    protected abstract BlockEntityType<? extends PedestalBlockEntity> getBEType();

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, getBEType());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity blockEntity) {
                blockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return interact(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends PedestalBlockEntity> clientType
    ) {
        return createTickerHelper(serverType, clientType, (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pLevel1.isClientSide) {
                pBlockEntity.clientTick(pLevel1, pPos, pState1, pBlockEntity);
            } else {
                pBlockEntity.serverTick(pLevel1, pPos, pState1, pBlockEntity);
            }
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        boolean water = WaterloggedBlockUtil.isWaterAtPlacement(context);
        return this.defaultBlockState().setValue(WATERLOGGED, water);
    }

    @Override
    protected BlockState updateShape(
            BlockState state, net.minecraft.core.Direction direction, BlockState neighborState,
            net.minecraft.world.level.LevelAccessor level, net.minecraft.core.BlockPos pos, net.minecraft.core.BlockPos neighborPos
    ) {
        WaterloggedBlockUtil.scheduleWaterTickIfNeeded(state, WATERLOGGED, level, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected net.minecraft.world.level.material.FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? net.minecraft.world.level.material.Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
