package net.stln.magitech.item.fluid.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

public class EmberPotionFlaskItem extends DrinkableFlaskItem {

    public EmberPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        entity.addEffect(new MobEffectInstance(MobEffectInit.EMBER_POWER, 2400, 0));
        EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 0.5F, 0.3F), new Vector3f(1.0F, 0.1F, 0.0F), 1F, 1, 0, 15, 1.0F), entity, 20);
    }
}
