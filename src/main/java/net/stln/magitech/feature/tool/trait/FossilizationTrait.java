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

public class FossilizationTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.5F * traitLevel;
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
        return player.getHealth() < player.getMaxHealth() / 2;
    }

    @Override
    public Color getColor() {
        return new Color(0xF0FFC0);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFCFFDC);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xDFFFBA);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.fossilization");
    }
}
