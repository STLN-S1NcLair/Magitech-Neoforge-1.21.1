package net.stln.magitech.event;

import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredItem;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.*;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.magic.spell.SpellInit;
import vazkii.patchouli.common.item.PatchouliDataComponents;

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
        setComponentsForThreadbound(event, ItemInit.ARCANE_ENGINEERING_COMPENDIUM, Magitech.id("arcane_engineering_compendium"));

        event.modify(ItemInit.THREAD_PAGE, builder -> builder.set(ComponentInit.THREAD_PAGE_COMPONENT.get(), new ThreadPageComponent(SpellInit.ENERCRUX)).build());
    }

    private static void setComponentsForThreadbound(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item, ResourceLocation bookId) {
        if (ModList.get().isLoaded("patchouli")) {
            event.modify(item, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(), SpellComponent.EMPTY).set(PatchouliDataComponents.BOOK, bookId).set(DataComponentRegistry.BOOK_ID.get(), bookId).build());
        } else {
            event.modify(item, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(), SpellComponent.EMPTY).set(DataComponentRegistry.BOOK_ID.get(), bookId).build());
        }
    }
}
