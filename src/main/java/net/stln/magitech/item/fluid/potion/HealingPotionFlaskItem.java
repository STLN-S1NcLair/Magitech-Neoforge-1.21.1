package net.stln.magitech.item.fluid.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

public class HealingPotionFlaskItem extends DrinkableFlaskItem {

    public HealingPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        entity.heal(8.0F);
        EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.1F), 1F, 1, 0, 15, 1.0F), entity, 20);
    }
}
