package net.stln.magitech.item.tool;

import org.jetbrains.annotations.NotNull;

public enum ToolType {
    DAGGER("dagger", 3),
    LIGHT_SWORD("light_sword", 4),
    HEAVY_SWORD("heavy_sword", 4),
    PICKAXE("pickaxe", 3),
    HAMMER("hammer", 4),
    AXE("axe", 4),
    SHOVEL("shovel", 4),
    SCYTHE("scythe", 4),
    SPEAR("spear", 4),
    WAND("wand", 4),
    STAFF("staff", 5);

    private final String id;
    private final int size;

    ToolType(String id, int size) {
        this.id = id;
        this.size = size;
    }

    public @NotNull String getId() {
        return this.id;
    }

    public int getSize() {
        return size;
    }
}
