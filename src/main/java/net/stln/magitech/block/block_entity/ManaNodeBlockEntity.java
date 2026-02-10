package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.IManaNode;
import net.stln.magitech.api.mana.flow.ManaNodeHelper;
import net.stln.magitech.block.BlockInit;

public class ManaNodeBlockEntity extends BlockEntity implements IManaNode {

    private final ManaNodeHelper nodeLogic;

    public ManaNodeBlockEntity(BlockEntityType type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.nodeLogic = new ManaNodeHelper(this, null);
    }

    public ManaNodeBlockEntity(BlockPos pos, BlockState blockState) {
        this(BlockInit.MANA_NODE_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaNodeBlockEntity entity) {
        entity.nodeLogic.tick(level, pos, state);
    }

    @Override
    public void requestRescan() {
        this.nodeLogic.requestRescan();
    }
}