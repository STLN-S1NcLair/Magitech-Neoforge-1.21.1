package net.stln.magitech.content.field_effect.influence;

import net.stln.magitech.core.api.field_effect.FieldInfluenceType;

import java.awt.Color;

public class CoolingInfluenceType extends FieldInfluenceType {
    @Override
    public Color getPrimary() {
        return new Color(0x81FFEA);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x58DEFF);
    }
}
