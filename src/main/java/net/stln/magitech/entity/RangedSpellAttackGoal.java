package net.stln.magitech.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

public class RangedSpellAttackGoal<T extends Mob & RangedAttackMob> extends Goal {
    private final T mob;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int attackInterval = 40;
    private int hardAttackInterval = 20;

    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public RangedSpellAttackGoal(T mob, double moveSpeedAmp, float maxAttackDistance, int attackInterval, int hardAttackInterval) {
        this.mob = mob;
        this.moveSpeedAmp = moveSpeedAmp;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.attackInterval = attackInterval;
        this.hardAttackInterval = hardAttackInterval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void stop() {
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }

            double d0 = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(target);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }

            if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                this.strafingTime++;
            } else {
                this.mob.getNavigation().moveTo(target, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                if (this.mob.getControlledVehicle() instanceof Mob mob) {
                    mob.lookAt(target, 30.0F, 30.0F);
                }

                this.mob.lookAt(target, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

        double distanceSq = this.mob.distanceToSqr(target);
        boolean canSee = this.mob.getSensing().hasLineOfSight(target);

        if (--this.attackTime <= 0) {
            if (canSee && distanceSq <= this.maxAttackDistance) {
                this.mob.performRangedAttack(target, 1.0F);
                this.attackTime = mob.level().getDifficulty() == Difficulty.HARD ? hardAttackInterval : attackInterval;
            }
        }
    }
}