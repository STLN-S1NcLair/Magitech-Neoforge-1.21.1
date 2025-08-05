package net.stln.magitech.gui;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OverlayInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Magitech.MOD_ID);
    public static final Supplier<MenuType<ToolAssemblyMenu>> TOOL_ASSEMBLY_MENU = MENU_TYPES.register("tool_assembly_menu", () -> new MenuType(ToolAssemblyMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<PartCuttingMenu>> PART_CUTTING_MENU = MENU_TYPES.register("part_cutting_menu", () -> new MenuType(PartCuttingMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<ThreadboudMenuType>> THREADBOUND_MENU = MENU_TYPES.register("threadbound_menu", () -> new MenuType(ThreadboudMenuType::new, FeatureFlags.DEFAULT_FLAGS));

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_gauge"), new ManaGaugeOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "spell_gauge"), new SpellGaugeOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_container_info"), new ManaContainerInfoOverlay());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TOOL_ASSEMBLY_MENU.get(), ToolAssemblyScreen::new);
        event.register(PART_CUTTING_MENU.get(), PartCuttingScreen::new);
        event.register(THREADBOUND_MENU.get(), ThreadboundScreen::new);
    }

    public static void registerMenus(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }


}
