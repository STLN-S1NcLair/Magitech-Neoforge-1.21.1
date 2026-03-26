package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Component getName() {
        return Component.translatable("trait.magitech.smooth");
    }

}
