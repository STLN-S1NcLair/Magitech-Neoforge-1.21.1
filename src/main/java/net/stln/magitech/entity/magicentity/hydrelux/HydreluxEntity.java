package net.stln.magitech.entity.magicentity.hydrelux;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.stln.magitech.particle.particle_option.AbstractCustomizableParticleEffect;
import net.stln.magitech.particle.particle_option.BlowParticleEffect;
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

public class HydreluxEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation EXPLODE = RawAnimation.begin().thenLoop("explode");
    final int maxVacuumTick = 20;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    int vacuumTick = 0;

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        this.explodeRadius = 7.0F;
    }

    public HydreluxEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.HYDRELUX_ENTITY.get(), owner, world, null, damage);
        this.explodeRadius = 7.0F;

    }

    public HydreluxEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.HYDRELUX_ENTITY.get(), owner, world, weapon, damage);
        this.explodeRadius = 7.0F;
    }

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        this.explodeRadius = 7.0F;
    }

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
        this.explodeRadius = 7.0F;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
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
                world.addParticle(new BlowParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, level().random.nextInt(10, 30), 0.87F), x, y, z, vx, vy, vz);
            }
        }
        if (this.vacuumTick > 0) {
            this.setNoGravity(true);
            vacuumTick++;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.99));
            List<Entity> entities = EntityUtil.getEntitiesInBox(this.level(), this, this.position(), new Vec3(this.explodeRadius, this.explodeRadius, this.explodeRadius));
            for (Entity entity : entities) {
                Vec3 targetBodyPos = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
                if (this.level().clip(new ClipContext(targetBodyPos, this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK) {
                    Vec3 toEntity = this.position().subtract(entity.position()).normalize();
                    double distance = this.position().distanceTo(entity.position());
                    double pullStrength = Mth.clamp((this.explodeRadius - distance) / this.explodeRadius, 0, 1) * 0.35;
                    entity.addDeltaMovement(toEntity.scale(pullStrength));
                }
            }

            Vector3f fromCol = new Vector3f(0.7F, 1.0F, 0.0F);
            Vector3f toCol = new Vector3f(0.3F, 1.0F, 0.0F);

            for (int i = 0; i < 10; i++) {
                int twinkle = 1;

                Vec3 offset = new Vec3(Mth.nextDouble(random, -1, 1),
                        Mth.nextDouble(random, -1, 1),
                        Mth.nextDouble(random, -1, 1)).normalize().scale(explodeRadius / 2);
                Vec3 vector = new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble());
                double x = this.getX() + offset.x * vector.x;
                double y = this.getY(0.5F) + offset.y * vector.y;
                double z = this.getZ() + offset.z * vector.z;
                double vx = -offset.x / 10 * (vector.x);
                double vy = -offset.y / 10 * (vector.y);
                double vz = -offset.z / 10 * (vector.z);

                world.addParticle(new UnstableSquareParticleEffect(fromCol, toCol, 2.0F, twinkle, 0, 60, 1.05F), x, y, z, vx, vy, vz);
            }
        }
        if (vacuumTick >= maxVacuumTick) {
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
        return Element.FLOW;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            if (this.level().isClientSide) {
                explode();
                addHitEffect();
            } else if (vacuumTick >= maxVacuumTick) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hitEntity = result.getEntity();
        vacuumTick++;
        Vec3 normal = result.getLocation().subtract(hitEntity.position()).normalize();
        reflect(normal);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        vacuumTick++;
        Vec3 normal = Vec3.atLowerCornerOf(blockHitResult.getDirection().getNormal());
        reflect(normal);
    }

    protected void reflect(Vec3 normal) {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 reflected = deltaMovement.subtract(normal.scale(deltaMovement.dot(normal) * 2)).normalize().scale(0.2);
        level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundInit.HYDRELUX_BOUNCE.get(), SoundSource.PLAYERS, 2.0F, 1.0F + random.nextFloat() * 0.2F);
        this.setDeltaMovement(reflected);
    }

    @Override
    protected void applyEntityHitEffect(Entity entity) {
        super.applyEntityHitEffect(entity);
        if (entity instanceof LivingEntity livingEntity) {
            Vec3 toEntity = entity.position().subtract(this.position()).normalize();
            double distance = this.position().distanceTo(entity.position());
            double pushStrength = Mth.clamp((this.explodeRadius - distance) / this.explodeRadius, 0, 1) * 2;
            livingEntity.addDeltaMovement(toEntity.scale(pushStrength).add(0, 0.3, 0));
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
                world.addParticle(new BlowParticleEffect(fromColor, toColor, scale1, twinkle, rotSpeed, level().random.nextInt(5, 15), 0.87F), x, y, z, vx, vy, vz);
            }

            Vector3f fromCol = new Vector3f(0.7F, 1.0F, 0.0F);
            Vector3f toCol = new Vector3f(0.3F, 1.0F, 0.0F);

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
                            new BlowParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(10, 30), 0.87F);
                    case 3 ->
                            new BlowParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(10, 30), 0.8F);
                    case 4 ->
                            new BlowParticleEffect(fromColor, toColor, scale1, twinkle, rotSpeed + Mth.randomBetween(random, -0.1F, 0.1F), level().random.nextInt(10, 30), 0.65F);
                    default -> throw new IllegalStateException("Unexpected value: " + i % 4);
                };
                world.addParticle(effect, x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.HYDRELUX.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, (event) -> {
            if (vacuumTick > 0) {
                return event.setAndContinue(EXPLODE);
            }
            return event.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
