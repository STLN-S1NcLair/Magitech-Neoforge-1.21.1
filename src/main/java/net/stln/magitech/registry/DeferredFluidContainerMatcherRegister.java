package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.fluid.FluidContainerMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredFluidContainerMatcherRegister extends DeferredRegister<FluidContainerMatcher> {
    public DeferredFluidContainerMatcherRegister(String namespace) {
        super(MagitechRegistries.Keys.FLUID_CONTAINER_MATCHER, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FluidContainerMatcher> @NotNull DeferredFluidContainerMatcher<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredFluidContainerMatcher<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FluidContainerMatcher> @NotNull DeferredFluidContainerMatcher<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredFluidContainerMatcher<I>) super.register(name, func);
    }

    @Override
    protected <I extends FluidContainerMatcher> @NotNull DeferredFluidContainerMatcher<I> createHolder(@NotNull ResourceKey<? extends Registry<FluidContainerMatcher>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredFluidContainerMatcher<>(key);
    }
}
