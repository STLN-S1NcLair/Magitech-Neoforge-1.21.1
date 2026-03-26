package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureDifferenceTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> mods = new ArrayList<>();
        float mul = traitLevel * 0.25F;
        if (level.isDay()) {
            mods.add(new RationalToolPropertyModifier(ToolPropertyCategory.ATTACK, mul));
        } else {
            mods.add(new RationalToolPropertyModifier(ToolPropertyCategory.HANDLING, mul));
        }
        return super.modifyProperty(player, level, stack, traitLevel, properties);
    }

    @Override
    public Color getColor() {
        return new Color(0xFFFFC0);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.temperature_difference");
    }
}