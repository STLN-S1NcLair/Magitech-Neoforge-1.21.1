package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.util.Map;

// referenceの値を参照してtargetの値に加算する
public class CrossRefRationalToolPropertyModifier extends ValueToolPropertyModifier<Float> implements CrossRefToolPropertyModifier {

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
        target.set(prop, prop.scalarAdd(prop.identity(), amount * value));
    }

    @Override
    public MutableComponent getDisplayText() {
        int color = propertyCategory.getColor().getRGB();
        MutableComponent component = Component.empty().append(propertyCategory.getDisplayText())
                .append(Component.literal(" +[").withColor(color))
                .append(reference.getDisplayText()).append(Component.literal(" x " + Math.round(value * 100) + "%]").withColor(color));
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
