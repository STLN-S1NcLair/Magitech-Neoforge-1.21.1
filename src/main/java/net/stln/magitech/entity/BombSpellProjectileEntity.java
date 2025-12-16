package net.stln.magitech.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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
import net.stln.magitech.util.DataMapHelper;
import net.stln.magitech.util.EntityUtil;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BombSpellProjectileEntity extends SpellProjectileEntity {

    protected float explodeRadius = 5.0F;


    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, double x, double y, double z, Level level, @Nullable ItemStack firedFromWeapon, float damage) {
        super(entityType, x, y, z, level, firedFromWeapon, damage);
    }

    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, LivingEntity owner, Level level, @Nullable ItemStack firedFromWeapon, float damage) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, firedFromWeapon, damage);
        this.setOwner(owner);
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
        super.handleEntityEvent(status);
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        explode();
        if (this.level().isClientSide) {
            addHitEffect();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        explode();
        if (this.level().isClientSide) {
            addHitEffect();
        }
    }

    @Override
    protected void playHitGroundSoundEvent() {
        this.playSound(this.getHitGroundSoundEvent(), 5.0F, this.random.nextFloat() * 0.2F + 0.9F);
    }

    protected void explode() {
        List<Entity> entities = EntityUtil.getEntitiesInBox(this.level(), this, this.position(), new Vec3(this.explodeRadius, this.explodeRadius, this.explodeRadius));
        for (Entity entity : entities) {
            Vec3 targetBodyPos = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
            if (entity instanceof LivingEntity) {
                if (this.level().clip(new ClipContext(targetBodyPos, this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK) {

                    ResourceKey<DamageType> damageType = this.getElement().getDamageType();
                    DamageSource elementalDamageSource = getElementalDamageSource(this.getOwner(), damageType);


                    float finalDamage = this.damage * DataMapHelper.getElementMultiplier(entity, this.getElement());
                    applyDamage(entity, elementalDamageSource, finalDamage);
                    applyEntityHitEffect(entity);
                }
            }
        }
    }

    protected void applyEntityHitEffect(Entity entity) {
        // Optional: Override in subclasses to add specific effects on hit
    }

    abstract protected void addHitEffect();

}
