package net.stln.magitech.feature.tool.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.registry.RegistryHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ModelRegistrar {

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional registerAdditional) {
        for (ToolMaterial material : RegistryHelper.registeredToolMaterials()) {

            // tools
            for (ToolTypeLike type : RegistryHelper.registeredToolTypes()) {
                for (ToolPartLike part : type.asToolType().parts().stream().map(ToolType.PartData::part).toList()) {
                    ResourceLocation partModelId = getPartModelId(material, type.asToolType(), part.asToolPart());
                    if (Minecraft.getInstance().getResourceManager().getResource(partModelId.withPrefix("models/").withSuffix(".json")).isPresent()) {
                        registerAdditional.register(ModelResourceLocation.standalone(partModelId));
                    }
                }
            }

            // parts
            for (ToolPartLike part : RegistryHelper.registeredToolParts()) {
                ResourceLocation partItemModelId = getPartItemModelId(material, part.asToolPart());
                if (Minecraft.getInstance().getResourceManager().getResource(partItemModelId.withPrefix("models/").withSuffix(".json")).isPresent()) {
                    registerAdditional.register(ModelResourceLocation.standalone(partItemModelId));
                }
            }
        }
    }

    public static ResourceLocation getPartTextureId(ToolMaterial toolMaterial, ToolType toolType, ToolPart part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/tool/" + getTypePath(toolType) + "/" + toolMaterial.getId().getPath() + "_" + getPartPath(part));
    }

    public static ResourceLocation getPartModelId(ToolMaterial toolMaterial, ToolType toolType, ToolPart part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/" + toolMaterial.getId().getPath() + "_" + getTypePath(toolType) + "_" + getPartPath(part));
    }

    public static ResourceLocation getPartItemTextureId(ToolMaterial toolMaterial, ToolPart part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/part/" + toolMaterial.getId().getPath() + "_" + getPartPath(part));
    }

    public static ResourceLocation getPartItemModelId(ToolMaterial toolMaterial, ToolPart part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/" + toolMaterial.getId().getPath() + "_" + getPartPath(part));
    }

    public static String getPartModelName(ToolMaterial toolMaterial, ToolType toolType, ToolPart part) {
        return toolMaterial.getId().getPath() + "_" + getTypePath(toolType) + "_" + getPartPath(part);
    }

    public static String getPartItemModelName(ToolMaterial toolMaterial, ToolPart part) {
        return toolMaterial.getId().getPath() + "_" + getPartPath(part);
    }

    private static @NotNull String getTypePath(ToolType toolType) {
        return MagitechRegistries.TOOL_TYPE.getKey(toolType).getPath();
    }

    private static @NotNull String getPartPath(ToolPart part) {
        return MagitechRegistries.TOOL_PART.getKey(part).getPath();
    }

}
