package net.stln.magitech.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.SpellProjectileEntity;
import net.stln.magitech.network.RangedEntityAttackPayload;
import net.stln.magitech.particle.particle_option.*;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.Element;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.TickScheduler;
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
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;

public class WeaverEntity extends Monster implements GeoEntity, RangedAttackMob {

    private static final int HARD_ATTACK_INTERVAL = 20;
    private static final int NORMAL_ATTACK_INTERVAL = 40;
    private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {
        @Override
        public void stop() {
            super.stop();
            WeaverEntity.this.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            WeaverEntity.this.setAggressive(true);
        }
    };

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation DAMAGE = RawAnimation.begin().thenPlay("damage");
    private static final RawAnimation SPELL = RawAnimation.begin().thenPlay("spell");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WeaverEntity(EntityType<? extends WeaverEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        RandomSource randomsource = level.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        this.populateDefaultEquipmentEnchantments(level, randomsource, difficulty);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(randomsource.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && randomsource.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return spawnGroupData;
    }

    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            int i = this.getHardAttackInterval();
            if (this.level().getDifficulty() != Difficulty.HARD) {
                i = this.getAttackInterval();
            }
            this.bowGoal.setMinAttackInterval(i);
            this.goalSelector.addGoal(4, this.bowGoal);
        }
    }

    protected int getHardAttackInterval() {
        return 20;
    }

    protected int getAttackInterval() {
        return 40;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {

        int index = random.nextInt(2);
        performSpell(level(), target, index);

        if (!level().isClientSide) {
            PacketDistributor.sendToAllPlayers(new RangedEntityAttackPayload(this.getId(), target.getId(), distanceFactor, index));
        }
    }

    public void performSpell(Level level, LivingEntity target, int index) {
        switch (index) {
            case 0 -> addLightning(level, target);
            case 1 -> addFrostBeam(level, target);
        }
        this.triggerAnim("controller", "spell");
    }

    private void addLightning(Level level, LivingEntity target) {
        final Vec3[] surface = {null};


        Vec3 position = target.position();
        Vec3 oldSurface = EntityUtil.findSurface(level(), position);

        level().addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.5F, 0.5F, 1.0F), 1.0F, this.getRandom().nextInt(3, 6), 0), position.x, position.y + 0.1, position.z, 0, 0, 0);

        for (int i = 0; i < 40; i++) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.5F, 0.5F, 1.0F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            double x = oldSurface.x + Mth.randomBetween(this.getRandom(), -0.4F, 0.4F);
            double y = oldSurface.y + Mth.randomBetween(this.getRandom(), -0.4F, 1.4F);
            double z = oldSurface.z + Mth.randomBetween(this.getRandom(), -0.4F, 0.4F);
            double vy = (this.getRandom().nextFloat()) / 5;
            int twinkle = this.getRandom().nextInt(2, 4);
            level().addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, 0, vy, 0);
        }


        TickScheduler.schedule(2, () -> {
                surface[0] = EntityUtil.findSurface(level, target.position());
        }, level.isClientSide);

        for (int j = 0; j < 3; j++) {
            TickScheduler.schedule(5 + 10 * j, () -> {

                level().addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.5F, 0.5F, 1.0F), 1.0F, this.getRandom().nextInt(3, 6), 0), surface[0].x, surface[0].y + 0.1, surface[0].z, 0, 0, 0);

                Vec3 lightningTop = surface[0].add(0, Mth.randomBetween(this.getRandom(), 5, 20), 0);
                List<Entity> entities = EntityUtil.getEntitiesInBox(level, this, surface[0], new Vec3(2, 2, 2));

                level.playSound(this, BlockPos.containing(surface[0]), SoundInit.ARCLUME.get(), SoundSource.HOSTILE, 1.0F, 0.8F + (this.getRandom().nextFloat() * 0.6F));

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
                    level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), lightningTop.toVector3f(), 2F, 3, 0), surface[0].x, surface[0].y, surface[0].z,
                            0, 0, 0);
                    Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
                    Vector3f toColor = new Vector3f(0.5F, 0.5F, 1.0F);
                    float scale = 1.0F;
                    float rotSpeed = 0.0F;
                    int particleAmount = 20;

                    for (int i = 0; i < particleAmount; i++) {
                        int twinkle = this.getRandom().nextInt(2, 4);

                        double x = lightningTop.x;
                        double y = lightningTop.y;
                        double z = lightningTop.z;
                        double vx = (this.getRandom().nextFloat() - 0.5) / 10;
                        double vy = (this.getRandom().nextFloat() - 0.5) / 10;
                        double vz = (this.getRandom().nextFloat() - 0.5) / 10;
                        level.addParticle(new SquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
                    }

                    for (int i = 0; i < particleAmount; i++) {
                        int twinkle = this.getRandom().nextInt(2, 4);

                        double x = surface[0].x + Mth.randomBetween(this.getRandom(), -0.2F, 0.2F);
                        double y = surface[0].y + Mth.randomBetween(this.getRandom(), -0.2F, 0.2F);
                        double z = surface[0].z + Mth.randomBetween(this.getRandom(), -0.2F, 0.2F);
                        double vx = (this.getRandom().nextFloat() - 0.5) / 2;
                        double vy = (this.getRandom().nextFloat() - 0.5);
                        double vz = (this.getRandom().nextFloat() - 0.5) / 2;
                        level.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
                    }
                }}, level.isClientSide);
        }
    }

    private void addFrostBeam(Level level, LivingEntity target) {
        Vec3 forward = target.position().add(0, target.getBbHeight() / 2, 0).subtract(this.getEyePosition()).normalize();

        TickScheduler.schedule(3, () -> {

            Vec3 hitPos = EntityUtil.raycastBeam(this, 32, 0.3, forward);
            Entity beamTarget = EntityUtil.raycastBeamEntity(this, 32, 0.3, forward);
            Vec3 start = this.position().add(0, this.getBbHeight() * 0.7, 0).add(forward.scale(0.5));

            EffectUtil.lineEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
            level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), hitPos.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
            for (int i = 0; i < 20; i++) {
                level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                        hitPos.x, hitPos.y, hitPos.z, (this.getRandom().nextFloat() - 0.5) / 3, (this.getRandom().nextFloat() - 0.5) / 3, (this.getRandom().nextFloat() - 0.5) / 3);
            }

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
        return SoundEvents.TUFF_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DEEPSLATE_BREAK;
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
        return SoundEvents.TUFF_STEP;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    public boolean isShaking() {
        return this.isFullyFrozen();
    }
}
