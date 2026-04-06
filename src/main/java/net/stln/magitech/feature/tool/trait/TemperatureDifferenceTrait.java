package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureDifferenceTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float mul = traitLevel * 0.25F;
        RationalToolPropertyModifier mod1 = new RationalToolPropertyModifier(ToolPropertyCategory.ATTACK, mul);
        RationalToolPropertyModifier mod2 = new RationalToolPropertyModifier(ToolPropertyCategory.HANDLING, mul);
        list.add(mod1);
        list.add(mod2);
        if (level.isDay()) {
            mod2.setEnabled(false);
        } else {
            mod1.setEnabled(false);
        }
        return list;
    }

    @Override
    public Color getColor() {
        return new Color(0xFFFFC0);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("temperature_difference");
    }
}