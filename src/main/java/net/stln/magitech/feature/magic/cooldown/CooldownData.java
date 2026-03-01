package net.stln.magitech.feature.magic.cooldown;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.feature.magic.spell.Spell;

import java.util.HashMap;

public record CooldownData(HashMap<Spell, Integer> cooldowns) {

    public static final Codec<CooldownData> CODEC = Codec.unboundedMap(Spell.CODEC, Codec.INT).xmap(HashMap::new, (hashMap -> hashMap)).xmap(CooldownData::new, CooldownData::cooldowns);

    public static final StreamCodec<RegistryFriendlyByteBuf, CooldownData> STREAM_CODEC = ByteBufCodecs.map(HashMap::new, Spell.STREAM_CODEC, ByteBufCodecs.INT).map(CooldownData::new, CooldownData::cooldowns);

    public static CooldownData empty() {
        return new CooldownData(new java.util.HashMap<>());
    }

    public void tick() {
        cooldowns.replaceAll((spell, cooldown) -> cooldown - 1);
        cooldowns.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }
}
