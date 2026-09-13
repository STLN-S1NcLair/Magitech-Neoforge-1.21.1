package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.trait.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredTraitRegister extends DeferredRegister<Trait> {
    public DeferredTraitRegister(String namespace) {
        super(MagitechRegistries.Keys.TRAIT, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Trait> @NotNull DeferredTrait<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredTrait<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Trait> @NotNull DeferredTrait<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredTrait<I>) super.register(name, func);
    }

    @Override
    protected <I extends Trait> @NotNull DeferredTrait<I> createHolder(@NotNull ResourceKey<? extends Registry<Trait>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredTrait<>(key);
    }
}
