package net.stln.magitech.content.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.content.damage.DamageTypeInit;
import net.stln.magitech.content.entity.RangedSpellAttackGoal;
import net.stln.magitech.content.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.content.network.RangedEntityAttackPayload;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.*;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;

public class WeaverEntity extends Monster implements GeoEntity, RangedAttackMob {

    private static final int HARD_ATTACK_INTERVAL = 20;
    private static final int NORMAL_ATTACK_INTERVAL = 40;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation DAMAGE = RawAnimation.begin().thenPlay("damage");
    private static final RawAnimation SPELL = RawAnimation.begin().thenPlay("spell");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WeaverEntity(EntityType<? extends WeaverEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RangedSpellAttackGoal<>(this, 1.0, 15.0F, NORMAL_ATTACK_INTERVAL, HARD_ATTACK_INTERVAL)); // 魔法攻撃専用ゴール
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getControlledVehicle() instanceof PathfinderMob pathfindermob) {
            this.yBodyRot = pathfindermob.yBodyRot;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    protected int getHardAttackInterval() {
        return 20;
    }

    protected int getAttackInterval() {
        return 40;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {

        int index = random.nextInt(3);
        performSpell(level(), target, index);

        if (!level().isClientSide) {
            PacketDistributor.sendToAllPlayers(new RangedEntityAttackPayload(this.getId(), target.getId(), distanceFactor, index));
        }
    }

    public void performSpell(Level level, LivingEntity target, int index) {
        switch (index) {
            case 0 -> addLightning(level, target);
            case 1 -> addFrostBeam(level, target);
            case 2 -> addFireBall(level, target);
        }
        this.triggerAnim("controller", "spell");
    }

    private void addLightning(Level level, LivingEntity target) {

        Vec3 position = target.position();
        Vec3 surface = CombatHelper.findSurface(level(), position);
        Element element = Element.SURGE;

        for (int j = 0; j < 3; j++) {
            Vec3 lightningTop = surface.add(0, Mth.randomBetween(level.random, 5, 20), 0);
            TickScheduler.schedule(5 + 10 * j, () -> {

                if (this.isAlive() && target.isAlive()) {
                    PointVFX.burst(level, surface.add(0, 0.1F, 0), Element.SURGE, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, new Vec3(0, 1, 0), elm))), 1, 0.0F);
                }

                List<Entity> entities = CombatHelper.getEntitiesInBox(level, this, surface, new Vec3(2, 2, 2));

                level.playSound(this, BlockPos.containing(surface), SoundInit.ARCLUME.get(), SoundSource.HOSTILE, 1.0F, 0.8F + (this.getRandom().nextFloat() * 0.6F));

                if (!level.isClientSide) {
                    DamageSource elementalDamageSource = this.damageSources().source(DamageTypeInit.SURGE_DAMAGE, this);
                    for (Entity entity : entities) {
                        if (!entity.isInvulnerableTo(elementalDamageSource)) {
                            this.setLastHurtMob(entity);
                            if (entity instanceof LivingEntity livingEntity) {
                                livingEntity.setLastHurtByMob(this);
                            }
                        }
                        entity.hurt(elementalDamageSource, 3);
                    }
                }

                if (level.isClientSide) {
                    TrailVFX.zapTrail(level, surface, lightningTop, 0.5F, 1.0F, 0.5F, 20, element);
                    PointVFX.burst(level, lightningTop, element, SquareParticles::squareParticle, 10, 0.05F);
                    PointVFX.burst(level, surface, element, SquareParticles::squareGravityParticle, 20, 0.5F);
                    PointVFX.zap(level, surface, element, 4, 0.25F, 2F, 2F, 0.5F, 10);
                }
            }, level.isClientSide);
        }
    }

    private void addFrostBeam(Level level, LivingEntity target) {
        Vec3 forward = target.position().add(0, target.getBbHeight() / 2, 0).subtract(this.getEyePosition()).normalize();

        TickScheduler.schedule(3, () -> {

            Vec3 end = CombatHelper.raycastBeam(this, 32, 0.3, forward);
            Entity beamTarget = CombatHelper.raycastBeamEntity(this, 32, 0.3, forward);
            Vec3 start = this.position().add(0, this.getBbHeight() * 0.7, 0).add(forward.scale(0.5));

            Element element = Element.GLACE;
            LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.2F, 0.1F);
            LineVFX.destinationLined(level, start, end, element, ElementParticles::snowParticle, new Section(0F, 1F), 5, 0.1F, 0.1F);
            BeamParticles.beamParticle(level, start, end, element, 0.1F);
            PointVFX.burst(level, end, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.2F)), 30, 0.3F);
            PointVFX.burst(level, end, element, ElementParticles::snowParticle, 20, 0.3F);

            level.playSound(this, this.blockPosition(), SoundInit.FROST_BREAK.get(), SoundSource.HOSTILE, 1.0F, 0.6F + (this.getRandom().nextFloat() * 0.6F));


            if (!level.isClientSide) {
                DamageSource elementalDamageSource = this.damageSources().source(DamageTypeInit.GLACE_DAMAGE, this);
                if (beamTarget != null) {
                    beamTarget.hurt(elementalDamageSource, 4);
                    if (beamTarget instanceof LivingEntity livingTarget) {
                        beamTarget.setTicksFrozen(Math.min(beamTarget.getTicksFrozen() + 200, 320));
                    }
                }
            }

        }, level.isClientSide);
    }

    private void addFireBall(Level level, LivingEntity target) {
        Vec3 forward = target.position().add(0, target.getBbHeight() / 2, 0).subtract(this.getEyePosition()).normalize();
        IgniscaEntity igniscaEntity = new IgniscaEntity(level, this, 4);
        igniscaEntity.setPos(this.position().add(0, this.getBbHeight() * 0.7, 0));
        igniscaEntity.setDeltaMovement(forward.scale(1.0));
        level.addFreshEntity(igniscaEntity);


        level.playSound(this, this.blockPosition(), SoundInit.FIREBALL.get(), SoundSource.HOSTILE, 1.0F, 0.6F + (this.getRandom().nextFloat() * 0.6F));
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && amount > 0) {
            this.triggerAnim("controller", "damage");
        }
        return result;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.WEAVER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.WEAVER_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            if (state.isMoving()) {
                state.setAndContinue(WALK);
            } else {
                state.setAndContinue(IDLE);
            }
            return PlayState.CONTINUE;
        }).triggerableAnim("damage", DAMAGE).triggerableAnim("spell", SPELL));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected SoundEvent getStepSound() {
        return SoundInit.ALCHECRYSITE_STEP.get();
    }

    public boolean isShaking() {
        return this.isFullyFrozen();
    }
}
