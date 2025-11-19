package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.fluid.FluidContainerMatcher;

public class DeferredFluidContainerMatcher<T extends FluidContainerMatcher> extends DeferredHolder<FluidContainerMatcher, T> {
    public DeferredFluidContainerMatcher(ResourceKey<FluidContainerMatcher> key) {
        super(key);
    }

    public DeferredFluidContainerMatcher(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.FLUID_CONTAINER_MATCHER, id));
    }
}
