package net.stln.magitech.feature.tool.tool_type;

import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.property.ToolProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ToolType(ToolProperties defaultProperties, List<ToolPartLike> parts) implements ToolTypeLike {
    @Override
    public @NotNull ToolType asToolType() {
        return this;
    }
}

//@net.neoforged.fml.common.asm.enumextension.NamedEnum(1)
//@net.neoforged.fml.common.asm.enumextension.NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
//public enum ToolType implements IExtensibleEnum {
//    DAGGER("dagger", 3, ToolGroupInit.MELEE),
//    LIGHT_SWORD("light_sword", 4, ToolGroupInit.MELEE),
//    HEAVY_SWORD("heavy_sword", 4, ToolGroupInit.MELEE),
//    PICKAXE("pickaxe", 3, ToolGroupInit.MELEE),
//    HAMMER("hammer", 4, ToolGroupInit.MELEE),
//    AXE("axe", 4, ToolGroupInit.MELEE),
//    SHOVEL("shovel", 4, ToolGroupInit.MELEE),
//    SCYTHE("scythe", 4, ToolGroupInit.MELEE),
//    SPEAR("spear", 4, ToolGroupInit.MELEE),
//    WAND("wand", 4, ToolGroupInit.CASTER),
//    STAFF("staff", 5, ToolGroupInit.CASTER);
//
//    private final String id;
//    List<ToolPart> parts;
//    private final ToolGroup group;
//
//    ToolType(String id, List<ToolPart> parts, ToolGroup group) {
//        this.id = id;
//        this.parts = parts;
//        this.group = group;
//    }
//
//    public @NotNull String getId() {
//        return this.id;
//    }
//
//    public List<ToolPart> getParts() {
//        return parts;
//    }
//
//    public ToolGroup getGroup() {
//        return group;
//    }
//
//    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
//        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
//    }
//}
