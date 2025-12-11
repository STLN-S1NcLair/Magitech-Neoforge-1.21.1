package net.stln.magitech.entity.magicentity.electroide;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.BombSpellProjectileEntity;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.particle.particle_option.*;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.TickScheduler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Random;

public class ElectroideEntity extends BombSpellProjectileEntity{

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        this.explodeRadius = 7.0F;
    }

    public ElectroideEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.ELECTROIDE_ENTITY.get(), owner, world, null, damage);
        this.explodeRadius = 7.0F;

    }

    public ElectroideEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.ELECTROIDE_ENTITY.get(), owner, world, weapon, damage);
        this.explodeRadius = 7.0F;
    }

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        this.explodeRadius = 7.0F;
    }

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
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
                world.addParticle(new SparkParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, level().random.nextInt(5, 15), 0.95F), x, y, z, vx, vy, vz);
                Vector3f toPos = this.position().add(new Vec3(Mth.nextDouble(random, -2, 2), Mth.nextDouble(random, -2, 2), Mth.nextDouble(random, -2, 2))).toVector3f();
                world.addParticle(new ZapParticleEffect(fromColor, toColor, toPos, scale, twinkle, rotSpeed, level().random.nextInt(2, 5), 0.95F), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected Element getElement() {
        return Element.SURGE;
    }

    @Override
    protected void applyEntityHitEffect(Entity entity) {
        super.applyEntityHitEffect(entity);
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
            int particleAmount = 300;
            for (int i = 0; i < hitParticleAmount; i++) {
                int twinkle = random.nextInt(2, 4);

                double x = this.getX() - this.getDeltaMovement().x + (random.nextFloat() - 0.5) / 5;
                double y = this.getY(0.5F) - this.getDeltaMovement().y + (random.nextFloat() - 0.5) / 5;
                double z = this.getZ() - this.getDeltaMovement().z + (random.nextFloat() - 0.5) / 5;
                double vx = (random.nextFloat() - 0.5) / 2;
                double vy = (random.nextFloat() - 0.5) / 2;
                double vz = (random.nextFloat() - 0.5) / 2;
                world.addParticle(new SparkParticleEffect(fromColor, toColor, scale1, twinkle, rotSpeed, level().random.nextInt(5, 8), 0.9F), x, y, z, vx, vy, vz);
            }

            Vector3f fromCol = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toCol = new Vector3f(0.6F, 1.0F, 1.0F);

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = random.nextInt(2, 4);

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
                    case 0 -> new UnstableSquareParticleEffect(fromCol, toCol, scale2, twinkle, rotSpeed, 10, 0.9F);
                    case 1 -> new UnstableSquareParticleEffect(fromCol, toCol, scale2, twinkle, rotSpeed, 10, 0.95F);
                    case 2 -> new SparkParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(5, 15), 0.9F);
                    case 3 -> new SparkParticleEffect(fromColor, toColor, scale2, twinkle, rotSpeed, level().random.nextInt(5, 15), 0.99F);
                    case 4 -> new ZapParticleEffect(fromColor, toColor, this.position().add(offset).toVector3f(), scale1, twinkle, rotSpeed + Mth.randomBetween(random, -0.1F, 0.1F), level().random.nextInt(3, 6), 0.0F);
                    default -> throw new IllegalStateException("Unexpected value: " + i % 4);
                };
                world.addParticle(effect, x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected void explode() {
        Level level = this.level();
        RandomSource random1 = this.random;
        Entity user = this.getOwner();
        for (int i = 0; i < 10; i++) {
            TickScheduler.schedule(i * 3 + random1.nextInt(0, 5), () -> {
                Vec3 origin = this.position().add(Mth.nextDouble(random1, -5, 5), Mth.nextDouble(random1, -5, 5), Mth.nextDouble(random1, -5, 5));
                Vec3 lightningPos = EntityUtil.findSurface(level, origin);
                addLightning(user, level, lightningPos, random1);

            }, level.isClientSide);
        }
        super.explode();
    }

    private void addLightning(Entity user, Level level, Vec3 pos, RandomSource randomSource) {
        Vec3 surface = EntityUtil.findSurface(level, pos);
        Vec3 lightningTop = surface.add(0, Mth.randomBetween(randomSource, 5, 20), 0);
        List<Entity> entities = EntityUtil.getEntitiesInBox(level, null, surface, new Vec3(2, 2, 2));

        level.playSound(user instanceof Player ? (Player) user : null, surface.x, surface.y, surface.z, SoundInit.ARCLUME.get(), SoundSource.PLAYERS, 1.0F, 0.8F + (randomSource.nextFloat() * 0.6F));

        if (!level.isClientSide) {

            ResourceKey<DamageType> damageType = this.getElement().getDamageType();
            DamageSource elementalDamageSource = getElementalDamageSource(this.getOwner(), damageType);

            for (Entity target : entities) {
                this.applyDamage(target, elementalDamageSource, this.damage);
            }
        }

        if (level.isClientSide) {
            level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), lightningTop.toVector3f(), 2F, 3, 0, level.random.nextInt(2, 5), 1.0F), surface.x, surface.y, surface.z,
                    0, 0, 0);
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.5F, 0.5F, 1.0F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            int particleAmount = 20;

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = randomSource.nextInt(2, 4);

                double x = lightningTop.x;
                double y = lightningTop.y;
                double z = lightningTop.z;
                double vx = (randomSource.nextFloat() - 0.5) / 10;
                double vy = (randomSource.nextFloat() - 0.5) / 10;
                double vz = (randomSource.nextFloat() - 0.5) / 10;
                level.addParticle(new SquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, 15, 0.8F), x, y, z, vx, vy, vz);
            }

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = randomSource.nextInt(2, 4);

                double x = surface.x + Mth.randomBetween(randomSource, -0.2F, 0.2F);
                double y = surface.y + Mth.randomBetween(randomSource, -0.2F, 0.2F);
                double z = surface.z + Mth.randomBetween(randomSource, -0.2F, 0.2F);
                double vx = (randomSource.nextFloat() - 0.5) / 2;
                double vy = (randomSource.nextFloat() - 0.5);
                double vz = (randomSource.nextFloat() - 0.5) / 2;
                level.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, 15, 0.8F), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.VOLKARIN.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
