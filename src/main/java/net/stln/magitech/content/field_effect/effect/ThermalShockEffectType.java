package net.stln.magitech.content.field_effect.effect;

import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ThermalShockEffectType extends DefaultFieldEffectType {

    @Override
    public @NotNull FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEAT, 1), new FieldInfluence(FieldInfluenceInit.COOLING, 1));
    }

    @Override
    public @NotNull Color getPrimary() {
        return new Color(0xFFDDBE);
    }

    @Override
    public @NotNull Color getSecondary() {
        return new Color(0xB7E3FF);
    }
}
