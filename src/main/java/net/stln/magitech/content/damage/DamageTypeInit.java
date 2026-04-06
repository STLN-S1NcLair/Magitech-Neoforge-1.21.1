package net.stln.magitech.content.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.stln.magitech.Magitech;

public class DamageTypeInit {

    public static final ResourceKey<DamageType> MANA_DAMAGE = create("mana");
    public static final ResourceKey<DamageType> EMBER_DAMAGE = create("ember");
    public static final ResourceKey<DamageType> GLACE_DAMAGE = create("glace");
    public static final ResourceKey<DamageType> SURGE_DAMAGE = create("surge");
    public static final ResourceKey<DamageType> PHANTOM_DAMAGE = create("phantom");
    public static final ResourceKey<DamageType> TREMOR_DAMAGE = create("tremor");
    public static final ResourceKey<DamageType> MAGIC_DAMAGE = create("magic");
    public static final ResourceKey<DamageType> FLOW_DAMAGE = create("flow");
    public static final ResourceKey<DamageType> HOLLOW_DAMAGE = create("hollow");
    public static final ResourceKey<DamageType> LOGOS_DAMAGE = create("logos");

    public static final ResourceKey<DamageType> MANA_BERRY_BUSH = create("mana_berry_bush");

    private static ResourceKey<DamageType> create(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Magitech.id(path));
    }
}
