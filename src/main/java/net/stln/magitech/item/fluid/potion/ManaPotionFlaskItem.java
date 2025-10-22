package net.stln.magitech.item.fluid.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

public class ManaPotionFlaskItem extends DrinkableFlaskItem {

    public ManaPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
            double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);

            ManaUtil.setMana(player, ManaUtil.ManaType.MANA, Math.min(currentMana + 90, maxMana));
        }
        EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0), entity, 20);
        entity.addEffect(new MobEffectInstance(MobEffectInit.MANA_REGENERATION, 300, 1));
    }
}
