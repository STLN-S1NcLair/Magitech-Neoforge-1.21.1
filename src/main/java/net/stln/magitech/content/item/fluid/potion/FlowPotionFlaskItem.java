package net.stln.magitech.content.item.fluid.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.vfx.particle.particle_option.PowerupParticleEffect;
import org.joml.Vector3f;

public class FlowPotionFlaskItem extends DrinkableFlaskItem {

    public FlowPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        entity.addEffect(new MobEffectInstance(MobEffectInit.FLOW_POWER, 2400, 0));
        EffectHelper.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.8F, 1.0F, 0.5F), new Vector3f(0.3F, 1.0F, 0.3F), 1F, 1, 0, 15, 1.0F), entity, 20);
    }
}
