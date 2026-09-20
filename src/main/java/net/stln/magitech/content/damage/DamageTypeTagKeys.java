package net.stln.magitech.content.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class DamageTypeTagKeys {
    public static final TagKey<DamageType> BYPASSES_SHIELD = external("minecraft", "bypasses_shield");
    public static final TagKey<DamageType> NO_KNOCKBACK = external("minecraft", "no_knockback");
    public static final TagKey<DamageType> IS_MAGIC = external("neoforge", "is_magic");

    private DamageTypeTagKeys() {
    }

    private static TagKey<DamageType> external(String namespace, String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
