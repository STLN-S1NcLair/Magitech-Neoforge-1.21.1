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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuranceTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        ToolPropertyModifier mod = new RationalToolPropertyModifier(ToolPropertyCategory.DURATION, 0.5F * traitLevel);
        return List.of(mod);
    }

    @Override
    public Color getColor() {
        return new Color(0xC0C0C0);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.durance");
    }
}
