package net.stln.magitech.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class MagicBulletEntity extends SpellProjectileEntity {

    public MagicBulletEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public MagicBulletEntity(Level world, Player player) {
        super(EntityInit.MAGIC_BULLET.get(), player, world, null);

    }

    public MagicBulletEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, weapon);
    }

    public MagicBulletEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, shotFrom);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        if (world.isClientSide) {
            Vector3f fromColor = new Vector3f(1.0F, 0.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.7F, 0.0F, 1.0F);
            float scale = 1.0F;
            int twinkle = 5;
            int particleAmount = 5;
            for (int i = 0; i < particleAmount; i++) {
                Vec3 deltaMovement = this.getDeltaMovement();
                double x = this.getX() - deltaMovement.x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - deltaMovement.y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - deltaMovement.z + (random.nextFloat() - 0.5) / 10;
                double vx = deltaMovement.x / 4;
                double vy = deltaMovement.y / 4;
                double vz = deltaMovement.z / 4;
                world.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.hurt(this.damageSources().magic(), 6);
        hitParticle();

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        }
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
            Vector3f fromColor = new Vector3f(1.0F, 0.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.7F, 0.0F, 1.0F);
            float scale = 1.0F;
            int particleAmount = 10;
            for (int i = 0; i < particleAmount; i++) {
                int twinkle = random.nextInt(3, 7);

                double x = this.getX() - this.getDeltaMovement().x + (random.nextFloat() - 0.5) / 10;
                double y = this.getY(0.5F) - this.getDeltaMovement().y + (random.nextFloat() - 0.5) / 10;
                double z = this.getZ() - this.getDeltaMovement().z + (random.nextFloat() - 0.5) / 10;
                double vx = (random.nextFloat() - 0.5) / 6;
                double vy = (random.nextFloat() - 0.5) / 6;
                double vz = (random.nextFloat() - 0.5) / 6;
                world.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.BEACON_POWER_SELECT;
    }
}
