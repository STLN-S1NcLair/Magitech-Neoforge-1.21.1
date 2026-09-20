package net.stln.magitech.core.api.field_effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

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
     * この影響のGUIアイコンテクスチャを返します。
     * Returns the GUI icon texture for this influence.
     */
    public @Nullable ResourceLocation getIconTexture() {
        ResourceLocation key = MagitechRegistries.FIELD_INFLUENCE_TYPE.getKey(this);
        return key == null
                ? null
                : ResourceLocation.fromNamespaceAndPath(
                        key.getNamespace(),
                        "textures/field_influence/" + key.getPath() + ".png"
                );
    }

    /**
     * この影響の主色を返します。
     * Returns the primary color of this influence.
     */
    public Color getPrimary() {
        return Color.WHITE;
    }

    /**
     * この影響の副色を返します。
     * Returns the secondary color of this influence.
     */
    public Color getSecondary() {
        return Color.WHITE;
    }

    /**
     * このオブジェクトをフィールド効果タイプとして返します。
     * Returns this object as a field-effect type.
     */
    @Override
    public @NotNull FieldInfluenceType asFieldEffectType() {
        return this;
    }
}
