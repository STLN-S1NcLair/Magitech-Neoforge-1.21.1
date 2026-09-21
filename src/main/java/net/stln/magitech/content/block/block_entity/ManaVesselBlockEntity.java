package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import org.jetbrains.annotations.NotNull;

public class ManaVesselBlockEntity extends AbstractManaVesselBlockEntity {

    public ManaVesselBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_VESSEL_ENTITY.get(), pos, blockState, mana);
    }

    public ManaVesselBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.magitech.mana_vessel");
    }
}
