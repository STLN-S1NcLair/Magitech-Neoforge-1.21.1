package net.stln.magitech.item.tool.upgrade;

import com.mojang.serialization.Codec;
import net.stln.magitech.item.tool.ToolStats;

public abstract class Upgrade {

    public ToolStats getUpgradeStats(int level) {
        return ToolStats.DEFAULT;
    }
}
