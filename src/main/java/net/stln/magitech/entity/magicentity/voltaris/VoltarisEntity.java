package net.stln.magitech.entity.magicentity.voltaris;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
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
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.SpellProjectileEntity;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VoltarisEntity extends SpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public VoltarisEntity(Level world, Player player, float damage) {
        super(EntityInit.VOLTARIS_ENTITY.get(), player, world, null, damage);
    }

    public VoltarisEntity(Level world, Player player, ItemStack weapon, float damage) {
        super(EntityInit.VOLTARIS_ENTITY.get(), player, world, weapon, damage);
    }

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(1.0F, 1.0F, 1.0F);
            float scale = 1.0F;
            int twinkle = 5;
            float rotSpeed = 0.0F;
            int particleAmount = 5;
            for (int i = 0; i < particleAmount; i++) {
                Vec3 deltaMovement = this.getDeltaMovement();
                double x = this.getX() - deltaMovement.x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - deltaMovement.y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - deltaMovement.z + (random.nextFloat() - 0.5) / 10;
                double vx = deltaMovement.x / 4;
                double vy = deltaMovement.y / 4;
                double vz = deltaMovement.z / 4;
                world.addParticle(new SparkParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }
            Vec3 deltaMovement = this.getDeltaMovement();
            double x = this.getX();
            double y = this.getY(0.5F);
            double z = this.getZ();
            double vx = deltaMovement.x;
            double vy = deltaMovement.y;
            double vz = deltaMovement.z;
            Vector3f endPos = this.position().add(0, this.getBbHeight() * 0.5, 0).add(new Vec3(this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1)).toVector3f();
            world.addParticle(new ZapParticleEffect(fromColor, toColor, endPos, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        ResourceKey<DamageType> damageType = this.getElement().getDamageType();
        DamageSource elementalDamageSource;
        if (owner != null) {
            if (this.getWeaponItem() != null) {
                elementalDamageSource = this.getWeaponItem().has(DataComponents.CUSTOM_NAME) ? owner.damageSources().source(damageType, owner) : owner.damageSources().source(damageType);
            } else {
                elementalDamageSource = owner.damageSources().source(damageType);
            }
        } else {
            elementalDamageSource = this.damageSources().source(damageType);
        }


        float finalDamage = this.damage * EntityElementRegister.getElementAffinity(entity, this.getElement()).getMultiplier();
        applyDamage(entity, elementalDamageSource, finalDamage);
        hitParticle();

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        }
    }

    @Override
    protected Element getElement() {
        return Element.SURGE;
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
                int twinkle = random.nextInt(3, 7);

                double x = this.getX() - this.getDeltaMovement().x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - this.getDeltaMovement().y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - this.getDeltaMovement().z + (random.nextFloat() - 0.5) / 10;
                double vx = (random.nextFloat() - 0.5) / 6;
                double vy = (random.nextFloat() - 0.5) / 6;
                double vz = (random.nextFloat() - 0.5) / 6;
                Vector3f endPos = this.position().add(new Vec3(this.random.nextFloat() * 4 - 2, this.random.nextFloat() * 4 - 2, this.random.nextFloat() * 4 - 2)).toVector3f();

                world.addParticle(new SparkParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
                world.addParticle(new ZapParticleEffect(fromColor, toColor, endPos, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.SPARK.get();
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
