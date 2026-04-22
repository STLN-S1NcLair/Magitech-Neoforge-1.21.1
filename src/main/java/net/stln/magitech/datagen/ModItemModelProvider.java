package net.stln.magitech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.model.ModelRegistrar;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.registry.RegistryHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Magitech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemInit.GLISTENING_LEXICON.get());
        basicItem(ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN.get());
        basicItem(ItemInit.THE_FIRE_THAT_THINKS.get());
        basicItem(ItemInit.APPLIED_ARCANE_CIRCUITRY.get());
        basicItem(ItemInit.ARCANE_ENGINEERING_COMPENDIUM.get());
        basicItem(ItemInit.FLUXIUM_RING.get());
        basicItem(ItemInit.MANA_RING.get());
        basicItem(ItemInit.ARDOR_RING.get());
        basicItem(ItemInit.QUENCH_RING.get());
        basicItem(ItemInit.CHARGEBIND_RING.get());
        basicItem(ItemInit.CRACK_RING.get());
        basicItem(ItemInit.CELERITAS_RING.get());
        basicItem(ItemInit.PROTECTION_RING.get());
        basicItem(ItemInit.UPDRAFT_RING.get());
        basicItem(ItemInit.DISTORTION_RING.get());
        basicItem(ItemInit.UMBRAL_RING.get());
        basicItem(ItemInit.DAWN_RING.get());
        basicItem(ItemInit.FLUXBOUND_RING.get());
        basicItem(ItemInit.TOOL_BELT.get());
        basicItem(ItemInit.LIGHT_BLADE.get());
        basicItem(ItemInit.HEAVY_BLADE.get());
        basicItem(ItemInit.LIGHT_HANDLE.get());
        basicItem(ItemInit.HEAVY_HANDLE.get());
        basicItem(ItemInit.TOOL_BINDING.get());
        basicItem(ItemInit.HANDGUARD.get());
        basicItem(ItemInit.STRIKE_HEAD.get());
        basicItem(ItemInit.SPIKE_HEAD.get());
        basicItem(ItemInit.REINFORCED_ROD.get());
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
        basicItem(ItemInit.HIGH_PURITY_FLUORITE.get());
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
        basicItem(ItemInit.RAW_ZINC.get());
        basicItem(ItemInit.ZINC_INGOT.get());
        basicItem(ItemInit.FLUXIUM_INGOT.get());
        basicItem(ItemInit.FLUXIUM_NUGGET.get());
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
        basicItem(ItemInit.MANA_INSULATING_GLASS.get());
        basicItem(ItemInit.SULFURIC_ACID_BATTERY.get());
        basicItem(ItemInit.MANA_DEEXCITER_CORE.get());
        basicItem(ItemInit.ASPECT_COLLECTOR.get());
        basicItem(ItemInit.BOOTS_FRAME.get());
        basicItem(ItemInit.MANA_CELL.get());
        basicItem(ItemInit.MANA_BERRIES.get());
        basicItem(ItemInit.MANA_PIE.get());
        basicItem(ItemInit.ALCHEMICAL_FLASK.get());
        basicItem(ItemInit.WATER_FLASK.get());
        basicItem(ItemInit.LAVA_FLASK.get());
        basicItem(ItemInit.SULFURIC_ACID_FLASK.get());
        basicItem(ItemInit.MANA_POTION_FLASK.get());
        basicItem(ItemInit.HEALING_POTION_FLASK.get());
        basicItem(ItemInit.EMBER_POTION_FLASK.get());
        basicItem(ItemInit.GLACE_POTION_FLASK.get());
        basicItem(ItemInit.SURGE_POTION_FLASK.get());
        basicItem(ItemInit.PHANTOM_POTION_FLASK.get());
        basicItem(ItemInit.TREMOR_POTION_FLASK.get());
        basicItem(ItemInit.MAGIC_POTION_FLASK.get());
        basicItem(ItemInit.FLOW_POTION_FLASK.get());
        basicItem(ItemInit.HOLLOW_POTION_FLASK.get());

        basicItem(BlockInit.CELIFERN_DOOR_ITEM.get());
        saplingItem(BlockInit.CELIFERN_SAPLING_ITEM);
        basicItem(BlockInit.CELIFERN_SIGN_ITEM.get());
        basicItem(BlockInit.CELIFERN_HANGING_SIGN_ITEM.get());
        basicItem(BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get());
        saplingItem(BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM);
        basicItem(BlockInit.CHARCOAL_BIRCH_SIGN_ITEM.get());
        basicItem(BlockInit.CHARCOAL_BIRCH_HANGING_SIGN_ITEM.get());
        basicItem(BlockInit.MYSTWOOD_DOOR_ITEM.get());
        basicItem(BlockInit.MYSTWOOD_SIGN_ITEM.get());
        basicItem(BlockInit.MYSTWOOD_HANGING_SIGN_ITEM.get());
        basicItem(BlockInit.MISTALIA_PETALS_ITEM.get());

        basicItem(ItemInit.CELIFERN_BOAT.get());
        basicItem(ItemInit.CELIFERN_CHEST_BOAT.get());
        basicItem(ItemInit.CHARCOAL_BIRCH_BOAT.get());
        basicItem(ItemInit.CHARCOAL_BIRCH_CHEST_BOAT.get());
        basicItem(ItemInit.MYSTWOOD_BOAT.get());
        basicItem(ItemInit.MYSTWOOD_CHEST_BOAT.get());

        for (ToolMaterial material : RegistryHelper.registeredToolMaterials()) {

            // tools
            for (ToolTypeLike type : RegistryHelper.registeredToolTypes()) {
                for (ToolPartLike part : type.asToolType().parts().stream().map(ToolType.PartData::part).toList()) {
                    ToolType toolType = type.asToolType();
                    ToolPart toolPart = part.asToolPart();
                    if (existingFileHelper.exists(ModelRegistrar.getPartTextureId(material, toolType, toolPart), ModelProvider.TEXTURE)) {
                        String parent = "item/handheld";
                        if (type.asToolType().heavyTool()) {
                            parent = "magitech:item/heavy_tool";
                        }
                        getBuilder(ModelRegistrar.getPartModelName(material, toolType, toolPart))
                                .parent(new ModelFile.UncheckedModelFile(parent))
                                .texture("layer0", ModelRegistrar.getPartTextureId(material, toolType, toolPart));
                    }
                }
            }

            // parts
            for (ToolPartLike part : RegistryHelper.registeredToolParts()) {
                ToolPart toolPart = part.asToolPart();
                if (existingFileHelper.exists(ModelRegistrar.getPartItemTextureId(material, toolPart), ModelProvider.TEXTURE)) {
                    String parent = "item/generated";
                    getBuilder(ModelRegistrar.getPartItemModelName(material, toolPart))
                            .parent(new ModelFile.UncheckedModelFile(parent))
                            .texture("layer0", ModelRegistrar.getPartItemTextureId(material, toolPart));
                }
            }
        }
    }

    private void saplingItem(DeferredItem<BlockItem> item) {
        getBuilder(item.get().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Magitech.id("block/" + item.getId().getPath()));
    }
}
