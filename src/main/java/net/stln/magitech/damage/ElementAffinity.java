package net.stln.magitech.damage;

public enum ElementAffinity {
    EFFICIENT(2.0F),
    INEFFICIENT(0.5F),
    NORMAL(1.0F);

    final float multiplier;

    ElementAffinity(float multiplier) {
        this.multiplier = multiplier;
    }

    public float getMultiplier() {
        return multiplier;
    }
}
