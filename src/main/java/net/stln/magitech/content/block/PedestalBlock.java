package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.content.block.block_entity.IPedestalBlockEntity;
import net.stln.magitech.content.block.block_entity.PedestalBlockEntity;

import javax.annotation.Nullable;

public abstract class PedestalBlock extends BaseEntityBlock implements IPedestalBlock {

    public PedestalBlock(Properties properties) {
        super(properties);
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
}
