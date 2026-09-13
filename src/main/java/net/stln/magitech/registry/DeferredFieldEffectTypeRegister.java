package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredFieldEffectTypeRegister extends DeferredRegister<FieldEffectType> {
    public DeferredFieldEffectTypeRegister(String namespace) {
        super(MagitechRegistries.Keys.FIELD_EFFECT_TYPE, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FieldEffectType> @NotNull DeferredFieldEffectType<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredFieldEffectType<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FieldEffectType> @NotNull DeferredFieldEffectType<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredFieldEffectType<I>) super.register(name, func);
    }

    @Override
    protected <I extends FieldEffectType> @NotNull DeferredFieldEffectType<I> createHolder(@NotNull ResourceKey<? extends Registry<FieldEffectType>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredFieldEffectType<>(key);
    }
}
