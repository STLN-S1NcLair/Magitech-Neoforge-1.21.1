package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.CalculableToolProperty;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.IToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.util.Map;

public abstract class ValueToolPropertyModifier<T> implements ToolPropertyModifier {

    protected IToolPropertyGroup propertyCategory;
    protected T value;
    protected boolean enabled = true;

    public ValueToolPropertyModifier(IToolPropertyGroup propertyCategory, T value) {
        this.propertyCategory = propertyCategory;
        this.value = value;
    }

    @Override
    public ToolProperties apply(ToolProperties base) {
        ToolProperties properties = new ToolProperties(base.getCategory());
        if (enabled) {
            for (Map.Entry<IToolProperty<?>, Object> entry : base.getValues().entrySet()) {
                if (!(entry.getKey() instanceof CalculableToolProperty<?> prop)) continue;
                if (!propertyCategory.contains(prop)) continue;
                applyValue(base, prop, properties);
            }
        }
        return properties;
    }

    protected abstract <I> void applyValue(ToolProperties base, CalculableToolProperty<I> prop, ToolProperties target);

    @Override
    public ToolPropertyModifier setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public MutableComponent getDisplayText() {
        String value = this.value.toString();
        MutableComponent component = Component.empty().append(propertyCategory.getDisplayText())
                .append(Component.literal(" +" + value).withColor(propertyCategory.getColor().getRGB()));
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
