package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;

public abstract class Trait {

    public ToolStats modifyStats(ItemStack stack, int traitLevel) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStatsConditional(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
    }

    public Boolean isCorrectTool(ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState) {
        return null;
    }

    public float modifyMiningSpeed(ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState) {
        return 0;
    }

    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount) {
    }

    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
    }

    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
    }

    public void onRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, int repairAmount) {
    }

    public int getColor() {
        return 0xFFFFFF;
    }

    public Component getName() {
        return Component.empty();
    }

    public MutableComponent getComponent() {
        return getName().copy().withColor(getColor());
    }

    public int getMaxLevel() {
        return -1;
    }
}
