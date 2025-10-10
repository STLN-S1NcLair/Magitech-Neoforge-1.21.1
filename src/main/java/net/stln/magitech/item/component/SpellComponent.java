package net.stln.magitech.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellLike;

import java.util.List;

public record SpellComponent(List<Spell> spells, int selected) {
    public static final Codec<SpellComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Spell.CODEC.listOf().fieldOf("spells").forGetter(SpellComponent::spells),
            Codec.INT.fieldOf("selected").forGetter(SpellComponent::selected)
    ).apply(instance, SpellComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellComponent> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SpellComponent::spells,
            ByteBufCodecs.INT,
            SpellComponent::selected,
            SpellComponent::new
    );

    public static final SpellComponent EMPTY = new SpellComponent(List.of(), 0);
    
    public Spell getSelectedSpell() {
        return spells.get(selected);
    }

    public SpellComponent(List<SpellLike> spells) {
        this(spells.stream().map(SpellLike::asSpell).toList(), 0);
    }

    public SpellComponent setSelected(int selected) {
        return new SpellComponent(this.spells, selected);
    }
}
