package net.stln.magitech.feature.tool.trait;

import org.jetbrains.annotations.NotNull;

public record TraitInstance(@NotNull Trait trait, int level) {

    public static TraitInstance create(@NotNull Trait trait) {
        return new TraitInstance(trait, 1);
    }

    public TraitInstance addLevel(int value) {
        int maxLevel = trait.getMaxLevel();
        return new TraitInstance(trait, Math.min(level + value, maxLevel == -1 ? Integer.MAX_VALUE: maxLevel));
    }

    public TraitInstance increment() {
        return addLevel(1);
    }
}
