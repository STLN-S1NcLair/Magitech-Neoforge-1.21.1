package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverchargedTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.25F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.ELEMENT, value));
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.HANDLING, value));
        if (!effectEnabled(player, level, stack, traitLevel, properties)) {
            for (ToolPropertyModifier modifier : list) {
                modifier.setEnabled(false);
            }
        }
        return list;
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return EntityManaHelper.getMagicManaFillRatio(player) >= 0.5;
    }

    @Override
    public Color getColor() {
        return new Color(0x80FFC0);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xA0FFA0);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x00F0D0);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.overcharged");
    }
}
