package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_group.ToolGroup;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolGroupRegister extends DeferredRegister<ToolGroup> {
    public DeferredToolGroupRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_GROUP, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolGroup> @NotNull DeferredToolGroup<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolGroup<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolGroup> @NotNull DeferredToolGroup<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolGroup<I>) super.register(name, func);
    }

    @Override
    protected <I extends ToolGroup> @NotNull DeferredToolGroup<I> createHolder(@NotNull ResourceKey<? extends Registry<ToolGroup>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolGroup<>(key);
    }
}
