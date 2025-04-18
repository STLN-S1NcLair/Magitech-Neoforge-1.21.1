package net.stln.magitech.magic.spell.ember;

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
import net.stln.magitech.particle.particle_option.FlameParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ashveil extends Spell {
    @Override
    public Map<ManaUtil.ManaType, Double> getCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 10.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 3.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getTickCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 1.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 1.0);
        return cost;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        user.startUsingItem(hand);
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        Vec3 forward = Vec3.directionFromRotation(livingEntity.getRotationVector());
        Vec3 bodyPos = livingEntity.position().add(0, livingEntity.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(forward.scale(0.5));
        Vec3 center = livingEntity.getEyePosition().add(forward);
        Vec3 center2 = center.add(forward.scale(2));
        Set<Entity> attackList = new HashSet<>();
        attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center, new Vec3(3.0, 3.0, 3.0)));
        attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center2, new Vec3(4.0, 4.0, 4.0)));
        Vec3 startPos = offset;
        for (Entity entity : attackList) {
            Vec3 targetBodyPos = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
        }
        for (int i = 0; i < 5; i++) {
            level.addParticle(new FlameParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F), offset.x, offset.y, offset.z,
                    forward.x * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 3, forward.y * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 2, forward.z * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 2);
        }
        ResourceKey<DamageType> damageType = DamageTypeInit.EMBER_DAMAGE;
        float damage = 3.0F;

        DamageSource ElementalDamageSource = stack.has(DataComponents.CUSTOM_NAME) ? livingEntity.damageSources().source(damageType, livingEntity) : livingEntity.damageSources().source(damageType);

        float targetHealth = livingEntity.getHealth();
        if (livingEntity instanceof Player player) {
            if (remainingUseDuration % 5 == 0) {
                level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundInit.ZAP.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
            player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingEntity.getHealth()) * 10));
        }
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.setLastHurtByMob(livingEntity);
                }
                damage *= EntityElementRegister.getElementAffinity(target, Element.SURGE).getMultiplier();
                target.hurt(ElementalDamageSource, damage);
            }
        }
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity) {

    }
}
