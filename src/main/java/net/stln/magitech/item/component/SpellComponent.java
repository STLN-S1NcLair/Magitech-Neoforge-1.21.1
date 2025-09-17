package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.magic.spell.Spell;

import java.util.List;

public record SpellComponent(List<Spell> spells, int selected) {

    public static final Codec<SpellComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Spell.CODEC.listOf().fieldOf("spells").forGetter(SpellComponent::spells),
                    Codec.INT.fieldOf("selected").forGetter(SpellComponent::selected)
            ).apply(instance, SpellComponent::new)
    );
    public static final StreamCodec<ByteBuf, SpellComponent> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SpellComponent::spells,
            ByteBufCodecs.INT,
            SpellComponent::selected,
            SpellComponent::new
    );
    public static final SpellComponent EMPTY = new SpellComponent(List.of(), 0);
    
    public SpellComponent(List<Spell> spells) {
        this(spells, 0);
    }
}

