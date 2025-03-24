package net.stln.magitech.item.tool;

public enum ToolType {
    DAGGER("dagger"),
    LIGHT_SWORD("light_sword"),
    HEAVY_SWORD("heavy_sword"),
    PICKAXE("pickaxe"),
    HAMMER("hammer"),
    AXE("axe"),
    SHOVEL("shovel"),
    SCYTHE("scythe"),
    SPEAR("spear"),
    WAND("wand"),
    STAFF("staff");

    private final String id;

    ToolType(String id) {
        this.id = id;
    }

    public String get() {
        return this.id;
    }
}
