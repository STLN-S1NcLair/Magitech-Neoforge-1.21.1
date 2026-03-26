package net.stln.magitech.feature.tool.material;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import net.stln.magitech.feature.element.Element;

@net.neoforged.fml.common.asm.enumextension.NamedEnum()
@net.neoforged.fml.common.asm.enumextension.NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum MiningLevel implements IExtensibleEnum {
    NONE("none", 0, 0x805830),
    STONE("stone", 1, 0x808080),
    IRON("iron", 2, 0xFFFFFF),
    DIAMOND("diamond", 3, 0x80F0FF),
    NETHERITE("netherite", 4, 0x705070);

    private final String id;
    private final int tier;
    private final int color;

    MiningLevel(String id, int tier, int color) {
        this.id = id;
        this.tier = tier;
        this.color = color;
    }

    public String get() {
        return this.id;
    }

    public int getTier() {
        return tier;
    }

    public int getColor() {
        return color;
    }

    public MutableComponent getDisplayName() {
        return Component.translatable("tool.magitech.property.mining_level." + get());
    }

    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
    }
}
