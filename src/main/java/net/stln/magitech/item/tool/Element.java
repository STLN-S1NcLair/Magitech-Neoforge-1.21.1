package net.stln.magitech.item.tool;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.stln.magitech.damage.DamageTypeInit;

public enum Element {
    NONE("none", 0xFFFFFF, null),
    EMBER("ember", 0xFF4040, DamageTypeInit.EMBER_DAMAGE),
    GLACE("glace", 0xA0FFFF, DamageTypeInit.GLACE_DAMAGE),
    SURGE("surge", 0x6070FF, DamageTypeInit.SURGE_DAMAGE),
    PHANTOM("phantom", 0xFFFFA0, DamageTypeInit.PHANTOM_DAMAGE),
    TREMOR("tremor", 0x008080, DamageTypeInit.TREMOR_DAMAGE),
    MAGIC("magic", 0xFF40C0, DamageTypeInit.MAGIC_DAMAGE),
    FLOW("flow", 0xA0FF40, DamageTypeInit.FLOW_DAMAGE),
    HOLLOW("hollow", 0x8020C0, DamageTypeInit.HOLLOW_DAMAGE);

    private final String id;
    private final int color;
    private final ResourceKey<DamageType> damageType;

    Element(String id, int color, ResourceKey<DamageType> damageType) {
        this.id = id;
        this.color = color;
        this.damageType = damageType;
    }

    public String get() {
        return this.id;
    }

    public int getColor() {
        return color;
    }

    public ResourceKey<DamageType> getDamageType() {
        return damageType;
    }
}
