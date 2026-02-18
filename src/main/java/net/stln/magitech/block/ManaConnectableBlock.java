package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.network.manager.ManaNetworkManager;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;

import javax.annotation.Nullable;

public abstract class ManaConnectableBlock extends Block {

    protected ManaConnectableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        requestRebuildNetwork(level, pos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        requestRebuildNetwork(level, pos);
    }

    private void requestRebuildNetwork(Level level, BlockPos worldPosition) {
        if (level != null && !level.isClientSide && level instanceof ServerLevel serverLevel) {
            ManaNetworkManager.get(serverLevel).requestRebuild(serverLevel, worldPosition);
        }
    }
}
