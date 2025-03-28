package net.stln.magitech.item.renderer;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolMaterial;
import net.stln.magitech.item.tool.ToolMaterialDictionary;
import net.stln.magitech.item.tool.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModelRegistrar {

    public static final List<ToolMaterial> materials = ToolMaterialDictionary.getDictId().values().stream().toList();
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

    public static @NotNull net.minecraft.client.resources.model.UnbakedModel getUnbakedModel(ResourceLocation identifier) {
        Magitech.LOGGER.info(identifier.toString());
        return BlockModel.fromString(
                "{\n" +
                        "    \"parent\": \"item/handheld\",\n" +
                        "    \"textures\": {\n" +
                        "        \"layer0\": \"" + identifier.toString() + "\"\n" +
                        "    }\n" +
                        "}"
        );
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
