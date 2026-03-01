package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.manager.ManaNetworkManager;

public abstract class ManaConnectableBlock extends Block {

    protected ManaConnectableBlock(Properties properties) {
        super(properties);
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
}
