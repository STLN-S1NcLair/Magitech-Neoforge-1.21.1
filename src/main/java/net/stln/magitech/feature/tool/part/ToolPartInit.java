package net.stln.magitech.feature.tool.part;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.spell.ember.*;
import net.stln.magitech.feature.magic.spell.spell.flow.*;
import net.stln.magitech.feature.magic.spell.spell.glace.*;
import net.stln.magitech.feature.magic.spell.spell.hollow.*;
import net.stln.magitech.feature.magic.spell.spell.magic.*;
import net.stln.magitech.feature.magic.spell.spell.mana.Enercrux;
import net.stln.magitech.feature.magic.spell.spell.phantom.*;
import net.stln.magitech.feature.magic.spell.spell.surge.*;
import net.stln.magitech.feature.magic.spell.spell.tremor.*;
import net.stln.magitech.registry.DeferredSpell;
import net.stln.magitech.registry.DeferredToolPart;
import net.stln.magitech.registry.DeferredToolPartRegister;
import org.jetbrains.annotations.NotNull;

public class ToolPartInit {
    public static final DeferredToolPartRegister REGISTER = new DeferredToolPartRegister(Magitech.MOD_ID);

    private static @NotNull DeferredToolPart<ToolPart> register(@NotNull String path, @NotNull ToolPart toolPart) {
        return REGISTER.register(path, () -> toolPart);
    }

    public static final DeferredToolPart<ToolPart> LIGHT_BLADE = register("light_blade", new ToolPart(ItemInit.LIGHT_BLADE));
    public static final DeferredToolPart<ToolPart> HEAVY_BLADE = register("heavy_blade", new ToolPart(ItemInit.HEAVY_BLADE));
    public static final DeferredToolPart<ToolPart> LIGHT_HANDLE = register("light_handle", new ToolPart(ItemInit.LIGHT_HANDLE));
    public static final DeferredToolPart<ToolPart> HEAVY_HANDLE = register("heavy_handle", new ToolPart(ItemInit.HEAVY_HANDLE));
    public static final DeferredToolPart<ToolPart> TOOL_BINDING = register("tool_binding", new ToolPart(ItemInit.TOOL_BINDING));
    public static final DeferredToolPart<ToolPart> HANDGUARD = register("handguard", new ToolPart(ItemInit.HANDGUARD));
    public static final DeferredToolPart<ToolPart> STRIKE_HEAD = register("strike_head", new ToolPart(ItemInit.STRIKE_HEAD));
    public static final DeferredToolPart<ToolPart> SPIKE_HEAD = register("spike_head", new ToolPart(ItemInit.SPIKE_HEAD));
    public static final DeferredToolPart<ToolPart> REINFORCED_ROD = register("reinforced_rod", new ToolPart(ItemInit.REINFORCED_ROD));
    public static final DeferredToolPart<ToolPart> PLATE = register("plate", new ToolPart(ItemInit.PLATE));
    public static final DeferredToolPart<ToolPart> CATALYST = register("catalyst", new ToolPart(ItemInit.CATALYST));
    public static final DeferredToolPart<ToolPart> CONDUCTOR = register("conductor", new ToolPart(ItemInit.CONDUCTOR));

    public static void registerToolParts(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Parts for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
