package net.stln.magitech.event;

import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.stln.magitech.Magitech;
import net.stln.magitech.fluid.FluidInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.*;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.magic.spell.SpellInit;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ModifyDefaultComponentsEvent {

    @SubscribeEvent
    public static void modifyDefault(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event) {
        event.modify(ItemInit.DAGGER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.LIGHT_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.HEAVY_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.PICKAXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.HAMMER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.AXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.SHOVEL, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.SCYTHE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());
        event.modify(ItemInit.WAND, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON)).build());

        event.modify(ItemInit.LIGHT_BLADE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.HEAVY_BLADE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.LIGHT_HANDLE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.HEAVY_HANDLE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.TOOL_BINDING, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.HANDGUARD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.STRIKE_HEAD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.SPIKE_HEAD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.REINFORCED_STICK, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.PLATE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.CATALYST, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());
        event.modify(ItemInit.CONDUCTOR, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).set(ComponentInit.UPGRADE_COMPONENT.get(), UpgradeComponent.EMPTY).build());

        setComponentsForThreadbound(event, ItemInit.GLISTENING_LEXICON, Magitech.id("glistening_lexicon"));
        setComponentsForThreadbound(event, ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN, Magitech.id("materials_and_toolcraft_design"));
        setComponentsForThreadbound(event, ItemInit.THE_FIRE_THAT_THINKS, Magitech.id("the_fire_that_thinks"));
        setComponentsForThreadbound(event, ItemInit.APPLIED_ARCANE_CIRCUITRY, Magitech.id("applied_arcane_circuitry"));
        setComponentsForThreadbound(event, ItemInit.ARCANE_ENGINEERING_COMPENDIUM, Magitech.id("arcane_engineering_compendium"));

        event.modify(ItemInit.THREAD_PAGE, builder -> builder.set(ComponentInit.THREAD_PAGE_COMPONENT.get(), new ThreadPageComponent(SpellInit.ENERCRUX)).build());

        event.modify(ItemInit.TOOL_BELT, builder -> builder.set(ComponentInit.TOOLBELT_COMPONENT.get(), ToolBeltComponent.EMPTY).build());

        event.modify(ItemInit.ALCHEMICAL_FLASK, builder -> builder.set(ComponentInit.FLUID_CONTENT_COMPONENT.get(), SimpleFluidContent.EMPTY).build());
        setFluidContentComponent(event, ItemInit.WATER_FLASK, new FluidStack(Fluids.WATER, 250));
        setFluidContentComponent(event, ItemInit.LAVA_FLASK, new FluidStack(Fluids.LAVA, 250));
        setFluidContentComponent(event, ItemInit.SULFURIC_ACID_FLASK, new FluidStack(FluidInit.SULFURIC_ACID, 250));
        setFluidContentComponent(event, ItemInit.MANA_POTION_FLASK, new FluidStack(FluidInit.MANA_POTION, 250));
        setFluidContentComponent(event, ItemInit.HEALING_POTION_FLASK, new FluidStack(FluidInit.HEALING_POTION, 250));
        setFluidContentComponent(event, ItemInit.EMBER_POTION_FLASK, new FluidStack(FluidInit.EMBER_POTION, 250));
        setFluidContentComponent(event, ItemInit.GLACE_POTION_FLASK, new FluidStack(FluidInit.GLACE_POTION, 250));
        setFluidContentComponent(event, ItemInit.SURGE_POTION_FLASK, new FluidStack(FluidInit.SURGE_POTION, 250));
        setFluidContentComponent(event, ItemInit.PHANTOM_POTION_FLASK, new FluidStack(FluidInit.PHANTOM_POTION, 250));
        setFluidContentComponent(event, ItemInit.TREMOR_POTION_FLASK, new FluidStack(FluidInit.TREMOR_POTION, 250));
        setFluidContentComponent(event, ItemInit.MAGIC_POTION_FLASK, new FluidStack(FluidInit.MAGIC_POTION, 250));
        setFluidContentComponent(event, ItemInit.FLOW_POTION_FLASK, new FluidStack(FluidInit.FLOW_POTION, 250));
        setFluidContentComponent(event, ItemInit.HOLLOW_POTION_FLASK, new FluidStack(FluidInit.HOLLOW_POTION, 250));
    }

    private static void setComponentsForThreadbound(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item, ResourceLocation bookId) {
        event.modify(item, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(), SpellComponent.EMPTY).set(DataComponentRegistry.BOOK_ID.get(), bookId).build());
    }

    private static void setFluidContentComponent(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item, FluidStack fluidStack) {
        event.modify(item, builder -> builder.set(ComponentInit.FLUID_CONTENT_COMPONENT.get(), SimpleFluidContent.copyOf(fluidStack)).build());
    }
}
