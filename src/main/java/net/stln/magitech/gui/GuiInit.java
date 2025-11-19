package net.stln.magitech.gui;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.stln.magitech.gui.overlay.ManaContainerInfoOverlay;
import net.stln.magitech.gui.overlay.ManaGaugeOverlay;
import net.stln.magitech.gui.overlay.SpellGaugeOverlay;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class GuiInit {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Magitech.MOD_ID);

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, MenuType.MenuSupplier<T> supplier) {
        return REGISTER.register(name, () -> new MenuType<>(supplier, FeatureFlags.VANILLA_SET));
    }    public static final Supplier<MenuType<PartCuttingMenu>> PART_CUTTING_MENU = register("part_cutting_menu", PartCuttingMenu::new);

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Magitech.id("mana_gauge"), new ManaGaugeOverlay());
        event.registerAboveAll(Magitech.id("spell_gauge"), new SpellGaugeOverlay());
        event.registerAboveAll(Magitech.id("mana_container_info"), new ManaContainerInfoOverlay());
    }    public static final Supplier<MenuType<ToolAssemblyMenu>> TOOL_ASSEMBLY_MENU = register("tool_assembly_menu", ToolAssemblyMenu::new);

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(PART_CUTTING_MENU.get(), PartCuttingScreen::new);
        event.register(TOOL_ASSEMBLY_MENU.get(), ToolAssemblyScreen::new);
        event.register(TOOL_REPAIRING_MENU.get(), ToolRepairingScreen::new);
        event.register(TOOL_UPGRADE_MENU.get(), ToolUpgradeScreen::new);
        event.register(THREADBOUND_MENU.get(), ThreadboundScreen::new);
    }    public static final Supplier<MenuType<ToolRepairingMenu>> TOOL_REPAIRING_MENU = register("tool_repairing_menu", ToolRepairingMenu::new);

    public static void registerMenus(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }    public static final Supplier<MenuType<ToolUpgradeMenu>> TOOL_UPGRADE_MENU = register("tool_upgrade_menu", ToolUpgradeMenu::new);
    public static final Supplier<MenuType<ThreadboundMenuType>> THREADBOUND_MENU = register("threadbound_menu", ThreadboundMenuType::new);










}
