package net.stln.magitech.content.field_effect.effect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColdEffectType extends DefaultFieldEffectType {

    @Override
    public @NotNull FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.COOLING.get(), 1));
    }

    @Override
    public void affectEntity(@NotNull Entity entity) {
        if (entity instanceof LivingEntity && entity.canFreeze() && entity.getTicksFrozen() < 100) {
            entity.setTicksFrozen(entity.getTicksFrozen() + 3);
        }
    }

    @Override
    public @NotNull Color getPrimary() {
        return new Color(0xA8FFEF);
    }

    @Override
    public @NotNull Color getSecondary() {
        return new Color(0x58DEFF);
    }
}
