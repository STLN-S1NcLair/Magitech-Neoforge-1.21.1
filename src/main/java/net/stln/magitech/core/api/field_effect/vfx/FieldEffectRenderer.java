package net.stln.magitech.core.api.field_effect.vfx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.*;
import net.stln.magitech.core.api.field_effect.data.FieldEffectClientCache;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * クライアント側のフィールド効果描画を仲介します。
 * Coordinates client-side rendering of field effects.
 */
public final class FieldEffectRenderer {

	private FieldEffectRenderer() {
	}

    /**
     * キャッシュされた範囲内の描画対象位置を処理します。
     * Processes renderable positions in cached ranges.
     */
	public static void forEachRenderablePos(@NotNull Level level) {

		FieldEffectClientCache.getInstance().forEachPosInRanges(level, (pos, instance) -> {
			if (FieldEffectHelper.shouldSkip(level, pos)) {
				return;
			}
			FieldEffectType type = FieldEffectHelper.getFieldEffect(level, instance);
			if (type != null) {
				type.renderVFX(level, pos);
			}
		});
	}
}
