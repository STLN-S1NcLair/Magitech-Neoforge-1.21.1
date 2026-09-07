package net.stln.magitech.feature.tool.trait;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.List;

public class InsomniaTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.20F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.RANGE, value));
        if (!effectEnabled(player, level, stack, traitLevel, properties)) {
            for (ToolPropertyModifier modifier : list) {
                modifier.setEnabled(false);
            }
        }
        return list;
    }

    @Override
    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, List<ItemAttributeModifiers.Entry> entries) {
        entries.add(new ItemAttributeModifiers.Entry(Attributes.GRAVITY, new AttributeModifier(Magitech.id("insomnia_gravity"), -0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND));
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return isInsomniaTime(player);
    }

    private boolean isInsomniaTime(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            int ticks = serverPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            return ticks > 72000; // 1 hour in ticks
        } else if (player instanceof LocalPlayer localPlayer) {
            int ticks = localPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            return ticks > 72000; // 1 hour in ticks
        }
        return false;
    }

    @Override
    public Color getColor() {
        return new Color(0XD2D69E);
    }

    @Override
    public Color getPrimary() {
        return new Color(0XFBE4FF);
    }

    @Override
    public Color getSecondary() {
        return new Color(0XFFED92);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("insomnia");
    }
}
