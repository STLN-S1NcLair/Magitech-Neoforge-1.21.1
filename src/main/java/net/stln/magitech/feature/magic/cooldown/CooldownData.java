package net.stln.magitech.feature.magic.cooldown;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.feature.magic.spell.ISpell;

import java.util.HashMap;
import java.util.List;

public record CooldownData(HashMap<ISpell, Cooldown> cooldowns) {

    public static final Codec<CooldownData> CODEC = Codec.unboundedMap(ISpell.CODEC, Cooldown.CODEC).xmap(HashMap::new, (hashMap -> hashMap)).xmap(CooldownData::new, CooldownData::cooldowns);

    public static final StreamCodec<RegistryFriendlyByteBuf, CooldownData> STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ISpell.STREAM_CODEC, Cooldown.STREAM_CODEC).map(CooldownData::new, CooldownData::cooldowns);

    public static CooldownData empty() {
        return new CooldownData(new java.util.HashMap<>());
    }

    public boolean isCooldown(ISpell spell) {
        return cooldowns.containsKey(spell);
    }

    public Cooldown get(ISpell spell) {
        return cooldowns.get(spell);
    }

    public void add(ISpell spell, int time) {
        cooldowns.put(spell, new Cooldown(time, time));
    }

    public void tick() {
        cooldowns.replaceAll((spell, cooldown) -> cooldown.tick());
        cooldowns.entrySet().removeIf(entry -> entry.getValue().remaining <= 0);
    }

    public record Cooldown(int length, int remaining) {

        public static final Codec<Cooldown> CODEC = Codec.INT.listOf(2, 2).xmap(integers -> new Cooldown(integers.get(0), integers.get(1)), cooldown -> List.of(cooldown.length, cooldown.remaining));
        public static final StreamCodec<ByteBuf, Cooldown> STREAM_CODEC = ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(integers -> new Cooldown(integers.get(0), integers.get(1)), cooldown -> List.of(cooldown.length, cooldown.remaining));

        public Cooldown tick() {
            return new Cooldown(this.length, this.remaining - 1);
        }

        public int progress() {
            return length - remaining;
        }
    }
}
