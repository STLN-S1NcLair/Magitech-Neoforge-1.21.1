package net.stln.magitech.content.field_effect.influence;

import net.stln.magitech.core.api.field_effect.FieldInfluenceType;

import java.awt.Color;

public class HeatInfluenceType extends FieldInfluenceType {
    @Override
    public Color getPrimary() {
        return new Color(0xFFAB44);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xFF4400);
    }
}
