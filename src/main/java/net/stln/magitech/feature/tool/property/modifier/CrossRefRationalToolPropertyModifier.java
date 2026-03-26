package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.*;

import java.util.Map;

// referenceの値を参照してtargetの値に加算する
public class CrossRefRationalToolPropertyModifier extends RationalToolPropertyModifier implements CrossRefToolPropertyModifier {

    private IToolPropertyGroup reference;

    public CrossRefRationalToolPropertyModifier(IToolPropertyGroup property, IToolPropertyGroup reference, Float value) {
        super(property, value);
        this.reference = reference;
    }

    // referenceの値の平均をとる
    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        float amount = 0.0F;
        int count = 0;
        for (Map.Entry<IToolProperty<?>, Object> entry : base.getValues().entrySet()) {
            if (!(entry.getKey() instanceof CalculableToolProperty<?> property)) continue;
            if (!reference.contains(property)) continue;
            count++;
            amount = base.getScalar(property);
        }
        amount /= count;
        target.set(prop, prop.scalarAdd(base.get(prop), amount * value));
    }

    @Override
    public MutableComponent getDisplayText() {
        return super.getDisplayText();
    }
}
