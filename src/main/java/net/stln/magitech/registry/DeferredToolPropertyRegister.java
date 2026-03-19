package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.property.IToolProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolPropertyRegister extends DeferredRegister<IToolProperty<?>> {
    public DeferredToolPropertyRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_PROPERTY, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends IToolProperty<?>> @NotNull DeferredToolProperty<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolProperty<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends IToolProperty<?>> @NotNull DeferredToolProperty<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolProperty<I>) super.register(name, func);
    }

    @Override
    protected <I extends IToolProperty<?>> @NotNull DeferredToolProperty<I> createHolder(@NotNull ResourceKey<? extends Registry<IToolProperty<?>>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolProperty<>(key);
    }
}
