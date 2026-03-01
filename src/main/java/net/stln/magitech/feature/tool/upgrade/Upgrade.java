package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.ToolStats;

public abstract class Upgrade {

    public ToolStats getUpgradeStats(int level) {
        return ToolStats.DEFAULT;
    }
}
