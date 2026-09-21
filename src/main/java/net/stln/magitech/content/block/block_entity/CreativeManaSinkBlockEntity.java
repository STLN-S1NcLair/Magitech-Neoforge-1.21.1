package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaVesselBlock;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import org.jetbrains.annotations.NotNull;

public class CreativeManaSinkBlockEntity extends AbstractManaVesselBlockEntity {

    public CreativeManaSinkBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.CREATIVE_MANA_SINK_ENTITY.get(), pos, blockState, mana);
    }

    public CreativeManaSinkBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        this.getManaHandler(null).setMana(0);
    }

    @Override
    public void setMana(long mana) {
        this.mana = 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side.getAxis() == state.getValue(ManaVesselBlock.AXIS)) {
            return ManaFlowRule.bothWays(0.0F);
        }
        return ManaFlowRule.none();
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.magitech.creative_mana_sink");
    }
}
