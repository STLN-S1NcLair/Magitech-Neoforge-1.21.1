package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShatterTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        float value = traitLevel * 0.25F * stack.getDamageValue() / stack.getMaxDamage();
        ToolPropertyModifier mod1 = new RationalToolPropertyModifier(ToolPropertyCategory.CONTINUITY, value);
        ToolPropertyModifier mod2 = new RationalToolPropertyModifier(ToolPropertyCategory.UNIQUE, value);
        return List.of(mod1, mod2);
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return stack.isDamaged();
    }

    @Override
    public Color getColor() {
        return new Color(0xF0A0FF);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFFC9FC);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x9B63FF);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.shatter");
    }
}
