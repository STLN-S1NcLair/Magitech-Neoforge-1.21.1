package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.stln.magitech.content.block.AbstractManaPumpBlock;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractManaPumpBlockEntity extends ManaContainerBlockEntity {

    public AbstractManaPumpBlockEntity(BlockEntityType<? extends AbstractManaPumpBlockEntity> type, BlockPos pos, BlockState blockState, long mana) {
        super(type, pos, blockState, mana);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    protected abstract @NotNull Component getDefaultName();

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
    }

    public int getContainerSize() {
        return 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || state.getValue(BlockStateProperties.POWERED)) {
            return ManaFlowRule.bothWays(0.0F);
        }
        if (side == state.getValue(AbstractManaPumpBlock.FACING)) {
            return ManaFlowRule.extractOnly(1.0F);
        }
        if (side == state.getValue(AbstractManaPumpBlock.FACING).getOpposite()) {
            return ManaFlowRule.insertOnly(-1.0F);
        }
        return ManaFlowRule.none();
    }
}
