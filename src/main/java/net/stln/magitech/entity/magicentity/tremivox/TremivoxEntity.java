package net.stln.magitech.entity.magicentity.tremivox;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.SpellProjectileEntity;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.DataMapHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TremivoxEntity extends SpellProjectileEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public TremivoxEntity(Level world, Player player, float damage) {
        super(EntityInit.TREMIVOX_ENTITY.get(), player, world, null, damage);

    }

    public TremivoxEntity(Level world, Player player, ItemStack weapon, float damage) {
        super(EntityInit.TREMIVOX_ENTITY.get(), player, world, weapon, damage);
    }

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        Vec3 deltaMovement = this.getDeltaMovement();
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(1.0F, 1.0F, 1.0F);
            float scale = 1.0F;
            int twinkle = 1;
            float rotSpeed = 0.0F;
            int particleAmount = 5;
            for (int i = 0; i < particleAmount; i++) {
                double x = this.getX() - deltaMovement.x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - deltaMovement.y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - deltaMovement.z + (random.nextFloat() - 0.5) / 10;
                double vx = deltaMovement.x / 4;
                double vy = deltaMovement.y / 4;
                double vz = deltaMovement.z / 4;
                world.addParticle(new WaveParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }
        } else {
            Vec3 center = this.position().add(this.getDeltaMovement().normalize().scale(10.0));
            LivingEntity target;
            if (this.getOwner() instanceof LivingEntity livingEntity) {
                target = world.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), livingEntity, this.getX(), this.getY(), this.getZ(), new AABB(center.subtract(10, 10, 10), center.add(10, 10, 10)));
            } else {
                target = world.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), null, this.getX(), this.getY(), this.getZ(), new AABB(center.subtract(10, 10, 10), center.add(10, 10, 10)));
            }
            if (target != null) {
                this.setDeltaMovement(target.position().add(0, target.getBbHeight() * 0.5, 0).subtract(this.position()).normalize().scale(0.07).add(deltaMovement).normalize().scale(deltaMovement.length()));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        ResourceKey<DamageType> damageType = this.getElement().getDamageType();
        DamageSource elementalDamageSource = getElementalDamageSource(owner, damageType);

        float finalDamage = this.damage * DataMapHelper.getElementMultiplier(entity, this.getElement());
        applyDamage(entity, elementalDamageSource, finalDamage);
        hitParticle();

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        }
    }

    @Override
    protected Element getElement() {
        return Element.TREMOR;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        hitParticle();

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        }
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.DEATH) {
            if (this.level().isClientSide) {
                hitParticle();
            } else {
                this.discard();
            }
        }
        super.handleEntityEvent(status);
    }

    protected void hitParticle() {
        Level world = this.level();
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(1.0F, 1.0F, 1.0F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            int particleAmount = 10;
            for (int i = 0; i < particleAmount; i++) {
                int twinkle = 1;

                double x = this.getX() - this.getDeltaMovement().x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - this.getDeltaMovement().y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - this.getDeltaMovement().z + (random.nextFloat() - 0.5) / 10;
                double vx = (random.nextFloat() - 0.5) / 6;
                double vy = (random.nextFloat() - 0.5) / 6;
                double vz = (random.nextFloat() - 0.5) / 6;
                world.addParticle(new WaveParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.TREMIVOX.get();
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
