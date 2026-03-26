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

public class FragileTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> mods = new ArrayList<>();
        float mul = traitLevel * 0.3F;
        float div = 0.5F / traitLevel - 1.0F;
        for (ToolPropertyCategory category : ToolPropertyCategory.values()) {
            mods.add(new RationalToolPropertyModifier(category, category == ToolPropertyCategory.DURATION ? div : mul));
        }

        return super.modifyProperty(player, level, stack, traitLevel, properties);
    }

    @Override
    public Color getColor() {
        return new Color(0xC0E8FF);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.fragile");
    }
}
