package net.stln.magitech.core.api.field_effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.stln.magitech.MagitechRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * フィールド効果の検索とキャッシュを補助します。
 * Provides lookup and caching helpers for field effects.
 */
public class FieldEffectHelper {

    private FieldEffectHelper() {
    }

    /**
     * 影響の組み合わせから対応する効果タイプへのキャッシュです。
     * Cache mapping influence combinations to their corresponding effect types.
     */
    public static final Map<FieldInfluenceInstance, FieldEffectType> INFLUENCE_TO_TYPE = new HashMap<>();

    /**
     * 対象チャンクが未ロードの場合に処理をスキップすべきか判定します。
     * Determines whether processing should be skipped when the target chunk is not loaded.
     *
     * @param level 対象ワールド / target level
     * @param pos 対象位置 / target position
     * @return チャンクが未ロードならtrue / true when the chunk is not loaded
     */
    public static boolean shouldSkip(Level level, BlockPos pos) {
        return !level.hasChunkAt(pos);
    }

    /**
     * 影響インスタンスに対応するフィールド効果タイプを取得します。
     * Gets the field-effect type corresponding to an influence instance.
     *
     * @param level 効果タイプを登録しているワールド / level containing the registered effect types
     * @param instance 検索対象の影響インスタンス / influence instance to look up
     * @return 対応するタイプ、存在しない場合はnull / the matching type, or null if none exists
     */
    public static FieldEffectType getFieldEffect(Level level, FieldInfluenceInstance instance) {
        FieldEffectType exactType = findExactFieldEffect(instance);
        if (exactType != null || instance == null || instance.fieldInfluences() == null
                || instance.fieldInfluences().isEmpty()) {
            return exactType;
        }

        int maximumIntensity = Integer.MIN_VALUE;
        for (FieldInfluence influence : instance.fieldInfluences()) {
            if (influence != null && influence.type() != null) {
                maximumIntensity = Math.max(maximumIntensity, influence.intensity());
            }
        }
        if (maximumIntensity <= 0 || maximumIntensity == Integer.MIN_VALUE) {
            return null;
        }

        Set<FieldInfluenceType> strongestTypes = new HashSet<>();
        for (FieldInfluence influence : instance.fieldInfluences()) {
            if (influence != null && influence.type() != null && influence.intensity() == maximumIntensity) {
                strongestTypes.add(influence.type());
            }
        }

        for (int intensity = maximumIntensity; intensity > 0; intensity--) {
            Set<FieldInfluence> fallbackInfluences = new HashSet<>();
            for (FieldInfluenceType type : strongestTypes) {
                fallbackInfluences.add(new FieldInfluence(type, intensity));
            }

            FieldEffectType fallbackType = findExactFieldEffect(new FieldInfluenceInstance(fallbackInfluences));
            if (fallbackType != null) {
                FieldEffectHelper.INFLUENCE_TO_TYPE.put(instance, fallbackType);
                return fallbackType;
            }
        }
        return null;
    }

    /**
     * 影響集合に対する完全一致だけを検索します。
     * Searches only for an exact match for an influence set.
     *
     * @param instance 検索対象の影響集合 / influence set to search
     * @return 完全一致した効果、無ければnull / exact effect, or null when none exists
     */
    private static FieldEffectType findExactFieldEffect(FieldInfluenceInstance instance) {
        FieldEffectType type = FieldEffectHelper.INFLUENCE_TO_TYPE.get(instance);
        if (type != null) {
            return type;
        }

        for (FieldEffectType candidate : MagitechRegistries.FIELD_EFFECT_TYPE) {
            if (candidate.getCondition().equals(instance)) {
                FieldEffectHelper.INFLUENCE_TO_TYPE.put(instance, candidate);
                return candidate;
            }
        }
        return null;
    }
}
