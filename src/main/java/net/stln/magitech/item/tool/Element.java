package net.stln.magitech.item.tool;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.stln.magitech.damage.DamageTypeInit;

public enum Element {
    NONE("none", 0xFFFFFF, 0x404040, 0xA0FFD0, 0x005060, DamageTypeInit.MANA_DAMAGE),
    EMBER("ember", 0xFF4040, 0x400000, 0xFF4040, 0x400000, DamageTypeInit.EMBER_DAMAGE),
    GLACE("glace", 0xA0FFFF, 0x002840, 0xA0FFFF, 0x002840, DamageTypeInit.GLACE_DAMAGE),
    SURGE("surge", 0x6070FF, 0x100040, 0x6070FF, 0x100040, DamageTypeInit.SURGE_DAMAGE),
    PHANTOM("phantom", 0xFFFFA0, 0x403000, 0xFFFFA0, 0x403000, DamageTypeInit.PHANTOM_DAMAGE),
    TREMOR("tremor", 0x008080, 0x001020, 0x008080, 0x001020, DamageTypeInit.TREMOR_DAMAGE),
    MAGIC("magic", 0xFF40C0, 0x400020, 0xFF40C0, 0x400020, DamageTypeInit.MAGIC_DAMAGE),
    FLOW("flow", 0xA0FF40, 0x104000, 0xA0FF40, 0x104000, DamageTypeInit.FLOW_DAMAGE),
    HOLLOW("hollow", 0x8020C0, 0x200040, 0x8020C0, 0x200040, DamageTypeInit.HOLLOW_DAMAGE);

    private final String id;
    private final int color;
    private final int dark;
    private final int spellColor;
    private final int spellDark;
    private final ResourceKey<DamageType> damageType;

    Element(String id, int color, int dark, int spellColor, int spellDark, ResourceKey<DamageType> damageType) {
        this.id = id;
        this.color = color;
        this.dark = dark;
        this.spellColor = spellColor;
        this.spellDark = spellDark;
        this.damageType = damageType;
    }

    public String get() {
        return this.id;
    }

    public int getColor() {
        return color;
    }

    public int getDark() {
        return dark;
    }

    public int getSpellColor() {
        return spellColor;
    }

    public int getSpellDark() {
        return spellDark;
    }


    public ResourceKey<DamageType> getDamageType() {
        return damageType;
    }
}
