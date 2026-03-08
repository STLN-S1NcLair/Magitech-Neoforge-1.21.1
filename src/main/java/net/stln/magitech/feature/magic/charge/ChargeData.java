package net.stln.magitech.feature.magic.charge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.feature.magic.spell.ISpell;

import java.util.List;
import java.util.Optional;

public record ChargeData(Optional<ISpell> spell, Charge charge) {

    public static final Codec<ChargeData> CODEC = RecordCodecBuilder.<ChargeData>create(instance -> instance.group(
            ISpell.CODEC.optionalFieldOf("spell").forGetter(ChargeData::spell),
            Charge.CODEC.fieldOf("charge").forGetter(ChargeData::charge)
    ).apply(instance, ChargeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChargeData> STREAM_CODEC = StreamCodec.composite(
            ISpell.STREAM_CODEC.apply(ByteBufCodecs::optional), ChargeData::spell,
            Charge.STREAM_CODEC, ChargeData::charge,
            ChargeData::new
    );

    public static ChargeData of(ISpell spell, int length) {
        return new ChargeData(Optional.ofNullable(spell), new Charge(length, length));
    }

    public ChargeData tick() {
        return new ChargeData(this.spell, this.charge.tick());
    }

    public static ChargeData empty() {
        return new ChargeData(Optional.empty(), new Charge(0, 0));
    }

    public record Charge(int length, int remaining) {

        public static final Codec<ChargeData.Charge> CODEC = Codec.INT.listOf(2, 2).xmap(integers -> new ChargeData.Charge(integers.get(0), integers.get(1)), cooldown -> List.of(cooldown.length, cooldown.remaining));
        public static final StreamCodec<ByteBuf, ChargeData.Charge> STREAM_CODEC = ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(integers -> new ChargeData.Charge(integers.get(0), integers.get(1)), cooldown -> List.of(cooldown.length, cooldown.remaining));

        public ChargeData.Charge tick() {
            return new ChargeData.Charge(this.length, this.remaining - 1);
        }

        public int progress() {
            return length - remaining;
        }
    }
}
