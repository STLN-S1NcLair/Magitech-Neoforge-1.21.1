package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolTypeRegister extends DeferredRegister<ToolType> {
    public DeferredToolTypeRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_TYPE, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolType> @NotNull DeferredToolType<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolType<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolType> @NotNull DeferredToolType<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolType<I>) super.register(name, func);
    }

    @Override
    protected <I extends ToolType> @NotNull DeferredToolType<I> createHolder(@NotNull ResourceKey<? extends Registry<ToolType>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolType<>(key);
    }
}
