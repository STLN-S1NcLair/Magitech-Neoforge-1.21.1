package net.stln.magitech.item.tool.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

import java.util.Collection;
import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ModelRegistrar {

    public static final Collection<ToolMaterial> materials = ToolMaterialRegister.getDictId().values();
    public static final List<String> toolTypes = List.of("dagger", "light_sword", "heavy_sword", "pickaxe", "hammer", "axe", "shovel", "scythe", "spear", "wand", "staff", "wandrel");
    public static final List<String> partTypes = List.of("light_blade", "heavy_blade", "light_handle", "heavy_handle", "tool_binding", "handguard", "strike_head", "spike_head", "reinforced_stick", "plate", "catalyst", "conductor");

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional registerAdditional) {
        for (ToolMaterial material : materials) {
            for (String type : toolTypes) {
                for (String part : partTypes) {
                    ResourceLocation partModelId = getPartModelId(material, type, part);
                    if (Minecraft.getInstance().getResourceManager().getResource(partModelId.withPrefix("models/").withSuffix(".json")).isPresent()) {
                        registerAdditional.register(ModelResourceLocation.standalone(partModelId));
                    }
                }
            }
        }
        for (ToolMaterial material : materials) {
            for (String part : partTypes) {
                ResourceLocation partItemModelId = getPartItemModelId(material, part);
                if (Minecraft.getInstance().getResourceManager().getResource(partItemModelId.withPrefix("models/").withSuffix(".json")).isPresent()) {
                    registerAdditional.register(ModelResourceLocation.standalone(partItemModelId));
                }
            }
        }
    }

    public static ResourceLocation getPartTextureId(ToolMaterial toolMaterial, String toolType, String part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/tool/" + toolType + "/" + toolMaterial.getId().getPath() + "_" + part);
    }

    public static ResourceLocation getPartModelId(ToolMaterial toolMaterial, String toolType, String part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/" + toolMaterial.getId().getPath() + "_" + toolType + "_" + part);
    }

    public static ResourceLocation getPartItemTextureId(ToolMaterial toolMaterial, String part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/part/" + toolMaterial.getId().getPath() + "_" + part);
    }

    public static ResourceLocation getPartItemModelId(ToolMaterial toolMaterial, String part) {
        return ResourceLocation.fromNamespaceAndPath(toolMaterial.getId().getNamespace(), "item/" + toolMaterial.getId().getPath() + "_" + part);
    }

    public static String getPartModelName(ToolMaterial toolMaterial, String toolType, String part) {
        return toolMaterial.getId().getPath() + "_" + toolType + "_" + part;
    }

    public static String getPartItemModelName(ToolMaterial toolMaterial, String part) {
        return toolMaterial.getId().getPath() + "_" + part;
    }

}
