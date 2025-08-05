package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.block.block_entity.ZardiusCrucibleBlockEntity;
import net.stln.magitech.util.TickScheduler;

import javax.annotation.Nullable;
import java.util.Random;

public class ZardiusCrucibleBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 14, 4, 4, 16),
            Block.box(0, 4, 14, 14, 16, 16),
            Block.box(0, 0, 0, 2, 4, 4),
            Block.box(0, 0, 12, 2, 4, 14),
            Block.box(0, 4, 0, 2, 16, 14),
            Block.box(12, 0, 0, 16, 4, 2),
            Block.box(2, 0, 0, 4, 4, 2),
            Block.box(2, 4, 0, 16, 16, 2),
            Block.box(12, 0, 14, 14, 4, 16),
            Block.box(14, 0, 2, 16, 4, 4),
            Block.box(14, 4, 2, 16, 16, 16),
            Block.box(14, 0, 12, 16, 4, 16),
            Block.box(2, 2, 2, 14, 4, 14)
    );
    public static final MapCodec<ZardiusCrucibleBlock> CODEC = simpleCodec(ZardiusCrucibleBlock::new);

    public ZardiusCrucibleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false))
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(LIT, Boolean.valueOf(false));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(isOnFire(pState, pLevel, pPos)) {
            addBoilEffect(pState, pLevel, pPos, pRandom);
        }
    }

    public boolean isOnFire(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockState belowBlock = pLevel.getBlockState(pPos.below());
        if (belowBlock.getBlock().equals(Blocks.LAVA)
                || belowBlock.getBlock().equals(Blocks.FIRE)
                || belowBlock.getBlock().equals(Blocks.SOUL_FIRE)) {
            return true;
        } else if (belowBlock.getBlock().equals(Blocks.CAMPFIRE)
                || belowBlock.getBlock().equals(Blocks.SOUL_CAMPFIRE)) {
            if (belowBlock.getValue(BlockStateProperties.LIT)) {
                return true;
            }
        }
        return false;
    }

    private void addBoilEffect(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        ZardiusCrucibleBlockEntity blockEntity = (ZardiusCrucibleBlockEntity) pLevel.getBlockEntity(pPos);
        for (int i = 0; i < 2; i++) {
            if (!blockEntity.fluidTank.isEmpty()) {
                ParticleOptions option;
                float height = (float) blockEntity.getFluidAnim(blockEntity, 0) / blockEntity.fluidTank.getCapacity() * 0.75f + 0.2f;

                double d0 = (double) pPos.getX() + (double) pRandom.nextFloat() * 0.8 + 0.1;
                double d1 = (double) pPos.getY() + height;
                double d2 = (double) pPos.getZ() + (double) pRandom.nextFloat() * 0.8 + 0.1;

                if (blockEntity.fluidTank.getFluid().getFluid().isSame(Fluids.LAVA)) {
                    pLevel.addParticle(ParticleTypes.LAVA, d0, d1, d2,
                            pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D);
                } else {
                    pLevel.addParticle(ParticleTypes.BUBBLE, d0, d1, d2,
                            pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D);
                    TickScheduler.schedule(1, () -> {
                        pLevel.addParticle(ParticleTypes.BUBBLE_POP, d0, d1, d2,
                                pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * 0.005D);
                    }, pLevel.isClientSide);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ZardiusCrucibleBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ZardiusCrucibleBlockEntity pylonBlockEntity) {
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
        if (entity instanceof ZardiusCrucibleBlockEntity zardiusCrucibleBlockEntity) {
            ItemStack itemInHand = player.getItemInHand(hand);
            zardiusCrucibleBlockEntity.addItem(player, itemInHand, 1);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.CONSUME;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pLevel1.isClientSide) {
                pBlockEntity.clientTick(pLevel1, pPos, pState1);
            } else {
                pBlockEntity.serverTick(pLevel1, pPos, pState1);
            }
        });
    }
}
