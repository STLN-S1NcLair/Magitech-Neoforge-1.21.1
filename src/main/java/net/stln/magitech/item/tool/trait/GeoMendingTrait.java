package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;

import java.util.function.Predicate;

public class GeoMendingTrait extends Trait {

    @Override
    public Boolean isCorrectTool(ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState) {
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD))) {
            return true;
        }
        return super.isCorrectTool(stack, traitLevel, stats, blockState);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD)) && damageAmount > 0 && stack.getDamageValue() < stack.getMaxDamage()) {
            if (player.getRandom().nextFloat() < traitLevel * 0.22F) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

    @Override
    public int getColor() {
        return 0x808080;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.geomending");
    }
}
