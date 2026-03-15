package net.stln.magitech.feature.magic.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface ISummonEntitySpell {

    // ヒット対象に与える効果
    default void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {

    }
}
