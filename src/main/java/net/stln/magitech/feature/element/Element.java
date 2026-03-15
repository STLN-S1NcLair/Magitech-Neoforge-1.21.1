package net.stln.magitech.feature.element;


import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.damage.DamageTypeInit;
import net.stln.magitech.content.damage.ElementAffinityRegister;
import net.stln.magitech.content.entity.status.AttributeInit;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Locale;
import java.util.Optional;

@net.neoforged.fml.common.asm.enumextension.NamedEnum(1)
@net.neoforged.fml.common.asm.enumextension.NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum Element implements StringRepresentable, IExtensibleEnum {
    MANA("mana", new Color(0xA0FFD0), new Color(0xA0FFA0), new Color(0x00F0D0), new Color(0x005060), DamageTypeInit.MANA_DAMAGE, Optional.empty()),
    EMBER("ember", new Color(0xFF2000), new Color(0xFFA000), new Color(0xFF2000), new Color(0x400000), DamageTypeInit.EMBER_DAMAGE, Optional.of(AttributeInit.EMBER_SPELL_POWER)),
    GLACE("glace", new Color(0x00FFF0), new Color(0x60FFE0), new Color(0x40A0FF), new Color(0x002840), DamageTypeInit.GLACE_DAMAGE, Optional.of(AttributeInit.GLACE_SPELL_POWER)),
    SURGE("surge", new Color(0x3000FF), new Color(0x3030FF), new Color(0x8080FF), new Color(0x000840), DamageTypeInit.SURGE_DAMAGE, Optional.of(AttributeInit.SURGE_SPELL_POWER)),
    PHANTOM("phantom", new Color(0xFFFF80), new Color(0xFFFF80), new Color(0xFFA000), new Color(0x403000), DamageTypeInit.PHANTOM_DAMAGE, Optional.of(AttributeInit.PHANTOM_SPELL_POWER)),
    TREMOR("tremor", new Color(0x008080), new Color(0x008080), new Color(0x004080), new Color(0x001020), DamageTypeInit.TREMOR_DAMAGE, Optional.of(AttributeInit.TREMOR_SPELL_POWER)),
    MAGIC("magic", new Color(0xFF00C0), new Color(0xFF20C0), new Color(0xFF0040), new Color(0x400020), DamageTypeInit.MAGIC_DAMAGE, Optional.of(AttributeInit.MAGIC_SPELL_POWER)),
    FLOW("flow", new Color(0xA0FF40), new Color(0x40FF40), new Color(0xE0FF20), new Color(0x104000), DamageTypeInit.FLOW_DAMAGE, Optional.of(AttributeInit.FLOW_SPELL_POWER)),
    HOLLOW("hollow", new Color(0xA000F0), new Color(0xA000F0), new Color(0x8000F0), new Color(0x200040), DamageTypeInit.HOLLOW_DAMAGE, Optional.of(AttributeInit.HOLLOW_SPELL_POWER)),
    LOGOS("logos", new Color(0x333333), new Color(0x333333), new Color(0x000000), new Color(0xA0A0A0), DamageTypeInit.LOGOS_DAMAGE, Optional.empty()),
    NONE("none", new Color(0xFFFFFF), new Color(0xFFFFFF), new Color(0xFFE0D0), new Color(0x404040), DamageTypes.PLAYER_ATTACK, Optional.empty());

    public static final Codec<Element> CODEC = StringRepresentable.fromEnum(Element::values);
    private final String id;
    private final Color textColor;
    private final Color primary;
    private final Color secondary;
    private final Color dark;
    private final ResourceKey<DamageType> damageType;
    private final Optional<Holder<Attribute>> powerAttribute;

    /**
     * Constructor for the Element enum, initializing all properties of the element.
     *
     * @param id             The string ID of the element, used for serialization and translation keys.
     * @param textColor      The text color of the element, used for UI.
     * @param primary        The primary color of the element, used for spell effects and UI.
     * @param secondary      The secondary color of the element, used for spell effects and UI.
     * @param dark           The dark color of the element, used for spell effects and UI.
     * @param damageType     The damage type associated with the element, used for damage calculation and interactions with entities.
     * @param powerAttribute The optional attribute that increases the power of spells of this element, used for damage calculation and spell scaling.
     */
    Element(String id, Color textColor, Color primary, Color secondary, Color dark, ResourceKey<DamageType> damageType, Optional<Holder<Attribute>> powerAttribute) {
        this.id = id;
        this.textColor = textColor;
        this.primary = primary;
        this.secondary = secondary;
        this.dark = dark;
        this.damageType = damageType;
        this.powerAttribute = powerAttribute;
    }

    public String get() {
        return this.id;
    }

    public MutableComponent getSpellElementName() {
        return Component.translatable("element.magitech." + this.id);
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getPrimary() {
        return primary;
    }

    public Color getSecondary() {
        return secondary;
    }

    public Color getDark() {
        return dark;
    }

    public ResourceKey<DamageType> getDamageType() {
        return damageType;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Optional<Holder<Attribute>> getPowerAttribute() {
        return powerAttribute;
    }

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

    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
    }
}
