package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;

public class ManaPumpBlockEntity extends AbstractManaPumpBlockEntity {

    public ManaPumpBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_PUMP_ENTITY.get(), pos, blockState, mana);
    }

    public ManaPumpBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_pump");
    }
}
