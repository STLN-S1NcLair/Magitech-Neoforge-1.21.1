package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellLike;
import org.jetbrains.annotations.NotNull;

public class DeferredSpell<T extends Spell> extends DeferredHolder<Spell, T> implements SpellLike {
    public DeferredSpell(ResourceKey<Spell> key) {
        super(key);
    }

    public DeferredSpell(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.SPELL, id));
    }

    @Override
    public @NotNull Spell asSpell() {
        return get();
    }
}
