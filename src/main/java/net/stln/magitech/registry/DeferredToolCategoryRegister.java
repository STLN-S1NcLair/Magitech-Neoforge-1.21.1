package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolCategoryRegister extends DeferredRegister<ToolCategory> {
    public DeferredToolCategoryRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_CATEGORY, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolCategory> @NotNull DeferredToolCategory<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolCategory<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolCategory> @NotNull DeferredToolCategory<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolCategory<I>) super.register(name, func);
    }

    @Override
    protected <I extends ToolCategory> @NotNull DeferredToolCategory<I> createHolder(@NotNull ResourceKey<? extends Registry<ToolCategory>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolCategory<>(key);
    }
}
