package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.CrossRefRationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalysisTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        float value = 0.3F * traitLevel;
        ToolPropertyModifier mod1 = new CrossRefRationalToolPropertyModifier(ToolPropertyCategory.ATTACK, ToolPropertyCategory.ELEMENT, value);
        ToolPropertyModifier mod2 = new CrossRefRationalToolPropertyModifier(ToolPropertyCategory.CONTINUITY, ToolPropertyCategory.ELEMENT, value);
        return List.of(mod1, mod2);
    }

    @Override
    public Color getColor() {
        return new Color(0xF0B020);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.catalysis");
    }
}
