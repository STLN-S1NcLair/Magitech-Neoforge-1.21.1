package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashSet;
import java.util.Set;

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

    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        return 0;
    }

    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        if (!isInitial) {
            SoundType soundType = level.getBlockState(pos).getSoundType(level, pos, player);
            level.playSound(player, pos, soundType.getBreakSound(), SoundSource.PLAYERS, soundType.getVolume(), soundType.getPitch());
        }
    }

    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return false;
    }

    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {

    }

    public Set<BlockPos> addAdditionalBlockBreakFirst(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        return posSet;
    }

    public Set<BlockPos> addAdditionalBlockBreakSecond(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        return posSet;
    }

    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
    }

    public InteractionResult use(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand) {
        return InteractionResult.PASS;
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
