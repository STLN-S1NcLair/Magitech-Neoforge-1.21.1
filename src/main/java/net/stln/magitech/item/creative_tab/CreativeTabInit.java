package net.stln.magitech.item.creative_tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.item.tool.toolitem.PartToolGenerator;

public class CreativeTabInit {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Magitech.MOD_ID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_TAB = CREATIVE_MODE_TABS.register("magitech_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemInit.WAND.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemInit.WAND.get());
                output.accept(ItemInit.ENDER_METAL_INGOT.get());
                output.accept(ItemInit.FRIGIDITE.get());
                output.accept(ItemInit.POLISHED_FRIGIDITE.get());
                output.accept(ItemInit.TRANSLUCIUM.get());
                output.accept(ItemInit.POLISHED_TRANSLUCIUM.get());
                output.accept(ItemInit.ABYSSITE.get());
                output.accept(ItemInit.POLISHED_ABYSSITE.get());
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.WOOD, MaterialInit.WOOD, MaterialInit.WOOD, MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.STONE, MaterialInit.STONE, MaterialInit.STONE, MaterialInit.STONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.COPPER, MaterialInit.COPPER, MaterialInit.COPPER, MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.GOLD, MaterialInit.GOLD, MaterialInit.GOLD, MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM));

                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.WOOD, MaterialInit.WOOD, MaterialInit.WOOD, MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.STONE, MaterialInit.STONE, MaterialInit.STONE, MaterialInit.STONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE, MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.COPPER, MaterialInit.COPPER, MaterialInit.COPPER, MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON, MaterialInit.IRON));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.GOLD, MaterialInit.GOLD, MaterialInit.GOLD, MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND, MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL, MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE, MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM, MaterialInit.TRANSLUCIUM));
            }).build());

    public static void registerCreativeTabs(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Creative Tabs for" + Magitech.MOD_ID);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
