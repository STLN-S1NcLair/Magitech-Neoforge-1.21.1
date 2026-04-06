package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.awt.*;
import java.util.List;

public class SmoothTrait extends Trait {

    @Override
    public void modifyEnchantmentOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack) {
        Holder<Enchantment> silkTouch = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH);
        stack.enchant(silkTouch, 1);
        super.modifyEnchantmentOnBlockLooting(player, level, stack, traitLevel, properties, blockState, pos, lootStack);
    }

    @Override
    public double modifyExpOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack, int exp) {
        return 0.0; // No experience gained when the trait is active
    }

    @Override
    public Color getColor() {
        return new Color(0xFFF8E0);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("smooth");
    }

}
