package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.core.api.mana.flow.network.manager.ManaNetworkManager;

import javax.annotation.Nullable;

public abstract class ManaContainerBlock extends BaseEntityBlock {

    protected ManaContainerBlock(Properties properties) {
        super(properties);
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends ManaContainerBlockEntity> clientType
    ) {
        return level.isClientSide ? createTickerHelper(serverType, clientType, ManaContainerBlockEntity::clientTicker) : createTickerHelper(serverType, clientType, ManaContainerBlockEntity::ticker);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        requestRebuildNetwork(level, pos, false);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        requestRebuildNetwork(level, pos, true);
    }

    public static void requestRebuildNetwork(Level level, BlockPos worldPosition, boolean removal) {
        if (level != null && !level.isClientSide && level instanceof ServerLevel serverLevel) {
            ManaNetworkManager.get(serverLevel).requestRebuild(serverLevel, worldPosition, removal);
        }
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
