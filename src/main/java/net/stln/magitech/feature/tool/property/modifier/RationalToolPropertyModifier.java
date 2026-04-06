package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;

// 割合で加算する
public class RationalToolPropertyModifier extends ValueToolPropertyModifier<Float> implements SelfRefToolPropertyModifier {

    public RationalToolPropertyModifier(IToolPropertyGroup property, Float value) {
        super(property, value);
    }

    protected <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target) {
        target.set(prop, prop.scalarMul(base.getOrId(prop), value));
    }

    @Override
    public MutableComponent getDisplayText() {
        int value = Math.round(this.value * 100);
        MutableComponent component = Component.empty().append(propertyCategory.getDisplayText())
                .append(Component.literal(" " + (value >= 0 ? "+" : "") + value + "%").withColor(propertyCategory.getColor().getRGB()));
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
