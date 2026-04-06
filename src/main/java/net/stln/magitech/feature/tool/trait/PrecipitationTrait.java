package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.BlockHelper;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class PrecipitationTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.25F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.ATTACK, value));
        if (!effectEnabled(player, level, stack, traitLevel, properties)) {
            for (ToolPropertyModifier modifier : list) {
                modifier.setEnabled(false);
            }
        }
        return list;
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return player.isInWater() || level.isRainingAt(player.blockPosition());
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos) {
        return isTallEnough(level, blockState, pos) ? 0.2F : super.modifyMiningSpeed(player, level, stack, traitLevel, properties, blockState, pos);
    }

    @Override
    public Set<BlockPos> additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        boolean isTallEnough = isTallEnough(level, blockState, pos);
        if (isTallEnough) {
            return BlockHelper.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel * 15);
        }
        return super.additionalBlockBreak(player, level, stack, traitLevel, properties, blockState, pos, damageAmount, direction);
    }

    private static boolean isTallEnough(Level level, BlockState blockState, BlockPos pos) {
        boolean flag = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        for (int i = 0; i < 4; i++) {
            flag &= level.getBlockState(pos.above(i)).getBlock().equals(blockState.getBlock());
            flag2 &= level.getBlockState(pos.above(i - 1)).getBlock().equals(blockState.getBlock());
            flag3 &= level.getBlockState(pos.above(i - 2)).getBlock().equals(blockState.getBlock());
            flag4 &= level.getBlockState(pos.above(i - 3)).getBlock().equals(blockState.getBlock());
        }
        boolean isTallEnough = flag || flag2 || flag3 || flag4;
        return isTallEnough;
    }

    @Override
    public Color getColor() {
        return new Color(0xE0B090);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xF8E2AF);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xC2857A);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("precipitation");
    }

}
