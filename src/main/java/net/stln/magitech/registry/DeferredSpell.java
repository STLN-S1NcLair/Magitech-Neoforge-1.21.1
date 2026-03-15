package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellLike;
import org.jetbrains.annotations.NotNull;

public class DeferredSpell<T extends ISpell> extends DeferredHolder<ISpell, T> implements SpellLike {
    public DeferredSpell(ResourceKey<ISpell> key) {
        super(key);
    }

    public DeferredSpell(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.SPELL, id));
    }

    @Override
    public @NotNull ISpell asSpell() {
        return get();
    }
}
