package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.MathHelper;

import java.util.Map;

// referenceの値を参照してtargetの値に加算する
public class RationalConversionToolPropertyModifier extends ValueToolPropertyModifier<Float> implements CrossRefToolPropertyModifier {

    private IToolPropertyGroup reference;
    private float rate;

    // rate: 参照元の値に対して、どのくらいの割合で加算するかを指定する
    public RationalConversionToolPropertyModifier(IToolPropertyGroup property, IToolPropertyGroup reference, Float value, float rate) {
        super(property, value);
        this.reference = reference;
        this.rate = rate;
    }

    // referenceの値の平均をとる
    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        float amount = 0.0F;
        int count = 0;
        for (Map.Entry<IToolProperty<?>, Object> entry : base.getValues().entrySet()) {
            if (!(entry.getKey() instanceof CalculableToolProperty<?> property)) continue;
            if (!reference.contains(property)) continue;
            count++;
            float scalar = base.getScalar(property);
            amount += scalar;
            setScalarAdd(target, property, -scalar * value);
        }
        amount /= count;
        float conversionValue = amount * value * rate;
        target.set(prop, prop.scalarAdd(prop.identity(), conversionValue));
    }

    private static <I> void setScalarAdd(ToolProperties target, CalculableToolProperty<I> property, float amount) {
        target.set(property, property.scalarAdd(property.identity(), amount));
    }

    @Override
    public MutableComponent getDisplayText() {
        int color = propertyCategory.getColor().getRGB();
        MutableComponent component = Component.empty().append(propertyCategory.getDisplayText())
                .append(Component.literal(" <- ").withColor(color))
                .append(reference.getDisplayText()).append(Component.literal(" " + Math.round(value * 100) + "% ").withColor(reference.getColor().getRGB())).append(Component.literal("x " + MathHelper.round(rate, 2)).withColor(color));
        if (!enabled) {
            component = component.withColor(0x808080).withStyle(ChatFormatting.STRIKETHROUGH);
            for (Component cp : component.getSiblings()) {
                if (cp instanceof MutableComponent mutable) {
                    mutable = mutable.withColor(0x808080).withStyle(ChatFormatting.STRIKETHROUGH);
                }
            }
        }
        return component;
    }
}
