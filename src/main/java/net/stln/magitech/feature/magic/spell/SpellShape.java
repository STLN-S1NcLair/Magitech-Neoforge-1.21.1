package net.stln.magitech.feature.magic.spell;

import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import net.stln.magitech.feature.element.Element;

@net.neoforged.fml.common.asm.enumextension.NamedEnum()
@net.neoforged.fml.common.asm.enumextension.NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum SpellShape implements IExtensibleEnum {
    SHOT("shot", 0xFFFFFF, 0x808080),
    BEAM("beam", 0xFFFFFF, 0x808080),
    SPRAY("spray", 0xFFFFFF, 0x808080),
    FRAGMENT("fragment", 0xFFFFFF, 0x808080),
    DASH("dash", 0xFFFFFF, 0x808080),
    FIELD("field", 0xFFFFFF, 0x808080),
    BOMB("bomb", 0xFFFFFF, 0x808080),
    CHARGE("charge", 0xFFFFFF, 0x808080),
    ENHANCE("enhance", 0xFFFFFF, 0x808080),
    WEAKEN("weaken", 0xFFFFFF, 0x808080),
    RESILIENCE("resilience", 0xFFFFFF, 0x808080),
    UTILITY("utility", 0xFFFFFF, 0x808080),
    INFUSE("infuse", 0xFFFFFF, 0x808080);

    private final String id;
    private final int color;
    private final int dark;

    SpellShape(String id, int color, int dark) {
        this.id = id;
        this.color = color;
        this.dark = dark;
    }

    public String get() {
        return this.id;
    }

    public int getColor() {
        return color;
    }

    public int getDark() {
        return dark;
    }

    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
    }
}
