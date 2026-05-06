package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;

import javax.annotation.Nullable;

public class ManaVesselBlock extends AbstractManaVesselBlock {
    public static final MapCodec<ManaVesselBlock> CODEC = simpleCodec(ManaVesselBlock::new);

    protected ManaVesselBlock(Properties properties, int maxMana, int maxFlow) {
        super(properties, maxMana, maxFlow);
    }

    protected ManaVesselBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaVesselBlockEntity(blockPos, blockState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.MANA_VESSEL_ENTITY.get());
    }
}
