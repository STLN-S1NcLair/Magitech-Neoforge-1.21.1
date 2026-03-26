package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.property.SingleToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.ComponentHelper;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AdaptationTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
        Set<ToolMaterial> materialSet = new HashSet<>(materials);
        float value = (float) ((materialSet.size() - 1) * 0.2F);
        ToolPropertyModifier mod = new RationalToolPropertyModifier(new SingleToolPropertyGroup(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT.get()), value);
        return List.of(mod);
    }

    @Override
    public Color getColor() {
        return new Color(0x805830);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.adaptation");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
