package net.stln.magitech.core.api.field_effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * 同時に適用されるフィールド影響の集合です。
 * A set of field influences applied together.
 */
public record FieldInfluenceInstance(@NotNull Set<FieldInfluence> fieldInfluences) {

    /**
     * 保存形式用の Codec です。
     * Codec used for persistent data serialization.
     */
    public static final Codec<FieldInfluenceInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FieldInfluence.CODEC.listOf().fieldOf("field_effects").forGetter((inst) -> inst.fieldInfluences().stream().toList())
    ).apply(instance, FieldInfluenceInstance::new));

    /**
     * ネットワーク同期用の StreamCodec です。
     * StreamCodec used for network synchronization.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FieldInfluenceInstance> STREAM_CODEC = StreamCodec.composite(
            FieldInfluence.STREAM_CODEC.apply(ByteBufCodecs.list()),
            (FieldInfluenceInstance inst) -> inst.fieldInfluences().stream().toList(),
            FieldInfluenceInstance::new
    );

    /**
     * 影響のリストからインスタンスを生成します。
     * Creates an instance from a list of influences.
     */
    public FieldInfluenceInstance(@NotNull List<FieldInfluence> fieldEffects) {
        this(Set.copyOf(fieldEffects));
    }

    /**
     * 可変長引数から影響インスタンスを生成します。
     * Creates an influence instance from varargs.
     */
    public static FieldInfluenceInstance of(@NotNull FieldInfluence... fieldEffects) {
        return new FieldInfluenceInstance(Set.of(fieldEffects));
    }

    /**
     * 内部集合を複製したインスタンスを返します。
     * Returns an instance with a copied internal set.
     */
    public FieldInfluenceInstance copy() {
        return new FieldInfluenceInstance(Set.copyOf(fieldInfluences));
    }

    /**
     * 影響集合の内容に基づいて等価性を判定します。
     * Compares instances by the contents of their influence sets.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FieldInfluenceInstance(Set<FieldInfluence> effects))) return false;
        return fieldInfluences.equals(effects);
    }
}
