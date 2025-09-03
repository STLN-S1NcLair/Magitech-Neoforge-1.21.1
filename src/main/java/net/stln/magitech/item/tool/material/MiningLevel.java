package net.stln.magitech.item.tool.material;

public enum MiningLevel {
    NONE("none", 0, 0x805830),
    STONE("stone", 1, 0x808080),
    IRON("iron", 2, 0xFFFFFF),
    DIAMOND("diamond", 3, 0x80F0FF),
    NETHERITE("netherite", 4, 0x705070);

    private final String id;
    private final int tier;
    private final int color;

    MiningLevel(String id, int tier, int color) {
        this.id = id;
        this.tier = tier;
        this.color = color;
    }

    public String get() {
        return this.id;
    }

    public int getTier() {
        return tier;
    }

    public int getColor() {
        return color;
    }
}
