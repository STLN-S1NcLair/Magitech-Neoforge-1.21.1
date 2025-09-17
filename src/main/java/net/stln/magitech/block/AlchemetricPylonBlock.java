package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.stln.magitech.block.block_entity.AlchemetricPylonBlockEntity;

import javax.annotation.Nullable;

public class AlchemetricPylonBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 2, 14),
            Block.box(3, 2, 7, 13, 10, 9),
            Block.box(7, 2, 3, 9, 10, 13),
            Block.box(4, 2, 4, 12, 10, 12),
            Block.box(0, 10, 0, 16, 14, 16)
    );
    public static final MapCodec<AlchemetricPylonBlock> CODEC = simpleCodec(AlchemetricPylonBlock::new);

    public AlchemetricPylonBlock(Properties properties) {
        super(properties);
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
        return new AlchemetricPylonBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.ALCHEMETRIC_PYLON_ENTITY.get());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AlchemetricPylonBlockEntity pylonBlockEntity) {
                pylonBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AlchemetricPylonBlockEntity pylonBlockEntity) {
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
    protected <T extends BlockEntity> BlockEntityTicker<T> createTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AlchemetricPylonBlockEntity> clientType
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
