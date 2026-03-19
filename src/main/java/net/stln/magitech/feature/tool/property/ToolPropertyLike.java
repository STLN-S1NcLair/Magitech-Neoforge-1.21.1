package net.stln.magitech.feature.tool.property;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolPropertyLike<T> {
    @NotNull IToolProperty<T> asToolProperty();
}
