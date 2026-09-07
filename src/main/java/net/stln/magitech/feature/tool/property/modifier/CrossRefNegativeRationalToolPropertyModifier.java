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
public class CrossRefNegativeRationalToolPropertyModifier extends ValueToolPropertyModifier<Float> implements CrossRefToolPropertyModifier {

    private IToolPropertyGroup reference;
    private float offset;

    public CrossRefNegativeRationalToolPropertyModifier(IToolPropertyGroup property, IToolPropertyGroup reference, float offset, Float value) {
        super(property, value);
        this.reference = reference;
        this.offset = offset;
    }

    // referenceの値の平均をとる
    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        float amount = 0.0F;
        int count = 0;
        for (Map.Entry<IToolProperty<?>, Object> entry : base.getValues().entrySet()) {
            if (!(entry.getKey() instanceof CalculableToolProperty<?> property)) continue;
            if (!reference.contains(property)) continue;
            count++;
            amount += base.getScalar(property);
        }
        amount /= count;
        float applyAmount = Math.max(0, offset - amount) * value;
        target.set(prop, prop.scalarAdd(prop.identity(), applyAmount));
    }

    @Override
    public MutableComponent getDisplayText() {
        int color = propertyCategory.getColor().getRGB();
        MutableComponent component = Component.empty().append(propertyCategory.getDisplayText())
                .append(Component.literal(" +[(" + MathHelper.round(offset, 2) + " - ").withColor(color))
                .append(reference.getDisplayText()).append(Component.literal(") x " + Math.round(value * 100) + "%]").withColor(color));
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
