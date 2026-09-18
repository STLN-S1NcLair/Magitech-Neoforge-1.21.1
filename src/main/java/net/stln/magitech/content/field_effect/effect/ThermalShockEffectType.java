package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

import java.awt.*;
import java.util.List;

public class ThermalShockEffectType extends DefaultFieldEffectType {

    @Override
    public boolean canProcess(Level level, List<ItemStack> inputs) {
        return false;
    }

    @Override
    public boolean canProcess(Level level, BlockPos pos) {
        return false;
    }

    @Override
    public FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEAT.get(), 1), new FieldInfluence(FieldInfluenceInit.COLD.get(), 1));
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFFF5F5);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xEDFFFF);
    }
}
