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
import net.stln.magitech.magic.spell.ember.Ignisca;
import net.stln.magitech.magic.spell.ember.Pyrolux;
import net.stln.magitech.magic.spell.flow.Aeltherin;
import net.stln.magitech.magic.spell.flow.Fluvinae;
import net.stln.magitech.magic.spell.flow.Mistrelune;
import net.stln.magitech.magic.spell.glace.Cryoluxa;
import net.stln.magitech.magic.spell.glace.Frigala;
import net.stln.magitech.magic.spell.glace.Nivalune;
import net.stln.magitech.magic.spell.hollow.Nullixis;
import net.stln.magitech.magic.spell.hollow.Tenebrisol;
import net.stln.magitech.magic.spell.hollow.Voidlance;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.magic.Glymora;
import net.stln.magitech.magic.spell.magic.Mystaven;
import net.stln.magitech.magic.spell.phantom.Mirazien;
import net.stln.magitech.magic.spell.phantom.Phantastra;
import net.stln.magitech.magic.spell.phantom.Veilmist;
import net.stln.magitech.magic.spell.surge.Fulgenza;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.surge.Voltaris;
import net.stln.magitech.magic.spell.tremor.Oscilbeam;
import net.stln.magitech.magic.spell.tremor.Sonistorm;
import net.stln.magitech.magic.spell.tremor.Tremivox;

import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModifyDefaultComponentsEvent {

    @SubscribeEvent
    public static void modifyDefault(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event) {
        event.modify(ItemInit.LIGHT_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.HEAVY_SWORD, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.PICKAXE, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.HAMMER, builder -> builder.set(ComponentInit.PART_MATERIAL_COMPONENT.get(), new PartMaterialComponent(List.of(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND))).build());
        event.modify(ItemInit.GLISTENING_LEXICON, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(),
                new SpellComponent(List.of(
                        new Ignisca(), new Pyrolux(), new Fluvalen(),
                        new Frigala(), new Cryoluxa(), new Nivalune(),
                        new Voltaris(), new Fulgenza(), new Sparkion(),
                        new Mirazien(), new Phantastra(), new Veilmist(),
                        new Tremivox(), new Oscilbeam(), new Sonistorm(),
                        new Arcaleth(), new Mystaven(), new Glymora(),
                        new Aeltherin(), new Fluvinae(), new Mistrelune(),
                        new Nullixis(), new Voidlance(), new Tenebrisol()), 0)));
        event.modify(ItemInit.ARCANE_ENGINEERING_COMPENDIUM, builder -> builder.set(ComponentInit.SPELL_COMPONENT.get(),
                new SpellComponent(List.of(
                        new Ignisca(), new Pyrolux(), new Fluvalen(),
                        new Frigala(), new Cryoluxa(), new Nivalune(),
                        new Voltaris(), new Fulgenza(), new Sparkion(),
                        new Mirazien(), new Phantastra(), new Veilmist(),
                        new Tremivox(), new Oscilbeam(), new Sonistorm(),
                        new Arcaleth(), new Mystaven(), new Glymora(),
                        new Aeltherin(), new Fluvinae(), new Mistrelune(),
                        new Nullixis(), new Voidlance(), new Tenebrisol()), 0)));
    }
}
