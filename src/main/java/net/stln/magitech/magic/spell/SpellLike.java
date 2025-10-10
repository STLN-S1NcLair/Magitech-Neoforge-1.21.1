package net.stln.magitech.magic.spell;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SpellLike {
    @NotNull Spell asSpell();
}
