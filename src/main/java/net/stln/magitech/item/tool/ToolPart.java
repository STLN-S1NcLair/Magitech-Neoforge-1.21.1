package net.stln.magitech.item.tool;

public enum ToolPart {
    LIGHT_BLADE("light_blade"),
    HEAVY_BLADE("heavy_blade"),
    LIGHT_HANDLE("light_handle"),
    HEAVY_HANDLE("heavy_handle"),
    TOOL_BINDING("tool_binding"),
    HANDGUARD("handguard"),
    STRIKE_HEAD("strike_head"),
    SPIKE_HEAD("spike_head"),
    REINFORCED_STICK("reinforced_stick"),
    PLATE("plate"),
    CATALYST("catalyst"),
    CONDUCTOR("conductor");

    private final String id;

    ToolPart(String id) {
        this.id = id;
    }

    public String get() {
        return this.id;
    }
}
