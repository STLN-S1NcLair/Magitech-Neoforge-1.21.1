package net.stln.magitech.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.magic.spell.ember.Fluvalen;
import net.stln.magitech.magic.spell.glace.Cryoluxa;
import net.stln.magitech.magic.spell.glace.Frigala;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.phantom.Mirazien;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.tremor.Ocsilbeam;

import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModifyDefaultComponentsEvent {

    @SubscribeEvent
    public static void modifyDefault(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event) {
        event.modify(ItemInit.LIGHT_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.HEAVY_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.PICKAXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.HAMMER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.GLISTENING_LEXICON, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(), new SpellComponent(List.of(new Sparkion(), new Arcaleth(), new Cryoluxa(), new Fluvalen(), new Mirazien(), new Ocsilbeam(), new Frigala()), 0)));
    }
}
