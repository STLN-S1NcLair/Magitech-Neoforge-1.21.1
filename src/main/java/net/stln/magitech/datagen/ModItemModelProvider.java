package net.stln.magitech.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.tool.model.ModelRegistrar;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Magitech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemInit.GLISTENING_LEXICON.get());
        basicItem(ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN.get());
        basicItem(ItemInit.THE_FIRE_THAT_THINKS.get());
        basicItem(ItemInit.ARCANE_ENGINEERING_COMPENDIUM.get());
        basicItem(ItemInit.MANA_RING.get());
        basicItem(ItemInit.GALEVENT_RING.get());
        basicItem(ItemInit.CHARGEBIND_RING.get());
        basicItem(ItemInit.TORSION_RING.get());
        basicItem(ItemInit.UMBRAL_RING.get());
        basicItem(ItemInit.DAWN_RING.get());
        basicItem(ItemInit.FLUXBOUND_RING.get());
        basicItem(ItemInit.LIGHT_BLADE.get());
        basicItem(ItemInit.HEAVY_BLADE.get());
        basicItem(ItemInit.LIGHT_HANDLE.get());
        basicItem(ItemInit.HEAVY_HANDLE.get());
        basicItem(ItemInit.TOOL_BINDING.get());
        basicItem(ItemInit.HANDGUARD.get());
        basicItem(ItemInit.STRIKE_HEAD.get());
        basicItem(ItemInit.SPIKE_HEAD.get());
        basicItem(ItemInit.REINFORCED_STICK.get());
        basicItem(ItemInit.PLATE.get());
        basicItem(ItemInit.CATALYST.get());
        basicItem(ItemInit.CONDUCTOR.get());
        handheldItem(ItemInit.DAGGER.get());
        handheldItem(ItemInit.LIGHT_SWORD.get());
        handheldItem(ItemInit.HEAVY_SWORD.get());
        handheldItem(ItemInit.PICKAXE.get());
        handheldItem(ItemInit.HAMMER.get());
        handheldItem(ItemInit.AXE.get());
        handheldItem(ItemInit.SHOVEL.get());
        handheldItem(ItemInit.SCYTHE.get());
        handheldItem(ItemInit.WAND.get());
        basicItem(ItemInit.AETHER_LIFTER.get());
        basicItem(ItemInit.FLAMGLIDE_STRIDER.get());
        basicItem(ItemInit.ALCHAEFABRIC.get());
        basicItem(ItemInit.AEGIS_WEAVE.get());
        basicItem(ItemInit.FLUORITE.get());
        basicItem(ItemInit.MANA_CHARGED_FLUORITE.get());
        basicItem(ItemInit.TOURMALINE.get());
        basicItem(ItemInit.EMBER_CRYSTAL.get());
        basicItem(ItemInit.GLACE_CRYSTAL.get());
        basicItem(ItemInit.SURGE_CRYSTAL.get());
        basicItem(ItemInit.PHANTOM_CRYSTAL.get());
        basicItem(ItemInit.TREMOR_CRYSTAL.get());
        basicItem(ItemInit.MAGIC_CRYSTAL.get());
        basicItem(ItemInit.FLOW_CRYSTAL.get());
        basicItem(ItemInit.HOLLOW_CRYSTAL.get());
        basicItem(ItemInit.AGGREGATED_NOCTIS.get());
        basicItem(ItemInit.AGGREGATED_LUMINIS.get());
        basicItem(ItemInit.AGGREGATED_FLUXIA.get());
        basicItem(ItemInit.CITRINE.get());
        basicItem(ItemInit.REDSTONE_CRYSTAL.get());
        basicItem(ItemInit.POLISHED_REDSTONE_CRYSTAL.get());
        basicItem(ItemInit.SULFUR.get());
        basicItem(ItemInit.CHROMIUM_INGOT.get());
        basicItem(ItemInit.ENDER_METAL_INGOT.get());
        basicItem(ItemInit.NETHER_STAR_BRILLIANCE.get());
        basicItem(ItemInit.RADIANT_STEEL_INGOT.get());
        basicItem(ItemInit.FRIGIDITE.get());
        basicItem(ItemInit.POLISHED_FRIGIDITE.get());
        basicItem(ItemInit.TRANSLUCIUM.get());
        basicItem(ItemInit.POLISHED_TRANSLUCIUM.get());
        basicItem(ItemInit.RESONITE.get());
        basicItem(ItemInit.POLISHED_RESONITE.get());
        basicItem(ItemInit.ABYSSITE.get());
        basicItem(ItemInit.POLISHED_ABYSSITE.get());
        basicItem(ItemInit.MANA_DEEXCITER_CORE.get());
        basicItem(ItemInit.ASPECT_COLLECTOR.get());
        basicItem(ItemInit.BOOTS_FRAME.get());
        basicItem(ItemInit.MANA_BERRIES.get());
        basicItem(ItemInit.MANA_PIE.get());
        basicItem(ItemInit.ALCHEMICAL_FLASK.get());
        basicItem(ItemInit.WATER_FLASK.get());
        basicItem(ItemInit.LAVA_FLASK.get());
        basicItem(ItemInit.SULFURIC_ACID_FLASK.get());
        basicItem(ItemInit.MANA_POTION_FLASK.get());
        basicItem(ItemInit.HEALING_POTION_FLASK.get());

        basicItem(BlockInit.CELIFERN_DOOR_ITEM.get());
        getBuilder(BlockInit.CELIFERN_SAPLING_ITEM.get().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Magitech.id("block/" + BlockInit.CELIFERN_SAPLING_ITEM.getId().getPath()));
        basicItem(BlockInit.CELIFERN_SIGN_ITEM.get());
        basicItem(BlockInit.CELIFERN_HANGING_SIGN_ITEM.get());
        basicItem(BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get());
        getBuilder(BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM.get().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Magitech.id("block/" + BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM.getId().getPath()));
        basicItem(BlockInit.CHARCOAL_BIRCH_SIGN_ITEM.get());
        basicItem(BlockInit.CHARCOAL_BIRCH_HANGING_SIGN_ITEM.get());
        basicItem(BlockInit.MISTALIA_PETALS_ITEM.get());

        MagitechRegistries.TOOL_MATERIAL.stream().forEach(toolMaterial -> {
            for (String type : ModelRegistrar.toolTypes) {
                for (String part : ModelRegistrar.partTypes) {
                    if (existingFileHelper.exists(ModelRegistrar.getPartTextureId(toolMaterial, type, part), ModelProvider.TEXTURE)) {
                        String parent = "item/handheld";
                        if (type.equals("heavy_sword") ||
                                type.equals("hammer") ||
                                type.equals("scythe") ||
                                type.equals("spear") ||
                                type.equals("staff")) {
                            parent = "magitech:item/heavy_tool";
                        }
                        getBuilder(ModelRegistrar.getPartModelName(toolMaterial, type, part))
                                .parent(new ModelFile.UncheckedModelFile(parent))
                                .texture("layer0", ModelRegistrar.getPartTextureId(toolMaterial, type, part));
                    }
                }
            }
        });
        MagitechRegistries.TOOL_MATERIAL.stream().forEach(toolMaterial -> {
            for (String part : ModelRegistrar.partTypes) {
                if (existingFileHelper.exists(ModelRegistrar.getPartItemTextureId(toolMaterial, part), ModelProvider.TEXTURE)) {
                    String parent = "item/generated";
                    getBuilder(ModelRegistrar.getPartItemModelName(toolMaterial, part))
                            .parent(new ModelFile.UncheckedModelFile(parent))
                            .texture("layer0", ModelRegistrar.getPartItemTextureId(toolMaterial, part));
                }
            }
        });
    }
}
