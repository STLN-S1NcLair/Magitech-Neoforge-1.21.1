package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.feature.tool.material.MiningLevel;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public class MiningLevelToolProperty extends ToolProperty<MiningLevel> implements CalculableToolProperty<MiningLevel> {

    public MiningLevelToolProperty(Color color) {
        super(color);
    }

    // 最高Tierを返す
    @Override
    public MiningLevel add(MiningLevel a, MiningLevel b) {
        return a.getTier() > b.getTier() ? a : b;
    }

    @Override
    public MiningLevel mul(MiningLevel a, MiningLevel b) {
        return a.getTier() > b.getTier() ? a : b;
    }

    @Override
    public MiningLevel scalarAdd(MiningLevel a, float b) {
        return a;
    }

    @Override
    public MiningLevel scalarMul(MiningLevel a, float b) {
        return a;
    }

    @Override
    public float scalarValue(MiningLevel a) {
        return a.getTier();
    }

    @Override
    public MiningLevel identity() {
        return MiningLevel.NONE;
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        MiningLevel miningLevel = properties.get(this);
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(miningLevel.getDisplayName())
                        .withColor(miningLevel.getColor()));
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        MiningLevel miningLevel = properties.get(this);
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(miningLevel.getDisplayName())
                .withColor(miningLevel.getColor()));
    }
}
