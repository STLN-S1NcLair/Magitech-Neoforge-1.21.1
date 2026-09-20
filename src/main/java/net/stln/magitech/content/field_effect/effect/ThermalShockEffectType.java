package net.stln.magitech.content.field_effect.effect;

import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

import java.awt.*;

public class ThermalShockEffectType extends DefaultFieldEffectType {

    @Override
    public FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEAT.get(), 1), new FieldInfluence(FieldInfluenceInit.COOLING.get(), 1));
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFFDDBE);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xB7E3FF);
    }
}
