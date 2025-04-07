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
                output.accept(ItemInit.CHROMIUM_INGOT.get());
                output.accept(ItemInit.REDSTONE_CRYSTAL.get());
                output.accept(ItemInit.POLISHED_REDSTONE_CRYSTAL.get());
                output.accept(ItemInit.ENDER_METAL_INGOT.get());
                output.accept(ItemInit.FRIGIDITE.get());
                output.accept(ItemInit.POLISHED_FRIGIDITE.get());
                output.accept(ItemInit.TRANSLUCIUM.get());
                output.accept(ItemInit.POLISHED_TRANSLUCIUM.get());
                output.accept(ItemInit.ABYSSITE.get());
                output.accept(ItemInit.POLISHED_ABYSSITE.get());
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.TRANSLUCIUM));

                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.TRANSLUCIUM));

                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.STONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.BONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.IRON));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.TRANSLUCIUM));

                output.accept(PartToolGenerator.generateHammer(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.TRANSLUCIUM));
            }).build());

    public static void registerCreativeTabs(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Creative Tabs for" + Magitech.MOD_ID);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
