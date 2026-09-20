package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldEffectTypeLike;
import org.jetbrains.annotations.NotNull;

public class DeferredFieldEffectType<T extends FieldEffectType> extends DeferredHolder<FieldEffectType, T> implements FieldEffectTypeLike {
    public DeferredFieldEffectType(ResourceKey<FieldEffectType> key) {
        super(key);
    }

    public DeferredFieldEffectType(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.FIELD_EFFECT_TYPE, id));
    }

    @Override
    public @NotNull FieldEffectType asFieldEffectType() {
        return get();
    }
}
