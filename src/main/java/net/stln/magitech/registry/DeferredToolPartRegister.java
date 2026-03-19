package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.part.ToolPart;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredToolPartRegister extends DeferredRegister<ToolPart> {
    public DeferredToolPartRegister(String namespace) {
        super(MagitechRegistries.Keys.TOOL_PART, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolPart> @NotNull DeferredToolPart<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredToolPart<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ToolPart> @NotNull DeferredToolPart<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredToolPart<I>) super.register(name, func);
    }

    @Override
    protected <I extends ToolPart> @NotNull DeferredToolPart<I> createHolder(@NotNull ResourceKey<? extends Registry<ToolPart>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredToolPart<>(key);
    }
}
