package net.stln.magitech.feature.tool.trait;

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
import java.util.List;

public class SturdyTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.5F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.DEFENSE, value));
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
    public void testRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, int repairAmount) {
        stack.setDamageValue(stack.getDamageValue() - repairAmount / 2 * traitLevel);
    }

    @Override
    public Color getColor() {
        return new Color(0xDE7F4E);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFFD8A7);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xDB7C5E);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("sturdy");
    }
}
