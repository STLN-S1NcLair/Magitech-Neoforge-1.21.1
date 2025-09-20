package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.tool.material.ToolMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolMaterialRegister extends DeferredRegister<ToolMaterial> {
    public DeferredToolMaterialRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_MATERIAL, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolMaterial> @NotNull DeferredToolMaterial<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolMaterial<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolMaterial> @NotNull DeferredToolMaterial<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolMaterial<I>) super.register(name, func);
    }

    @Override
    protected <I extends ToolMaterial> @NotNull DeferredToolMaterial<I> createHolder(@NotNull ResourceKey<? extends Registry<ToolMaterial>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolMaterial<>(key);
    }
}
