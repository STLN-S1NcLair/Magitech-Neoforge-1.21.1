package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.feature.tool.trait.TraitLike;
import org.jetbrains.annotations.NotNull;

public class DeferredTrait<T extends Trait> extends DeferredHolder<Trait, T> implements TraitLike {
    public DeferredTrait(ResourceKey<Trait> key) {
        super(key);
    }

    public DeferredTrait(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TRAIT, id));
    }

    @Override
    public @NotNull Trait asTrait() {
        return get();
    }
}
