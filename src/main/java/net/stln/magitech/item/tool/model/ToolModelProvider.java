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
                        getBuilder(ModelRegistrar.getPartModelName(material, type, part))
                                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                                .texture("layer0", ModelRegistrar.getPartTextureId(material, type, part));
                    }
                }
            }
        }
    }
}
