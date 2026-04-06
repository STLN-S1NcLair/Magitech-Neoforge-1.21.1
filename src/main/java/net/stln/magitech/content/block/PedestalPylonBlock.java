package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.content.block.block_entity.PedestalBlockEntity;
import net.stln.magitech.content.block.block_entity.PedestalPylonBlockEntity;

import javax.annotation.Nullable;

public class PedestalPylonBlock extends PedestalBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(3, 0, 3, 13, 3, 13),
            Block.box(5, 3, 5, 11, 9, 11),
            Block.box(2, 9, 2, 14, 13, 14)
    );
    public static final MapCodec<PedestalPylonBlock> CODEC = simpleCodec(PedestalPylonBlock::new);

    public PedestalPylonBlock(Properties properties) {
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

    @Override
    protected BlockEntityType<? extends PedestalBlockEntity> getBEType() {
        return BlockInit.PEDESTAL_PYLON_ENTITY.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PedestalPylonBlockEntity(blockPos, blockState);
    }
}
