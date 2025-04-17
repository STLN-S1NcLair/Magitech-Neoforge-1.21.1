package net.stln.magitech.magic.spell.glace;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Frozbeam extends Spell {
    @Override
    public Map<ManaUtil.ManaType, Double> getCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 10.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 1.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 2.0);
        return cost;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        ItemStack stack = user.getItemInHand(hand);
        Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
        Vec3 hitPos = EntityUtil.raycast(user, 63);
        Entity target = EntityUtil.raycastEntity(user, 63);
        Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
        EffectUtil.lineEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), hitPos.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                    hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
        }
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.FROST_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.6F + (user.getRandom().nextFloat() * 0.6F));


        if (target instanceof LivingEntity livingEntity) {

        ResourceKey<DamageType> damageType = DamageTypeInit.GLACE_DAMAGE;
        float damage = 6.0F;

        DamageSource ElementalDamageSource = stack.has(DataComponents.CUSTOM_NAME) ? user.damageSources().source(damageType, user) : user.damageSources().source(damageType);
            float targetHealth = livingEntity.getHealth();
            if (livingEntity instanceof Player player) {
                player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingEntity.getHealth()) * 10));
            }
            if (target.isAttackable()) {
                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.setLastHurtByMob(livingEntity);
                }
                damage *= EntityElementRegister.getElementAffinity(target, Element.GLACE).getMultiplier();
                target.hurt(ElementalDamageSource, damage);
            }
        }
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity) {

    }
}
