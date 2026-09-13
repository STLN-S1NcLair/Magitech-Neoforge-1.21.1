package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.ColoredFieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.effect.visual.preset.BlockVFX;

import java.awt.*;
import java.util.List;

public abstract class DefaultFieldEffectType extends ColoredFieldEffectType {

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
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.COLD.get(), 1));
    }

    @Override
    public void renderVFX(Level level, BlockPos pos) {
        BlockVFX.fieldEffect(level, getPrimary(), getSecondary(), pos, 0.02F);
    }
}
