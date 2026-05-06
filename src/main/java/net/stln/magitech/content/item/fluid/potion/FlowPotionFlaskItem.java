package net.stln.magitech.content.item.fluid.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

public class FlowPotionFlaskItem extends DrinkableFlaskItem {

    public FlowPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        entity.addEffect(new MobEffectInstance(MobEffectInit.FLOW_POWER, 2400, 0));
        EntityVFX.powerupAura(level, Element.FLOW, entity, Section.cover(), 40);
    }
}
