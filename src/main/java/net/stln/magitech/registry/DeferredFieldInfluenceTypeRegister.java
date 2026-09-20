package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredFieldInfluenceTypeRegister extends DeferredRegister<FieldInfluenceType> {
    public DeferredFieldInfluenceTypeRegister(String namespace) {
        super(MagitechRegistries.Keys.FIELD_INFLUENCE_TYPE, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FieldInfluenceType> @NotNull DeferredFieldInfluenceType<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredFieldInfluenceType<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends FieldInfluenceType> @NotNull DeferredFieldInfluenceType<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredFieldInfluenceType<I>) super.register(name, func);
    }

    @Override
    protected <I extends FieldInfluenceType> @NotNull DeferredFieldInfluenceType<I> createHolder(@NotNull ResourceKey<? extends Registry<FieldInfluenceType>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredFieldInfluenceType<>(key);
    }
}
