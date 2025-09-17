package net.stln.magitech.item.tool.upgrade;

import net.stln.magitech.item.tool.ToolStats;

public abstract class Upgrade {

    public ToolStats getUpgradeStats(int level) {
        return ToolStats.DEFAULT;
    }
}
