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
import java.util.List;

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
    public ResourceLocation getKey() {
        return Magitech.id("shatter");
    }
}
