package net.stln.magitech.magic.spell.surge;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.BeamSpell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fulgenza extends BeamSpell {

    public Fulgenza() {
        baseDamage = 18.0F;
        baseMaxRange = 63;
        beamradius = 1.0;
    }

    public Element getElement() {
        return Element.SURGE;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 75.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 10.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 7.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 200;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 80, this.getElement());
        int delay = (int) Math.round(80 / user.getAttributeValue(AttributeInit.CASTING_SPEED)) - 80;
        TickScheduler.schedule(Math.max(delay, 0), () -> {
            level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.FULGENZA_CHARGE.get(), SoundSource.PLAYERS, 1.0F, (float) 80 / (Math.round(80 / user.getAttributeValue(AttributeInit.CASTING_SPEED)) - Math.max(delay, 0)));
        }, level.isClientSide);
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

                if (target != null) {
                    if (!level.isClientSide) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                    }

                    Set<Entity> attackList = new HashSet<>();
                    attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, hitPos, new Vec3(12.0, 12.0, 12.0)));
                    attackList.remove(target);
                    for (Entity chainTarget : attackList) {
                        Vec3 targetBodyPos = chainTarget.position().add(0, chainTarget.getBbHeight() * 0.7, 0);
                        if (level.clip(new ClipContext(targetBodyPos, target.position().add(0, target.getBbHeight() * 0.7, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK) {
                            continue;
                        }

                        if (!level.isClientSide) {
                            this.applyDamage(baseDamage / 2, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, chainTarget);
                        }

                        level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), targetBodyPos.toVector3f(), 2F, 3, 0), hitPos.x, hitPos.y, hitPos.z,
                                0, 0, 0);
                        TickScheduler.schedule(5, () -> {
                            EffectUtil.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0), targetBodyPos, hitPos, 3, false);
                        }, level.isClientSide);
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

    @Override
    protected void applyEffectToLivingTarget(Level level, Player user, LivingEntity target) {
        super.applyEffectToLivingTarget(level, user, target);
    }

    @Override
    protected void playBeamSound(Level level, Player user) {
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.FULGENZA.get(), SoundSource.PLAYERS, 1.0F, 0.6F + (user.getRandom().nextFloat() * 0.6F));
    }

    @Override
    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
        EffectUtil.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0), start, hitPos, 2, false);
        for (int i = 0; i < 3; i++) {
            level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                            new Vector3f((float) (hitPos.x), (float) (hitPos.y), (float) (hitPos.z)), 1.0F, 1, 0),
                    start.x, start.y, start.z, 0, 0, 0);
        }
        level.addParticle(new BeamParticleEffect(new Vector3f(0.5F, 0.5F, 0.7F), new Vector3f(0.3F, 0.3F, 0.5F), hitPos.toVector3f(), 1.0F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.3F, 0.4F), new Vector3f(0.2F, 0.2F, 0.4F), hitPos.toVector3f(), 3.0F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                            new Vector3f((float) (hitPos.x + (user.getRandom().nextFloat() - 0.5)), (float) (hitPos.y + (user.getRandom().nextFloat() - 0.5)), (float) (hitPos.z + (user.getRandom().nextFloat() - 0.5))), 1.0F, 3, 0),
                    hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) * 2, (user.getRandom().nextFloat() - 0.5) * 2, (user.getRandom().nextFloat() - 0.5) * 2);
        }
    }
}
