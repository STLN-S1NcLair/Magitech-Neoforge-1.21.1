package net.stln.magitech.feature.tool.property.modifier;

import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;

// 定数値を加算する
public class ConstantToolPropertyModifier<T> extends ValueToolPropertyModifier<Float> implements SelfRefToolPropertyModifier {

    public ConstantToolPropertyModifier(IToolPropertyGroup property, float value) {
        super(property, value);
    }

    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        target.set(prop, prop.scalarAdd(base.getOrId(prop), value));
    }
}
