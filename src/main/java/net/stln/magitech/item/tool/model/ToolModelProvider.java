package net.stln.magitech.item.tool.model;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class ToolModelProvider extends ItemModelProvider {
    public ToolModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (ToolMaterial material : ModelRegistrar.materials) {
            for (String type : ModelRegistrar.toolTypes) {
                for (String part : ModelRegistrar.partTypes) {
                    if (existingFileHelper.exists(ModelRegistrar.getPartTextureId(material, type, part), ModelProvider.TEXTURE)) {
                        String parent = "item/handheld";
                        if (type.equals("heavy_sword") ||
                                type.equals("hammer") ||
                                type.equals("scythe") ||
                                type.equals("spear") ||
                                type.equals("staff")) {
                            parent = "magitech:item/heavy_tool";
                        }
                        getBuilder(ModelRegistrar.getPartModelName(material, type, part))
                                .parent(new ModelFile.UncheckedModelFile(parent))
                                .texture("layer0", ModelRegistrar.getPartTextureId(material, type, part));
                    }
                }
            }
        }
        for (ToolMaterial material : ModelRegistrar.materials) {
            for (String part : ModelRegistrar.partTypes) {
                if (existingFileHelper.exists(ModelRegistrar.getPartItemTextureId(material, part), ModelProvider.TEXTURE)) {
                    String parent = "item/generated";
                    getBuilder(ModelRegistrar.getPartItemModelName(material, part))
                            .parent(new ModelFile.UncheckedModelFile(parent))
                            .texture("layer0", ModelRegistrar.getPartItemTextureId(material, part));
                }
            }
        }
    }
}
