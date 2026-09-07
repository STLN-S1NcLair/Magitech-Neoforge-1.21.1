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

public class PlasticTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        ToolPropertyModifier mod = new RationalToolPropertyModifier(ToolPropertyCategory.UNIQUE, 0.2F * traitLevel);
        return List.of(mod);
    }

    @Override
    public Color getColor() {
        return new Color(0xFFAF26);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("plastic");
    }
}
