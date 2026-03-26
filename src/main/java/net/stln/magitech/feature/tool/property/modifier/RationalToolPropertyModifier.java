package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;

// 割合で加算する
public class RationalToolPropertyModifier extends ValueToolPropertyModifier<Float> implements SelfRefToolPropertyModifier {

    public RationalToolPropertyModifier(IToolPropertyGroup property, Float value) {
        super(property, value);
    }

    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        target.set(prop, prop.scalarMul(base.get(prop), value));
    }

    @Override
    public MutableComponent getDisplayText() {
        return propertyCategory.getDisplayText().append("+" + (value * 100) + "%");
    }
}
