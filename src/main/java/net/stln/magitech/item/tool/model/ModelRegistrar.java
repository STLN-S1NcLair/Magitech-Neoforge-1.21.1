package net.stln.magitech.item.tool.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModelRegistrar {

    public static final List<ToolMaterial> materials = ToolMaterialRegister.getDictId().values().stream().toList();
    public static final List<String> toolTypes = List.of("dagger", "light_sword", "heavy_sword", "pickaxe", "hammer", "axe", "shovel", "scythe", "spear", "wand", "staff");
    public static final List<String> partTypes = List.of("light_blade", "heavy_blade", "light_handle", "heavy_handle", "tool_binding", "handguard", "strike_head", "spike_head", "reinforced_stick", "plate", "catalyst", "conductor");

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional registerAdditional) {
        for (ToolMaterial material : materials) {
            for (String type : toolTypes) {
                for (String part : partTypes) {
                    registerAdditional.register(ModelResourceLocation.standalone(getPartModelId(material, type, part)));
                }
            }
        }
    }

    public static ResourceLocation getPartTextureId(ToolMaterial toolMaterial, String toolType, String part) {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "item/tool/" + toolType + "/" + toolMaterial.getId() + "_" + part);
    }

    public static ResourceLocation getPartModelId(ToolMaterial toolMaterial, String toolType, String part) {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "item/" + toolMaterial.getId() + "_" + toolType + "_" + part);
    }

    public static String getPartModelName(ToolMaterial toolMaterial, String toolType, String part) {
        return toolMaterial.getId() + "_" + toolType + "_" + part;
    }

}
