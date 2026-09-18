package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

import java.awt.*;
import java.util.List;

public class ScorchingEffectType extends DefaultFieldEffectType {

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
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEAT.get(), 2));
    }

    @Override
    public void affectEntity(Entity entity) {
        if (entity instanceof LivingEntity && !entity.fireImmune() && entity.getRemainingFireTicks() < 40) {
            entity.setRemainingFireTicks(40);
        }
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFF5729);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xFF1259);
    }
}
