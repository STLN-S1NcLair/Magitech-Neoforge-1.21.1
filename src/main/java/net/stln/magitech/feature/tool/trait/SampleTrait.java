package net.stln.magitech.feature.tool.trait;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.property.SingleToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.ComponentHelper;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SampleTrait extends Trait {

    @Override
    public Color getColor() {
        return new Color(0xCCCBDA);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("sample");
    }
}
