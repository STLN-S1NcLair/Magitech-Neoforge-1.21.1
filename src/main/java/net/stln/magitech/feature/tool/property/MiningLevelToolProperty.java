package net.stln.magitech.feature.tool.property;

import net.stln.magitech.feature.tool.material.MiningLevel;

import java.awt.*;

public class MiningLevelToolProperty extends ToolProperty<MiningLevel> {

    public MiningLevelToolProperty(Color color) {
        super(color);
    }

    // 最高Tierを返す
    @Override
    public MiningLevel add(MiningLevel a, MiningLevel b) {
        return a.getTier() > b.getTier() ? a : b;
    }

    @Override
    public MiningLevel mul(MiningLevel a, MiningLevel b) {
        return a.getTier() > b.getTier() ? a : b;
    }

    @Override
    public MiningLevel addIdentity() {
        return MiningLevel.NONE;
    }

    @Override
    public MiningLevel mulIdentity() {
        return MiningLevel.NONE;
    }
}
