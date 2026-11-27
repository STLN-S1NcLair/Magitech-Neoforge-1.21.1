package net.stln.magitech.content.entity.magicentity.mana_wisp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.SpellProjectileEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Optional;
import java.util.function.Supplier;

public class ManaWispEntity extends SpellProjectileEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final long MANA_AMOUNT = 10_000L;
    private static final int SEARCH_RADIUS = 5;
    private static final int TARGET_SCAN_INTERVAL = 10;
    private static final int MAX_LIFETIME = 1200;
    private static final double FLIGHT_SPEED = 0.16;
    private static final double DOCK_DISTANCE = 0.32;
    private static final double WANDER_ARRIVAL_DISTANCE = 0.55;
    private static final double STEERING_RESPONSE = 0.12;
    private static final double OBSTACLE_LOOK_AHEAD = 1.4;
    private static final double WANDER_FORWARD_DISTANCE = 3.0;
    private static final double WANDER_FORWARD_VARIATION = 0.5;
    private static final double WANDER_SIDE_VARIATION = 0.8;
    private static final double WANDER_HEIGHT_VARIATION = 0.5;

    private final double avoidanceSide;
    private long remainingMana = MANA_AMOUNT;
    @Nullable
    private BlockPos targetPos;
    @Nullable
    private Vec3 wanderTarget;
    private int targetSearchCooldown;
    private boolean docked;

    public ManaWispEntity(EntityType<? extends SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setDamage(0.0F);
        this.avoidanceSide = this.getRandom().nextBoolean() ? 1.0 : -1.0;
    }

    public ManaWispEntity(Level level, @Nullable Entity owner, Vec3 position, Vec3 initialMotion) {
        this(EntityInit.MANA_WISP_ENTITY.get(), level);
        this.setOwner(owner);
        this.setPos(position);
        this.setDeltaMovement(initialMotion);
    }

    @Override
    protected Element getDefaultElement() {
        return Element.MANA;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.empty();
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.MANA_PARCEL;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.tickCount >= MAX_LIFETIME) {
                this.discard();
                return;
            }

            this.updateTarget();

            if (this.targetPos != null) {
                Vec3 targetPosition = this.targetPos.getCenter();
                boolean targetHasCollision = !this.level().getBlockState(this.targetPos)
                        .getCollisionShape(this.level(), this.targetPos).isEmpty();
                if (this.docked || (!targetHasCollision
                        && this.position().distanceToSqr(targetPosition) <= DOCK_DISTANCE * DOCK_DISTANCE)) {
                    this.docked = true;
                    this.setDeltaMovement(Vec3.ZERO);
                    this.deliverMana();
                } else {
                    this.setDeltaMovement(this.steerToward(targetPosition));
                }
            } else {
                this.docked = false;
                this.updateWanderTarget();
                if (this.wanderTarget != null) {
                    this.setDeltaMovement(this.steerToward(this.wanderTarget));
                }
            }
        }

        if (!this.isRemoved()) {
            super.tick();
        }
    }

    private void updateTarget() {
        if (this.targetPos != null && !this.isCurrentTargetUsable()) {
            this.clearTarget();
        }

        if (this.targetPos != null || this.targetSearchCooldown-- > 0) {
            return;
        }

        TargetCandidate candidate = this.findManaTarget();
        if (candidate != null) {
            this.targetPos = candidate.position();
            this.wanderTarget = null;
            this.docked = false;
        }
        this.targetSearchCooldown = TARGET_SCAN_INTERVAL;
    }

    private boolean isCurrentTargetUsable() {
        if (this.targetPos == null || !this.level().hasChunkAt(this.targetPos)) {
            return false;
        }
        IBlockManaHandler handler = ManaTransferHelper.getManaContainer(this.level(), this.targetPos, null);
        return handler != null
                && handler.getManaFlowRule().canInsert()
                && handler.getMaxMana() - handler.getMana() >= this.remainingMana;
    }

    @Nullable
    private TargetCandidate findManaTarget() {
        BlockPos center = BlockPos.containing(this.position());
        double maxDistanceSqr = SEARCH_RADIUS * SEARCH_RADIUS;
        TargetCandidate best = null;

        for (int x = center.getX() - SEARCH_RADIUS; x <= center.getX() + SEARCH_RADIUS; x++) {
            for (int y = center.getY() - SEARCH_RADIUS; y <= center.getY() + SEARCH_RADIUS; y++) {
                for (int z = center.getZ() - SEARCH_RADIUS; z <= center.getZ() + SEARCH_RADIUS; z++) {
                    BlockPos candidatePos = new BlockPos(x, y, z);
                    if (!this.level().hasChunkAt(candidatePos)
                            || this.position().distanceToSqr(candidatePos.getCenter()) > maxDistanceSqr
                            || this.level().getBlockEntity(candidatePos) == null) {
                        continue;
                    }

                    IBlockManaHandler handler = ManaTransferHelper.getManaContainer(this.level(), candidatePos, null);
                    if (handler == null
                            || !handler.getManaFlowRule().canInsert()
                            || handler.getMaxMana() - handler.getMana() < this.remainingMana) {
                        continue;
                    }

                    double distanceSqr = this.position().distanceToSqr(candidatePos.getCenter());
                    if (best == null || distanceSqr < best.distanceSqr()) {
                        best = new TargetCandidate(candidatePos.immutable(), distanceSqr);
                    }
                }
            }
        }
        return best;
    }

    private void clearTarget() {
        this.targetPos = null;
        this.docked = false;
        this.targetSearchCooldown = 0;
    }

    private void deliverMana() {
        if (this.targetPos == null || !this.level().hasChunkAt(this.targetPos)) {
            this.clearTarget();
            return;
        }

        IBlockManaHandler handler = ManaTransferHelper.getManaContainer(this.level(), this.targetPos, null);
        if (handler == null || !handler.getManaFlowRule().canInsert()) {
            this.clearTarget();
            return;
        }

        long manaBefore = handler.getMana();
        handler.addMana(this.remainingMana);
        long inserted = handler.getMana() - manaBefore;
        if (inserted <= 0) {
            this.clearTarget();
            this.targetSearchCooldown = TARGET_SCAN_INTERVAL;
            return;
        }

        this.remainingMana -= inserted;
        if (this.remainingMana <= 0) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    private void updateWanderTarget() {
        if (this.wanderTarget != null) {
            if (this.position().distanceToSqr(this.wanderTarget)
                    > WANDER_ARRIVAL_DISTANCE * WANDER_ARRIVAL_DISTANCE) {
                return;
            }
        }

        Vec3 velocity = this.getDeltaMovement();
        Vec3 forward = velocity.lengthSqr() > 1.0E-6
                ? velocity.normalize()
                : new Vec3(this.getRandom().nextDouble() - 0.5,
                        this.getRandom().nextDouble() - 0.5,
                        this.getRandom().nextDouble() - 0.5).normalize();
        if (forward.lengthSqr() < 1.0E-6) {
            forward = new Vec3(1.0, 0.0, 0.0);
        }
        Vec3 lateral = new Vec3(-forward.z, 0.0, forward.x);
        if (lateral.lengthSqr() < 1.0E-6) {
            lateral = new Vec3(1.0, 0.0, 0.0);
        }
        lateral = lateral.normalize();

        for (int attempt = 0; attempt < 8; attempt++) {
            double forwardDistance = WANDER_FORWARD_DISTANCE
                    + (this.getRandom().nextDouble() * 2.0 - 1.0) * WANDER_FORWARD_VARIATION;
            double sideOffset = (this.getRandom().nextDouble() * 2.0 - 1.0) * WANDER_SIDE_VARIATION;
            double heightOffset = (this.getRandom().nextDouble() * 2.0 - 1.0) * WANDER_HEIGHT_VARIATION;
            Vec3 candidate = this.position()
                    .add(forward.scale(forwardDistance))
                    .add(lateral.scale(sideOffset))
                    .add(0.0, heightOffset, 0.0);
            if (this.level().noCollision(this, this.getBoundingBox().move(candidate.x - this.getX(), candidate.y - this.getY(), candidate.z - this.getZ()))) {
                this.wanderTarget = candidate;
                return;
            }
        }

        this.wanderTarget = this.position().add(forward.scale(WANDER_FORWARD_DISTANCE));
    }

    private Vec3 steerToward(Vec3 destination) {
        Vec3 toDestination = destination.subtract(this.position());
        if (toDestination.lengthSqr() < 1.0E-6) {
            return Vec3.ZERO;
        }

        Vec3 direction = toDestination.normalize();
        double lookAhead = Math.min(OBSTACLE_LOOK_AHEAD, toDestination.length());
        Vec3 rayStart = this.position().add(0.0, this.getBbHeight() * 0.5, 0.0);
        BlockHitResult obstacle = this.level().clip(new ClipContext(
                rayStart,
                rayStart.add(direction.scale(lookAhead)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));
        if (obstacle.getType() == HitResult.Type.BLOCK
                && (this.targetPos == null || !obstacle.getBlockPos().equals(this.targetPos))) {
            Vec3 normal = Vec3.atLowerCornerOf(obstacle.getDirection().getNormal());
            Vec3 lateral = new Vec3(-direction.z, 0.0, direction.x);
            if (lateral.lengthSqr() < 1.0E-6) {
                lateral = new Vec3(1.0, 0.0, 0.0);
            }
            Vec3 avoidanceDirection = normal.scale(0.75)
                    .add(lateral.normalize().scale(0.6 * this.avoidanceSide))
                    .normalize();
            double hitDistance = rayStart.distanceTo(obstacle.getLocation());
            double urgency = Mth.clamp(1.0 - hitDistance / lookAhead, 0.0, 1.0);
            double avoidanceWeight = 0.25 + urgency * 0.4;
            direction = direction.lerp(avoidanceDirection, avoidanceWeight).normalize();
        }

        Vec3 currentVelocity = this.getDeltaMovement();
        Vec3 currentDirection = currentVelocity.lengthSqr() > 1.0E-6 ? currentVelocity.normalize() : direction;
        Vec3 smoothedDirection = currentDirection.lerp(direction, STEERING_RESPONSE).normalize();
        double currentSpeed = currentVelocity.length();
        double smoothedSpeed = currentSpeed + (FLIGHT_SPEED - currentSpeed) * STEERING_RESPONSE;
        return smoothedDirection.scale(smoothedSpeed);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.level().isClientSide || !(result instanceof BlockHitResult blockHit)) {
            return;
        }

        Vec3 normal = Vec3.atLowerCornerOf(blockHit.getDirection().getNormal());
        if (this.targetPos != null && this.targetPos.equals(blockHit.getBlockPos())) {
            this.setPos(result.getLocation().add(normal.scale(0.05)));
            this.docked = true;
            this.setDeltaMovement(Vec3.ZERO);
            this.deliverMana();
            return;
        }

        Vec3 lateral = new Vec3(-normal.z, 0.0, normal.x);
        if (lateral.lengthSqr() < 1.0E-6) {
            lateral = new Vec3(1.0, 0.0, 0.0);
        }
        Vec3 velocity = this.getDeltaMovement();
        Vec3 tangentVelocity = velocity.subtract(normal.scale(velocity.dot(normal))).scale(0.78);
        Vec3 slideVelocity = tangentVelocity.add(normal.scale(FLIGHT_SPEED * 0.12));
        if (slideVelocity.lengthSqr() < 1.0E-4) {
            slideVelocity = normal.scale(FLIGHT_SPEED * 0.12)
                    .add(lateral.normalize().scale(FLIGHT_SPEED * 0.65 * this.avoidanceSide));
        }
        if (slideVelocity.lengthSqr() > FLIGHT_SPEED * FLIGHT_SPEED) {
            slideVelocity = slideVelocity.normalize().scale(FLIGHT_SPEED);
        }
        this.setPos(result.getLocation().add(normal.scale(0.05)));
        this.setDeltaMovement(slideVelocity);
        this.hasImpulse = true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            if (this.level().isClientSide) {
                this.spawnHitParticle();
            }
            return;
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void spawnTickParticle() {
        EntityVFX.aura(this.level(), Element.MANA, (lvl, pos, elm) -> PresetHelper.smaller(SquareParticles.squareParticle(lvl, pos, elm)), this, new Section(0.0F, 1.0F), 0.9F, 1);
    }

    @Override
    protected void spawnHitParticle() {
        PointVFX.burstSquare(this.level(), this.position(), Element.MANA, 10, 0.16F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", state -> state.setAndContinue(IDLE)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("mana_wisp_remaining", this.remainingMana);
        if (this.targetPos != null) {
            compound.putLong("mana_wisp_target", this.targetPos.asLong());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.remainingMana = compound.contains("mana_wisp_remaining") ? compound.getLong("mana_wisp_remaining") : MANA_AMOUNT;
        this.targetPos = compound.contains("mana_wisp_target") ? BlockPos.of(compound.getLong("mana_wisp_target")) : null;
    }

    private record TargetCandidate(BlockPos position, double distanceSqr) {
    }
}
