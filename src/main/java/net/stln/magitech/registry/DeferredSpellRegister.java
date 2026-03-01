package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.Spell;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredSpellRegister extends DeferredRegister<ISpell> {
    public DeferredSpellRegister(String namespace) {
        super(MagitechRegistries.Keys.SPELL, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ISpell> @NotNull DeferredSpell<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredSpell<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ISpell> @NotNull DeferredSpell<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredSpell<I>) super.register(name, func);
    }

    @Override
    protected <I extends ISpell> @NotNull DeferredSpell<I> createHolder(@NotNull ResourceKey<? extends Registry<ISpell>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredSpell<>(key);
    }
}
