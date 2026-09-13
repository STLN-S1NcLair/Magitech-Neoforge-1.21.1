package net.stln.magitech.core.api.field_effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.MagitechRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * フィールド影響の種類を定義する基底クラスです。
 * Base class defining a type of field influence.
 */
public class FieldInfluenceType implements FieldInfluenceTypeLike {

    /**
     * 登録名を使う保存用 Codec です。
     * Persistent-data Codec based on registry names.
     */
    public static final Codec<FieldInfluenceType> CODEC = MagitechRegistries.FIELD_INFLUENCE_TYPE.byNameCodec();
    /**
     * レジストリ値を使うネットワーク用 StreamCodec です。
     * Network StreamCodec based on registry values.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FieldInfluenceType> STREAM_CODEC = ByteBufCodecs.registry(MagitechRegistries.Keys.FIELD_INFLUENCE_TYPE);

    /**
     * このオブジェクトをフィールド効果タイプとして返します。
     * Returns this object as a field-effect type.
     */
    @Override
    public @NotNull FieldInfluenceType asFieldEffectType() {
        return this;
    }
}
