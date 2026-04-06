package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public interface IToolPropertyGroup {

    boolean contains(IToolProperty<?> property);

    Color getColor();

    MutableComponent getDisplayText();
}
