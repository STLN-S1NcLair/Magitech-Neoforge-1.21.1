package net.stln.magitech.item.tool;

public enum MiningLevel {
    NONE("none", 0),
    STONE("stone", 1),
    IRON("iron", 2),
    DIAMOND("diamond", 3),
    NETHERITE("netherite", 4);

    private final String id;
    private final int tier;

    MiningLevel(String id, int tier) {
        this.id = id;
        this.tier = tier;
    }

    public String get() {
        return this.id;
    }

    public int getTier() {
        return tier;
    }
}
