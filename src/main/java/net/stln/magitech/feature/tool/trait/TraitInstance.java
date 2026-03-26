package net.stln.magitech.feature.tool.trait;

import org.jetbrains.annotations.NotNull;

public record TraitInstance(@NotNull Trait trait, int level) {

    public static TraitInstance create(@NotNull Trait trait) {
        return new TraitInstance(trait, 1);
    }

    public TraitInstance addLevel(int value) {
        return new TraitInstance(trait, level + value);
    }

    public TraitInstance increment() {
        return addLevel(1);
    }
}
