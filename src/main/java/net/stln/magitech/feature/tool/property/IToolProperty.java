package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public interface IToolProperty<T> extends ToolPropertyLike<T> {

    public MutableComponent getDisplayName();

    @Override
    default @NotNull IToolProperty<T> asToolProperty() {
        return this;
    }
}