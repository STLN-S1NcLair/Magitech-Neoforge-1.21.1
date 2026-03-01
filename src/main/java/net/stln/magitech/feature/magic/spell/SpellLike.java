package net.stln.magitech.feature.magic.spell;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SpellLike {
    @NotNull ISpell asSpell();
}
