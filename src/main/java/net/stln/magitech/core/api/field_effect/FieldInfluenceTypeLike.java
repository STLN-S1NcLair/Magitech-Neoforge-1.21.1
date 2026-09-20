package net.stln.magitech.core.api.field_effect;

import org.jetbrains.annotations.NotNull;

/**
 * フィールド影響タイプへ変換できるオブジェクトの API です。
 * API for objects that can be converted to a field-influence type.
 */
@FunctionalInterface
public interface FieldInfluenceTypeLike {
    /**
     * 対応するフィールド影響タイプを返します。
     * Returns the corresponding field-influence type.
     */
    @NotNull FieldInfluenceType asFieldEffectType();
}
