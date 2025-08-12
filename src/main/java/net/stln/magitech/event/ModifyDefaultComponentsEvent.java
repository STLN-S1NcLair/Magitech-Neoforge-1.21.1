package net.stln.magitech.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.*;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.magic.spell.SpellInit;
import net.stln.magitech.magic.spell.SpellRegister;
import vazkii.patchouli.common.item.PatchouliDataComponents;

import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModifyDefaultComponentsEvent {

    @SubscribeEvent
    public static void modifyDefault(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event) {
        event.modify(ItemInit.DAGGER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.LIGHT_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.HEAVY_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.PICKAXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.HAMMER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.AXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.SHOVEL, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.SCYTHE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());
        event.modify(ItemInit.WAND, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON))).build());

        event.modify(ItemInit.LIGHT_SWORD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.HEAVY_BLADE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.LIGHT_HANDLE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.HEAVY_HANDLE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.TOOL_BINDING, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.HANDGUARD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.STRIKE_HEAD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.SPIKE_HEAD, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.REINFORCED_STICK, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.PLATE, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.CATALYST, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());
        event.modify(ItemInit.CONDUCTOR, builder -> builder.set(ComponentInit.MATERIAL_COMPONENT.get(), new MaterialComponent(MaterialInit.IRON)).build());

        event.modify(ItemInit.GLISTENING_LEXICON, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(),
                        new SpellComponent(List.of(), 0))
                .set(PatchouliDataComponents.BOOK, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "glistening_lexicon")).build());
        event.modify(ItemInit.THE_FIRE_THAT_THINKS, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(),
                        new SpellComponent(List.of(), 0))
                .set(PatchouliDataComponents.BOOK, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "the_fire_that_thinks")).build());
        event.modify(ItemInit.ARCANE_ENGINEERING_COMPENDIUM, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(),
                new SpellComponent(List.of(), 0)).build());
        event.modify(ItemInit.THREAD_PAGE, builder -> builder.set(ComponentInit.THREAD_PAGE_COMPONENT.get(),
                new ThreadPageComponent(SpellInit.ENERCRUX)).build());
    }
}
