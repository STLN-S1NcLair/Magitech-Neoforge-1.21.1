package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.ToolStats;

public class SimpleUpgrade extends Upgrade {

    ToolStats upgradeStats;

    SimpleUpgrade(ToolStats upgradeStats) {
        this.upgradeStats = upgradeStats;
    }

    public ToolStats getModifiers(int level) {
        return ToolStats.mulWithoutElementCode(this.upgradeStats, level);
    }
}
