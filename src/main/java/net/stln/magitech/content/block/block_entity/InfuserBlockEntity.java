package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;

public class InfuserBlockEntity extends InfusionAltarBlockEntity {

    public InfuserBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.INFUSER_ENTITY.get(), pos, blockState, mana);
        this.maxProgress = 100;
    }

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.infuser");
    }
}
