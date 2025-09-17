package net.stln.magitech.magic.spell.hollow;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.entity.mobeffect.MobEffectInit;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.BeamSpell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.VoidGlowParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voidlance extends BeamSpell {

    public Voidlance() {
        baseDamage = 7.0F;
        baseMaxRange = 63;
        beamradius = 0.1;
    }

    public Element getElement() {
        return Element.HOLLOW;
    }

    public SpellShape getSpellShape() {
        return SpellShape.BEAM;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 40.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 6.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 120;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 4, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        callSuperFinishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
                Vec3 hitPos = EntityUtil.raycastBeam(user, this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), beamradius);
                Entity target = EntityUtil.raycastBeamEntity(user, this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), beamradius);
                Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
                addVisualEffect(level, user, start, hitPos);
                playBeamSound(level, user);
                final List<Vec3>[] vec3s = new List[]{new ArrayList<>()};
                TickScheduler.schedule(2, () -> vec3s[0] = addBeam(stack, level, user, hitPos, 1), level.isClientSide);
                TickScheduler.schedule(4, () -> {
                    for (Vec3 hit : vec3s[0]) {
                        addBeam(stack, level, user, hit, 2);
                    }
                }, level.isClientSide);


                if (!level.isClientSide) {
                    if (target instanceof LivingEntity livingTarget) {
                        applyEffectToLivingTarget(level, user, livingTarget);
                    }
                    if (target != null) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                    }
                }
                addCooldown(level, user, stack);

                if (level.isClientSide) {
                    playShootAnimation(user);
                }
                addCooldown(level, user, stack);
            } else {
                ChargeData.removeCharge(user);
            }
        }
    }

    public List<Vec3> addBeam(ItemStack stack, Level level, Player user, Vec3 hitPos, int order) {
        List<Vec3> vec3s = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) != 1) {
                        continue;
                    }
                    Vec3 dir = new Vec3(i, j, k).normalize();
                    Vec3 hit = EntityUtil.raycastBeam(user, (double) 24 / Math.pow(order, 2), hitPos.add(dir.scale(0.5)), dir, beamradius);
                    Entity target = EntityUtil.raycastBeamEntity(user, (double) 24 / Math.pow(order, 2), hitPos.add(dir.scale(0.5)), dir, beamradius);
                    Vec3 start = hitPos.add(dir.scale(0.5));
                    EffectUtil.lineEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hit, 2, false);
                    level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), hit.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
                    for (int l = 0; l < 20; l++) {
                        level.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                                hit.x, hit.y, hit.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
                    }

                    if (!level.isClientSide) {
                        if (target instanceof LivingEntity livingTarget) {
                            livingTarget.addEffect(new MobEffectInstance(MobEffectInit.VOIDROT, 80, 0), livingTarget);
                        }
                        if (target != null) {
                            this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                        }
                    }
                    vec3s.add(hit);
                }
            }
        }
        level.playSound(user, hitPos.x(), hitPos.y(), hitPos.z(), SoundInit.VOIDLANCE.get(), SoundSource.PLAYERS, 1.0F, 0.6F + (user.getRandom().nextFloat() * 0.6F));
        return vec3s;
    }

    @Override
    protected void applyEffectToLivingTarget(Level level, Player user, LivingEntity target) {
        super.applyEffectToLivingTarget(level, user, target);
        target.addEffect(new MobEffectInstance(MobEffectInit.VOIDROT, 80, 0), target);
    }

    @Override
    protected void playBeamSound(Level level, Player user) {
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.VOIDLANCE.get(), SoundSource.PLAYERS, 1.0F, 0.6F + (user.getRandom().nextFloat() * 0.6F));
    }

    @Override
    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
        EffectUtil.lineEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), hitPos.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                    hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
