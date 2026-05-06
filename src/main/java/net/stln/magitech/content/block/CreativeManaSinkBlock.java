package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.block_entity.CreativeManaSinkBlockEntity;
import net.stln.magitech.content.block.block_entity.CreativeManaSourceBlockEntity;

import javax.annotation.Nullable;

public class CreativeManaSinkBlock extends AbstractManaVesselBlock {
    public static final MapCodec<CreativeManaSinkBlock> CODEC = simpleCodec(CreativeManaSinkBlock::new);

    protected CreativeManaSinkBlock(Properties properties) {
        super(properties, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CreativeManaSinkBlockEntity(blockPos, blockState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.CREATIVE_MANA_SINK_ENTITY.get());
    }
}
