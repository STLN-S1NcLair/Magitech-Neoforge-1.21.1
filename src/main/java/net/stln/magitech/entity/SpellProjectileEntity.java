package net.stln.magitech.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class SpellProjectileEntity extends Projectile {
    @javax.annotation.Nullable
    private BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.DISALLOWED;
    public int shakeTime;
    private int life;
    private double baseDamage = 2.0;
    private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
    @javax.annotation.Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @javax.annotation.Nullable
    private List<Entity> piercedAndKilledEntities;
    @javax.annotation.Nullable
    private ItemStack firedFromWeapon = null;


    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @javax.annotation.Nullable ItemStack firedFromWeapon) {
        this(entityType, level);
        this.setCustomName(pickupItemStack.get(DataComponents.CUSTOM_NAME));
        Unit unit = pickupItemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        this.setPos(x, y, z);
        if (firedFromWeapon != null && level instanceof ServerLevel serverlevel) {
            if (firedFromWeapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.firedFromWeapon = firedFromWeapon.copy();
        }
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, pickupItemStack, firedFromWeapon);
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

        if (this.shakeTime > 0) {
            this.shakeTime--;
        }

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
        float f = (float)this.getDeltaMovement().length();
        Entity entity1 = this.getOwner();

        if (entity1 instanceof LivingEntity livingentity1) {
            livingentity1.setLastHurtMob(entity);
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int i = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.igniteForSeconds(5.0F);
        }

        DamageSource damageSource = this.damageSources().magic();
        if (entity.hurt(damageSource, 6)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity livingentity) {

                this.doKnockback(livingentity, damageSource);
                if (this.level() instanceof ServerLevel serverlevel1) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, livingentity, damageSource, this.getWeaponItem());
                }

                if (!this.level().isClientSide && entity1 instanceof ServerPlayer serverplayer) {
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


        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        double d0 = (double)(
                this.firedFromWeapon != null && this.level() instanceof ServerLevel serverlevel
                        ? EnchantmentHelper.modifyKnockback(serverlevel, this.firedFromWeapon, entity, damageSource, 0.0F)
                        : 0.0F
        );
        if (d0 > 0.0) {
            double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(d0 * 0.6 * d1);
            if (vec3.lengthSqr() > 0.0) {
                entity.push(vec3.x, 0.1, vec3.z);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

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
        compound.putShort("life", (short)this.life);
        if (this.lastState != null) {
            compound.put("inBlockState", NbtUtils.writeBlockState(this.lastState));
        }

        compound.putByte("shake", (byte)this.shakeTime);
        compound.putBoolean("inGround", this.inGround);
        compound.putByte("pickup", (byte)this.pickup.ordinal());
        compound.putDouble("damage", this.baseDamage);
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

        this.shakeTime = compound.getByte("shake") & 255;
        this.inGround = compound.getBoolean("inGround");
        if (compound.contains("damage", 99)) {
            this.baseDamage = compound.getDouble("damage");
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
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);

        this.pickup = switch (entity) {
            case Player player when this.pickup == AbstractArrow.Pickup.DISALLOWED -> AbstractArrow.Pickup.ALLOWED;
            case OminousItemSpawner ominousitemspawner -> AbstractArrow.Pickup.DISALLOWED;
            case null, default -> this.pickup;
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
