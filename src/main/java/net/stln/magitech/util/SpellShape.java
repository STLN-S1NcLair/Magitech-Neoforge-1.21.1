package net.stln.magitech.util;

public enum SpellShape {
    SHOT("shot", 0xFFFFFF, 0x808080),
    BEAM("beam", 0xFFFFFF, 0x808080),
    SPRAY("spray", 0xFFFFFF, 0x808080),
    FRAGMENT("fragment", 0xFFFFFF, 0x808080),
    DASH("dash", 0xFFFFFF, 0x808080),
    FIELD("field", 0xFFFFFF, 0x808080),
    BOMB("bomb", 0xFFFFFF, 0x808080),
    CHARGE("charge", 0xFFFFFF, 0x808080),
    ENHANCE("enhance", 0xFFFFFF, 0x808080),
    WEAKEN("weaken", 0xFFFFFF, 0x808080),
    INFUSE("infuse", 0xFFFFFF, 0x808080),
    RESILIENCE("resilience", 0xFFFFFF, 0x808080),
    UTILITY("utility", 0xFFFFFF, 0x808080);

    private final String id;
    private final int color;
    private final int dark;

    SpellShape(String id, int color, int dark) {
        this.id = id;
        this.color = color;
        this.dark = dark;
    }

    public String get() {
        return this.id;
    }

    public int getColor() {
        return color;
    }

    public int getDark() {
        return dark;
    }
}
