package net.stln.magitech.feature.magic.charge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellInit;

import java.util.HashMap;
import java.util.Optional;

public record ChargeData(Optional<Spell> spell, int chargeTicks) {

    public static final Codec<ChargeData> CODEC = RecordCodecBuilder.<ChargeData>create(instance -> instance.group(
            Spell.CODEC.optionalFieldOf("spell").forGetter(ChargeData::spell),
            Codec.INT.fieldOf("chargeTicks").forGetter(ChargeData::chargeTicks)
    ).apply(instance, ChargeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChargeData> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC.apply(ByteBufCodecs::optional), ChargeData::spell,
            ByteBufCodecs.VAR_INT, ChargeData::chargeTicks,
            ChargeData::new
    );

    public static ChargeData empty() {
        return new ChargeData(Optional.empty(), 0);
    }
}
