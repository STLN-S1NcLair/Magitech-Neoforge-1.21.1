package net.stln.magitech.feature.tool.property;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import net.stln.magitech.feature.element.Element;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.awt.*;

@net.neoforged.fml.common.asm.enumextension.NamedEnum()
@net.neoforged.fml.common.asm.enumextension.NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum ToolPropertyCategory implements IExtensibleEnum, IToolPropertyGroup {
    ATTACK("attack", new Color(0xFF4040)),
    ELEMENT("element", new Color(0xFFFFFF)),
    HANDLING("handling", new Color(0x40FF80)),
    CONTINUITY("continuity", new Color(0x40A0FF)),
    RANGE("range", new Color(0xFFE040)),
    UNIQUE("unique", new Color(0x8040FF)),
    DEFENCE("defence", new Color(0x8080A0)),
    DURATION("duration", new Color(0xFFFFFF));

    private final String id;
    private final Color color;

    ToolPropertyCategory(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String get() {
        return this.id;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean contains(IToolProperty<?> property) {
        return property.getCategory() == this;
    }

    @Override
    public MutableComponent getDisplayText() {
        return Component.translatable("tool.magitech.property.category." + get()).withColor(getColor().getRGB()).withStyle(ChatFormatting.UNDERLINE);
    }

    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
    }
}
