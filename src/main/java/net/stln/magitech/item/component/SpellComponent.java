package net.stln.magitech.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.magic.spell.Spell;

import java.util.List;

public record SpellComponent(HolderSet<Spell> spells, int selected) {
    public static final Codec<SpellComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(MagitechRegistries.Keys.SPELL).fieldOf("spells").forGetter(SpellComponent::spells),
            Codec.INT.fieldOf("selected").forGetter(SpellComponent::selected)
    ).apply(instance, SpellComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(MagitechRegistries.Keys.SPELL),
            SpellComponent::spells,
            ByteBufCodecs.INT,
            SpellComponent::selected,
            SpellComponent::new
    );

    public static final SpellComponent EMPTY = new SpellComponent(HolderSet.empty(), 0);

    public Holder<Spell> getSelectedHolder() {
        return spells.get(selected);
    }
    
    public Spell getSelectedSpell() {
        return getSelectedHolder().value();
    }
    
    public SpellComponent(List<Holder<Spell>> spells) {
        this(spells, 0);
    }

    public SpellComponent(List<Holder<Spell>> spells, int selected) {
        this(HolderSet.direct(spells), selected);
    }

    public SpellComponent setSelected(int selected) {
        return new SpellComponent(this.spells, selected);
    }
}
