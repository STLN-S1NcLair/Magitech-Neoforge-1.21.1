package net.stln.magitech.item.tool.upgrade;

import net.stln.magitech.item.tool.ToolStats;

public class SimpleUpgrade extends Upgrade {

    ToolStats upgradeStats;

    SimpleUpgrade(ToolStats upgradeStats) {
        this.upgradeStats = upgradeStats;
    }

    public ToolStats getUpgradeStats(int level) {
        return ToolStats.mulWithoutElementCode(this.upgradeStats, level);
    }
}
