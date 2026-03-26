package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import org.joml.Vector3f;

import java.awt.*;

public class InclusionTrait extends Trait {

    @Override
    public int addEnchantments(ItemStack stack, int traitLevel, ToolProperties properties, Holder<Enchantment> enchantmentHolder, int enchantmentLevel) {
        if (enchantmentHolder.is(Enchantments.FORTUNE) || enchantmentHolder.is(Enchantments.LOOTING)) {
            return traitLevel;
        }
        return super.addEnchantments(stack, traitLevel, properties, enchantmentHolder, enchantmentLevel);
    }

    @Override
    public Color getColor() {
        return new Color(0x2040A0);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.inclusion");
    }

}
