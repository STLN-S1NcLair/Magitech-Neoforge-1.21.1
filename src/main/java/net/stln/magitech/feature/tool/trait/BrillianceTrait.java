package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public class BrillianceTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.25F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.CONTINUITY, value));
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.UNIQUE, value));
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light < 10) {
            for (ToolPropertyModifier modifier : list) {
                modifier.setEnabled(false);
            }
        }
        return list;
    }

    @Override
    public void handTick(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, boolean isHost) {
        super.handTick(player, level, stack, traitLevel, properties, isHost);
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light <= 3) {
            EffectHelper.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.85F, 0.8F, 1.0F), new Vector3f(0.85F, 0.8F, 1.0F), 1F, 1, 0F, 15, 1.0F), player, 1);
        }
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        return light <= 3 || light >= 10;
    }

    @Override
    public Color getColor() {
        return new Color(0xD8D0FF);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xF9F0FF);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x8680FF);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("brilliance");
    }
}
