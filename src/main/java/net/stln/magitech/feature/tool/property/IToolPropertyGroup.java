package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;

public interface IToolPropertyGroup {

    boolean contains(IToolProperty<?> property);

    MutableComponent getDisplayText();
}
