package net.stln.magitech.element;


import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageType;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.damage.ElementAffinityRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Element implements StringRepresentable {
    NONE("none", 0xFFFFFF, 0x404040, 0xA0FFD0, 0x005060, DamageTypeInit.MANA_DAMAGE),
    EMBER("ember", 0xFF4040, 0x400000, 0xFF4040, 0x400000, DamageTypeInit.EMBER_DAMAGE),
    GLACE("glace", 0xA0FFFF, 0x002840, 0xA0FFFF, 0x002840, DamageTypeInit.GLACE_DAMAGE),
    SURGE("surge", 0x6070FF, 0x100040, 0x6070FF, 0x100040, DamageTypeInit.SURGE_DAMAGE),
    PHANTOM("phantom", 0xFFFFA0, 0x403000, 0xFFFFA0, 0x403000, DamageTypeInit.PHANTOM_DAMAGE),
    TREMOR("tremor", 0x008080, 0x001020, 0x008080, 0x001020, DamageTypeInit.TREMOR_DAMAGE),
    MAGIC("magic", 0xFF40C0, 0x400020, 0xFF40C0, 0x400020, DamageTypeInit.MAGIC_DAMAGE),
    FLOW("flow", 0xA0FF40, 0x104000, 0xA0FF40, 0x104000, DamageTypeInit.FLOW_DAMAGE),
    HOLLOW("hollow", 0x8020C0, 0x200040, 0x8020C0, 0x200040, DamageTypeInit.HOLLOW_DAMAGE);

    public static final Codec<Element> CODEC = StringRepresentable.fromEnum(Element::values);
    
    public static void registerElements() {
        Magitech.LOGGER.info("Registering Elements for" + Magitech.MOD_ID);
        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.INEFFICIENT, Element.EMBER);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.INEFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.INEFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.INEFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.INEFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.INEFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.INEFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.INEFFICIENT, Element.HOLLOW);

        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.EFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.EFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.EFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.EFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.EFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.EFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.EFFICIENT, Element.HOLLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.EFFICIENT, Element.EMBER);

        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.EFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.EFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.EFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.EFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.EFFICIENT, Element.HOLLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.EFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.EFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.EFFICIENT, Element.EMBER);

        // EntityElementRegister.registerEntityElement(Blaze.class, Element.EMBER);
        // EntityElementRegister.registerEntityElement(MagmaCube.class, Element.EMBER);
        // EntityElementRegister.registerEntityElement(Stray.class, Element.GLACE);
        // EntityElementRegister.registerEntityElement(Phantom.class, Element.PHANTOM);
        // EntityElementRegister.registerEntityElement(Vex.class, Element.PHANTOM);
        // EntityElementRegister.registerEntityElement(Warden.class, Element.TREMOR);
        // EntityElementRegister.registerEntityElement(Witch.class, Element.MAGIC);
        // EntityElementRegister.registerEntityElement(Evoker.class, Element.MAGIC);
        // EntityElementRegister.registerEntityElement(Drowned.class, Element.FLOW);
        // EntityElementRegister.registerEntityElement(Guardian.class, Element.FLOW);
        // EntityElementRegister.registerEntityElement(ElderGuardian.class, Element.FLOW);
        // EntityElementRegister.registerEntityElement(Breeze.class, Element.FLOW);
        // EntityElementRegister.registerEntityElement(EnderMan.class, Element.HOLLOW);
        // EntityElementRegister.registerEntityElement(EnderDragon.class, Element.HOLLOW);
        // EntityElementRegister.registerEntityElement(Endermite.class, Element.HOLLOW);
        // EntityElementRegister.registerEntityElement(Shulker.class, Element.HOLLOW);
    }
    
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

    public MutableComponent getSpellElementName() {
        if (this == Element.NONE) {
            return Component.translatable("element.magitech.mana");
        } else {
            return Component.translatable("element.magitech." + this.id);
        }
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

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
