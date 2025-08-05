package net.stln.magitech.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.util.Element;
import software.bernie.geckolib.animatable.GeoEntity;

import javax.annotation.Nullable;

public abstract class SpellProjectileEntity extends Projectile implements GeoEntity {
    public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.DISALLOWED;
    protected boolean inGround;
    protected int inGroundTime;
    protected float damage;
    @javax.annotation.Nullable
    private BlockState lastState;
    private int life;
    private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
    private ItemStack firedFromWeapon = null;


    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, double x, double y, double z, Level level, @javax.annotation.Nullable ItemStack firedFromWeapon, float damage) {
        this(entityType, level);
        this.setPos(x, y, z);
        this.damage = damage;
        if (firedFromWeapon != null && level instanceof ServerLevel serverlevel) {
            if (firedFromWeapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.firedFromWeapon = firedFromWeapon.copy();
        }
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, LivingEntity owner, Level level, @Nullable ItemStack firedFromWeapon, float damage) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, firedFromWeapon, damage);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = true;
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
            this.setXRot((float) (Mth.atan2(vec3.y, d0) * 180.0F / (float) Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        blockstate.isAir();

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }
        this.inGroundTime = 0;
        Vec3 vec32 = this.position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
        }

        while (!this.isRemoved()) {
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
                    break;
                ProjectileDeflection projectiledeflection = this.hitTargetOrDeflectSelf(hitresult);
                this.hasImpulse = true;
                if (projectiledeflection != ProjectileDeflection.NONE) {
                    break;
                }
            }

            if (hitresult != null) {
                hitresult.getType();
            }

            break;
        }

        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;

        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();
        if (flag) {
            this.setYRot((float) (Mth.atan2(-d5, -d1) * 180.0F / (float) Math.PI));
        } else {
            this.setYRot((float) (Mth.atan2(d5, d1) * 180.0F / (float) Math.PI));
        }

        this.setXRot((float) (Mth.atan2(d6, d4) * 180.0F / (float) Math.PI));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        if (this.isInWater()) {
            for (int j = 0; j < 4; j++) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25, d2 - d6 * 0.25, d3 - d1 * 0.25, d5, d6, d1);
            }
        }
        if (!flag) {
            this.applyGravity();
        }

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
        if (this.tickCount >= 1200) {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        float f = (float) this.getDeltaMovement().length();
        Entity entity1 = this.getOwner();

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    protected void applyDamage(Entity entity, DamageSource damageSource, float amount) {
        int i = entity.getRemainingFireTicks();
        Entity owner = this.getOwner();

        if (owner instanceof LivingEntity livingentity1) {
            livingentity1.setLastHurtMob(entity);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setLastHurtByMob(livingentity1);
            }
        }

        if (entity.hurt(damageSource, amount)) {

            if (entity instanceof LivingEntity livingentity) {

//                this.doKnockback(livingentity, damageSource);
                if (this.level() instanceof ServerLevel serverlevel1) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, livingentity, damageSource, this.getWeaponItem());
                }

                if (!this.level().isClientSide && owner instanceof ServerPlayer serverplayer) {
                }
            }

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            entity.setRemainingFireTicks(i);
            this.deflect(ProjectileDeflection.REVERSE, entity, this.getOwner(), false);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {

                this.discard();
            }
        }
    }

    protected Element getElement() {
        return Element.NONE;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.firedFromWeapon;
    }


    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    protected final SoundEvent getHitGroundSoundEvent() {
        return this.soundEvent;
    }

    protected void doPostHurtEffects(LivingEntity target) {
    }

    public void setDamage(float value) {
        this.damage = value;
    }

    /**
     * Gets the EntityRayTraceResult representing the entity hit
     */
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(
                this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
        );
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("life", (short) this.life);
        if (this.lastState != null) {
            compound.put("inBlockState", NbtUtils.writeBlockState(this.lastState));
        }
        compound.putBoolean("inGround", this.inGround);
        compound.putFloat("damage", this.damage);
        compound.putString("SoundEvent", BuiltInRegistries.SOUND_EVENT.getKey(this.soundEvent).toString());
        if (this.firedFromWeapon != null) {
            compound.put("weapon", this.firedFromWeapon.save(this.registryAccess(), new CompoundTag()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.life = compound.getShort("life");
        if (compound.contains("inBlockState", 10)) {
            this.lastState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("inBlockState"));
        }
        this.inGround = compound.getBoolean("inGround");
        if (compound.contains("damage", 99)) {
            this.damage = compound.getFloat("damage");
        }
        if (compound.contains("SoundEvent", 8)) {
            this.soundEvent = BuiltInRegistries.SOUND_EVENT
                    .getOptional(ResourceLocation.parse(compound.getString("SoundEvent")))
                    .orElse(this.getDefaultHitGroundSoundEvent());
        }

        if (compound.contains("weapon", 10)) {
            this.firedFromWeapon = ItemStack.parse(this.registryAccess(), compound.getCompound("weapon")).orElse(null);
        } else {
            this.firedFromWeapon = null;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
