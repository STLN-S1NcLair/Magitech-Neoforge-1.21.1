package net.stln.magitech.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.stln.magitech.element.Element;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.util.DataMapHelper;
import net.stln.magitech.util.EntityUtil;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BombSpellProjectileEntity extends SpellProjectileEntity{

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
