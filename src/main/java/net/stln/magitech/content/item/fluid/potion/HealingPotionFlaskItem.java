package net.stln.magitech.content.item.fluid.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.awt.*;

public class HealingPotionFlaskItem extends DrinkableFlaskItem {

    public HealingPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        entity.heal(8.0F);
        EntityVFX.powerupAura(level, new Color(0x40FF80), new Color(0x80FF00), entity, Section.cover(), 40);
    }
}
