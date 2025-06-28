package net.stln.magitech.loot;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

public class LootFunctionInit {
    public static final DeferredRegister<LootItemFunctionType<?>> FUNCTIONS = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, "magitech");

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<RandomThreadPageFunction>> RANDOM_THREAD_PAGE =
            FUNCTIONS.register("random_thread_page", () -> new LootItemFunctionType<>(RandomThreadPageFunction.CODEC));

    public static void registerFunctions(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Loot Functions for" + Magitech.MOD_ID);
        FUNCTIONS.register(eventBus);
    }
}
