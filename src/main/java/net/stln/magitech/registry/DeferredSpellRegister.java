package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.magic.spell.Spell;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredSpellRegister extends DeferredRegister<Spell> {
    public DeferredSpellRegister(String namespace) {
        super(MagitechRegistries.Keys.SPELL, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Spell> @NotNull DeferredSpell<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredSpell<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Spell> @NotNull DeferredSpell<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredSpell<I>) super.register(name, func);
    }

    @Override
    protected <I extends Spell> @NotNull DeferredSpell<I> createHolder(@NotNull ResourceKey<? extends Registry<Spell>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredSpell<>(key);
    }
}
