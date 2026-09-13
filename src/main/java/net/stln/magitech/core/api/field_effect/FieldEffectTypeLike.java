package net.stln.magitech.core.api.field_effect;

import org.jetbrains.annotations.NotNull;

/**
 * フィールド効果タイプへ変換できるオブジェクトの API です。
 * API for objects that can be converted to a field-effect type.
 */
@FunctionalInterface
public interface FieldEffectTypeLike {
    /**
     * 対応するフィールド効果タイプを返します。
     * Returns the corresponding field-effect type.
     */
    @NotNull FieldEffectType asFieldEffectType();
}
