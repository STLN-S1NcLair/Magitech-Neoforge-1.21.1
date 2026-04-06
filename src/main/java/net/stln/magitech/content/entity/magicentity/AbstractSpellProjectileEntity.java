package net.stln.magitech.content.entity.magicentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class AbstractSpellProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Integer> PIERCE_LEVEL = SynchedEntityData.defineId(AbstractSpellProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> WAND = SynchedEntityData.defineId(AbstractSpellProjectileEntity.class, EntityDataSerializers.ITEM_STACK);
    protected boolean inGround;
    protected int inGroundTime;
    @Nullable
    private BlockState lastState;

    protected AbstractSpellProjectileEntity(EntityType<? extends AbstractSpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected AbstractSpellProjectileEntity(EntityType<? extends AbstractSpellProjectileEntity> entityType, double x, double y, double z, Level level, @Nullable ItemStack wand) {
        this(entityType, level);
        this.setPos(x, y, z);
        if (wand != null && level instanceof ServerLevel serverlevel) {
            if (wand.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon casting spell");
            }

           setWand(wand.copy());
        }
    }

    protected AbstractSpellProjectileEntity(EntityType<? extends AbstractSpellProjectileEntity> entityType, LivingEntity owner, Level level, @Nullable ItemStack wand) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, wand);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 delta = this.getDeltaMovement();

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        blockstate.isAir();

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }
        this.inGroundTime = 0;
        Vec3 pos = this.position();
        Vec3 newPos = pos.add(delta);
        // なにかに当たった場合にはその地点に移動する(貫通防止)
        HitResult hitresult = this.level().clip(new ClipContext(pos, newPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            newPos = hitresult.getLocation();
        }

        Vec3 oldHitPos = null;
        while (!this.isRemoved()) {
            EntityHitResult entityhitresult = this.findHitEntity(pos, newPos);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player target && entity1 instanceof Player owner && !owner.canHarmPlayer(target)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                // 無限ループ防止
                if (hitresult.getLocation().equals(oldHitPos)) break;
                if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) break;
                ProjectileDeflection projectiledeflection = this.hitTargetOrDeflectSelf(hitresult);
                this.hasImpulse = true;
                if (projectiledeflection != ProjectileDeflection.NONE) {
                    break;
                }
                oldHitPos = hitresult.getLocation();
            }

            if (entityhitresult == null || isFinalHit()) {
                break;
            }

            hitresult = null;
        }

        move();

        this.checkInsideBlocks();
        if (this.tickCount >= 1200) {
            this.discard();
        }
    }

    public ItemStack getWand() {
        return this.entityData.get(WAND);
    }

    public void setWand(ItemStack wand) {
        this.entityData.set(WAND, wand);
    }

    public int getPierce() {
        return this.entityData.get(PIERCE_LEVEL);
    }

    public void setPierce(int pierce) {
        this.entityData.set(PIERCE_LEVEL, pierce);
    }

    protected boolean isFinalHit() {
        return getPierce() == 0;
    }

    protected boolean isValidHit(HitResult result) {
        return isFinalHit() || result instanceof EntityHitResult;
    }

    private void move() {
        Vec3 vec3;
        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;

        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();
        this.setYRot((float) (Mth.atan2(-d5, -d1) * 180.0F / (float) Math.PI));

        this.setXRot((float) (Mth.atan2(d6, d4) * 180.0F / (float) Math.PI));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        if (this.isInWater()) {
            for (int j = 0; j < 4; j++) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25, d2 - d6 * 0.25, d3 - d1 * 0.25, d5, d6, d1);
            }
        }
        if (!this.isNoGravity()) {
            this.applyGravity();
        }

        this.setPos(d7, d2, d3);
    }

    @Override
    public ItemStack getWeaponItem() {
        return getWand();
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PIERCE_LEVEL, 0);
        builder.define(WAND, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.lastState != null) {
            compound.put("inBlockState", NbtUtils.writeBlockState(this.lastState));
        }
        compound.putBoolean("inGround", this.inGround);
        compound.putInt("pierce", this.getPierce());
        ItemStack wand = getWand();
        if (!wand.isEmpty()) {
            compound.put("weapon", wand.save(this.registryAccess(), new CompoundTag()));
        }
    }

    /**
     * (abstract) Protected helper method child read subclass entity data parent NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("inBlockState", 10)) {
            this.lastState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("inBlockState"));
        }
        this.inGround = compound.getBoolean("inGround");
        if (compound.contains("pierce", 99)) {
            setPierce(compound.getInt("pierce"));
        }
        if (compound.contains("weapon", 10)) {
            setWand(ItemStack.parse(this.registryAccess(), compound.getCompound("weapon")).orElse(ItemStack.EMPTY));
        } else {
            setWand(ItemStack.EMPTY);
        }
    }
}
