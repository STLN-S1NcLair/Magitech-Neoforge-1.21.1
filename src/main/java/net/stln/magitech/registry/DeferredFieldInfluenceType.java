package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceTypeLike;
import org.jetbrains.annotations.NotNull;

public class DeferredFieldInfluenceType<T extends FieldInfluenceType> extends DeferredHolder<FieldInfluenceType, T> implements FieldInfluenceTypeLike {
    public DeferredFieldInfluenceType(ResourceKey<FieldInfluenceType> key) {
        super(key);
    }

    public DeferredFieldInfluenceType(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.FIELD_INFLUENCE_TYPE, id));
    }

    @Override
    public @NotNull FieldInfluenceType asFieldEffectType() {
        return get();
    }
}
