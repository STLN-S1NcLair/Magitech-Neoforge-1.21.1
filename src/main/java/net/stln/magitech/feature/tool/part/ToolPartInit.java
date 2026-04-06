package net.stln.magitech.feature.tool.part;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.registry.DeferredToolPart;
import net.stln.magitech.registry.DeferredToolPartRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ToolPartInit {
    public static final DeferredToolPartRegister REGISTER = new DeferredToolPartRegister(Magitech.MOD_ID);

    private static float order = 0F;

    private static @NotNull DeferredToolPart<ToolPart> register(@NotNull String path, @NotNull Function<Float, ToolPart> toolPart) {
        return REGISTER.register(path, () -> toolPart.apply(order++));
    }

    public static final DeferredToolPart<ToolPart> LIGHT_BLADE = register("light_blade", f -> new ToolPart(f, ItemInit.LIGHT_BLADE));
    public static final DeferredToolPart<ToolPart> HEAVY_BLADE = register("heavy_blade", f -> new ToolPart(f, ItemInit.HEAVY_BLADE));
    public static final DeferredToolPart<ToolPart> LIGHT_HANDLE = register("light_handle", f -> new ToolPart(f, ItemInit.LIGHT_HANDLE));
    public static final DeferredToolPart<ToolPart> HEAVY_HANDLE = register("heavy_handle", f -> new ToolPart(f, ItemInit.HEAVY_HANDLE));
    public static final DeferredToolPart<ToolPart> TOOL_BINDING = register("tool_binding", f -> new ToolPart(f, ItemInit.TOOL_BINDING));
    public static final DeferredToolPart<ToolPart> HANDGUARD = register("handguard", f -> new ToolPart(f, ItemInit.HANDGUARD));
    public static final DeferredToolPart<ToolPart> STRIKE_HEAD = register("strike_head", f -> new ToolPart(f, ItemInit.STRIKE_HEAD));
    public static final DeferredToolPart<ToolPart> SPIKE_HEAD = register("spike_head", f -> new ToolPart(f, ItemInit.SPIKE_HEAD));
    public static final DeferredToolPart<ToolPart> REINFORCED_ROD = register("reinforced_rod", f -> new ToolPart(f, ItemInit.REINFORCED_ROD));
    public static final DeferredToolPart<ToolPart> PLATE = register("plate", f -> new ToolPart(f, ItemInit.PLATE));
    public static final DeferredToolPart<ToolPart> CATALYST = register("catalyst", f -> new ToolPart(f, ItemInit.CATALYST));
    public static final DeferredToolPart<ToolPart> CONDUCTOR = register("conductor", f -> new ToolPart(f, ItemInit.CONDUCTOR));

    public static void registerToolParts(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Parts for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
