package net.stln.magitech.entity.magicentity.nihilflare;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.BombSpellProjectileEntity;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.particle.particle_option.AbstractCustomizableParticleEffect;
import net.stln.magitech.particle.particle_option.VoidGlowParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class NihilflareEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        this.explodeRadius = 8.0F;
        this.setNoGravity(true);
    }

    public NihilflareEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.NIHILFLARE_ENTITY.get(), owner, world, null, damage);
        this.explodeRadius = 8.0F;
        this.setNoGravity(true);
    }

    public NihilflareEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.NIHILFLARE_ENTITY.get(), owner, world, weapon, damage);
        this.explodeRadius = 8.0F;
        this.setNoGravity(true);
    }

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        this.explodeRadius = 8.0F;
        this.setNoGravity(true);
    }

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
        this.explodeRadius = 8.0F;
        this.setNoGravity(true);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    int lifeTime = 20;

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        this.setDeltaMovement(this.getDeltaMovement().scale(0.95));
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(1.0F, 1.0F, 1.0F);
            float scale = 2.0F;
            int twinkle = 1;
            float rotSpeed = 0.0F;
            int particleAmount = 3;
            for (int i = 0; i < particleAmount; i++) {
                Vec3 deltaMovement = this.getDeltaMovement();
                double x = this.getX() - deltaMovement.x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - deltaMovement.y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - deltaMovement.z + (random.nextFloat() - 0.5) / 10;
                double vx = deltaMovement.x / 4;
                double vy = deltaMovement.y / 4;
                double vz = deltaMovement.z / 4;
                world.addParticle(new VoidGlowParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, level().random.nextInt(1, 21), 1.0F), x, y, z, vx, vy, vz);
            }
        }
        lifeTime--;
        if (this.lifeTime < 0) {
            this.explode();
            this.playHitGroundSoundEvent();
            if (this.level().isClientSide) {
                addHitEffect();
            } else {
                this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
                this.discard();
            }
        }
    }

    @Override
    protected Element getElement() {
        return Element.HOLLOW;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            if (this.level().isClientSide) {
                explode();
                addHitEffect();
            } else {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        this.setDeltaMovement(this.getDeltaMovement().scale(0.001));
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        Vec3 normal = Vec3.atLowerCornerOf(blockHitResult.getDirection().getNormal());
        reflect(normal);
    }

    protected void reflect(Vec3 normal) {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 reflected = deltaMovement.subtract(normal.scale(deltaMovement.dot(normal) * 2));
        this.setDeltaMovement(reflected);
    }

    @Override
    protected void applyEntityHitEffect(Entity entity) {
        super.applyEntityHitEffect(entity);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setPos(livingEntity.getX() + Mth.randomBetween(random, -1.0F, 1.0F), livingEntity.getY() + Mth.randomBetween(random, -1.0F, 1.0F), livingEntity.getZ() + Mth.randomBetween(random, -1.0F, 1.0F));
            if (!level().isClientSide) {
                livingEntity.addEffect(new MobEffectInstance(MobEffectInit.PHASELOCK, 100, 0));
            }
        }
    }

    @Override
    protected void addHitEffect() {
        Level world = this.level();
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(1.0F, 1.0F, 1.0F);
            float scale1 = 4.0F;
            float scale2 = 2.0F;
            float rotSpeed = 0.0F;
            int hitParticleAmount = 20;
            int particleAmount = 600;
            for (int i = 0; i < hitParticleAmount; i++) {
                int twinkle = 1;

                double x = this.getX() - this.getDeltaMovement().x + (random.nextFloat() - 0.5) / 5;
                double y = this.getY(0.5F) - this.getDeltaMovement().y + (random.nextFloat() - 0.5) / 5;
                double z = this.getZ() - this.getDeltaMovement().z + (random.nextFloat() - 0.5) / 5;
                double vx = (random.nextFloat() - 0.5) / 2;
                double vy = (random.nextFloat() - 0.5) / 2;
                double vz = (random.nextFloat() - 0.5) / 2;
                world.addParticle(new VoidGlowParticleEffect(fromColor, toColor, scale1, twinkle, rotSpeed, level().random.nextInt(1, 21), 1.0F), x, y, z, vx, vy, vz);
            }

            Vector3f fromCol = new Vector3f(0.3F, 0.0F, 1.0F);
            Vector3f toCol = new Vector3f(0.5F, 0.0F, 1.0F);

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = 1;

                Vec3 offset = new Vec3(Mth.nextDouble(random, -1, 1),
                        Mth.nextDouble(random, -1, 1),
                        Mth.nextDouble(random, -1, 1)).normalize().scale(explodeRadius / 2);
                Vec3 vector = new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble());
                double x = this.getX() + offset.x * vector.x;
                double y = this.getY(0.5F) + offset.y * vector.y;
                double z = this.getZ() + offset.z * vector.z;
                double vx = offset.x / 5 * (vector.x);
                double vy = offset.y / 5 * (vector.y);
                double vz = offset.z / 5 * (vector.z);
                AbstractCustomizableParticleEffect effect = switch (i % 5) {
                    case 0 -> new UnstableSquareParticleEffect(fromCol, toCol, scale2, twinkle, rotSpeed, 60, 0.9F);
                    case 1 -> new UnstableSquareParticleEffect(fromCol, toCol, scale2, twinkle, rotSpeed, 60, 0.95F);
                    case 2 ->
                            new VoidGlowParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(1, 21), 1.0F);
                    case 3 ->
                            new VoidGlowParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(1, 21), 0.9F);
                    case 4 ->
                            new VoidGlowParticleEffect(fromColor, toColor, scale1, twinkle, rotSpeed, level().random.nextInt(1, 41), 0.8F);
                    default -> throw new IllegalStateException("Unexpected value: " + i % 4);
                };
                world.addParticle(effect, x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.VOLKARIN.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, (event) -> {
            return event.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
