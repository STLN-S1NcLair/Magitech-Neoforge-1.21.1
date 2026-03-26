package net.stln.magitech.feature.tool.property.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.property.*;

import java.awt.*;
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
        ToolProperties properties = new ToolProperties(base.getGroup());
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
        MutableComponent component = propertyCategory.getDisplayText().append("+" + value.toString());
        if (!enabled) {
            component = component.withColor(0x808080).withStyle(ChatFormatting.STRIKETHROUGH);
        }
        return component;
    }
}
