package net.stln.magitech.core.api.field_effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

/**
 * 1種類のフィールド影響とその強度を表します。
 * Represents one field influence and its intensity.
 */
public record FieldInfluence(@NotNull FieldInfluenceType type,  int intensity) {
    /**
     * 保存形式用の Codec です。
     * Codec used for persistent data serialization.
     */
    public static final Codec<FieldInfluence> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FieldInfluenceType.CODEC.fieldOf("type").forGetter(FieldInfluence::type),
            Codec.INT.fieldOf("intensity").forGetter(FieldInfluence::intensity)
    ).apply(instance, FieldInfluence::new));

    /**
     * ネットワーク同期用の StreamCodec です。
     * StreamCodec used for network synchronization.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FieldInfluence> STREAM_CODEC = StreamCodec.composite(
            FieldInfluenceType.STREAM_CODEC,
            FieldInfluence::type,
            ByteBufCodecs.INT,
            FieldInfluence::intensity,
            FieldInfluence::new
    );
}
