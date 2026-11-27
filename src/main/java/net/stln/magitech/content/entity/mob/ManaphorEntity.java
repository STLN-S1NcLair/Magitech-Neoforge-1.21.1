package net.stln.magitech.content.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.entity.magicentity.mana_bolt.ManaBoltEntity;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ManaphorEntity extends Monster implements GeoEntity, RangedAttackMob {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("attack");
    private static final int TRAIL_LENGTH = 20;
    private static final int LONG_TRAIL_LENGTH = 40;
    private static final double HOVER_HEIGHT = 3.0;
    private static final double MIN_TARGET_DISTANCE = 5.0;
    private static final double PREFERRED_TARGET_DISTANCE = 8.0;
    private static final double MAX_PREFERRED_TARGET_DISTANCE = 12.0;
    private static final double ATTACK_RANGE = 24.0;
    private static final double DESIRED_BLOCK_CLEARANCE = 2.0;
    private static final double MIN_BLOCK_CLEARANCE = 0.5;
    private static final int LOCAL_GROUND_SEARCH_DEPTH = 32;
    private static final int STUCK_TICK_LIMIT = 40;
    private static final int ATTACK_INTERVAL = 60;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ManaphorEntity(EntityType<? extends ManaphorEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.navigation = new FlyingPathNavigation(this, level);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FlightCombatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.level().isClientSide || !target.isAlive()) {
            return;
        }

        Vec3 launchPosition = this.position().add(0, this.getBbHeight() * 0.55, 0);
        ManaBoltEntity projectile = new ManaBoltEntity(this.level(), this);
        projectile.setPos(launchPosition);
        Vec3 direction = target.getEyePosition().subtract(launchPosition).normalize();
        projectile.setDeltaMovement(direction.scale(0.3));
        this.level().addFreshEntity(projectile);
        this.triggerAnim("action_controller", "attack");
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                float targetYaw = Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX())) - 90.0F);
                this.setYRot(targetYaw);
                this.setYBodyRot(targetYaw);
                this.setYHeadRot(targetYaw);
            }
        }
        if (this.level().isClientSide) {
            this.tickTrail();
        }
    }

    private void tickTrail() {
        Element element = Element.MANA;
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(TRAIL_LENGTH);
        TrailPointBuilder longTrail = TrailPointBuilder.create(LONG_TRAIL_LENGTH);
        TrailData trailData = new TrailData(this.level(), builderFunc, trail, element.getPrimary(), element.getSecondary(), 0.55F, 0.9F);
        TrailData longTrailData = new TrailData(this.level(), builderFunc, longTrail, element.getSecondary(), element.getDark(), 0.3F, 0.55F);
        Vec3 center = this.position().add(0, this.getBbHeight() * 0.5, 0);
        TrailRenderer.updateTrail(this, trailData, new TrailPoint(center), TrailRenderer.TRAIL);
        TrailRenderer.updateTrail(this, longTrailData, new TrailPoint(center), TrailRenderer.LONG_TRAIL);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement_controller", state -> state.setAndContinue(IDLE)));
        controllers.add(new AnimationController<>(this, "action_controller", state -> PlayState.STOP)
                .triggerableAnim("attack", ATTACK));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private static final class FlightCombatGoal extends Goal {
        private final ManaphorEntity manaphor;
        private final double orbitDirection;
        private final Map<BlockPos, Long> rejectedDestinations = new HashMap<>();
        private int pathUpdateCooldown;
        private int attackCooldown;
        private int stuckTicks;
        private Vec3 activeDestination;
        private Vec3 plannedTargetPosition;
        private Vec3 lastProgressPosition;

        private FlightCombatGoal(ManaphorEntity manaphor) {
            this.manaphor = manaphor;
            this.orbitDirection = manaphor.getRandom().nextBoolean() ? 1.0 : -1.0;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.manaphor.getTarget();
            if (target == null || !target.isAlive()) {
                if (this.plannedTargetPosition != null) {
                    this.manaphor.getNavigation().stop();
                    this.activeDestination = null;
                    this.plannedTargetPosition = null;
                }
                this.manaphor.setAggressive(false);
                this.updateMovementProgress();
                this.maintainHoverHeight();
                return;
            }

            this.manaphor.setAggressive(true);
            this.manaphor.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distanceSqr = this.manaphor.distanceToSqr(target);
            boolean clearShot = this.hasClearShotFrom(this.manaphor.position(), target);
            boolean stalled = this.updateMovementProgress();

            if (this.pathUpdateCooldown-- <= 0) {
                boolean targetMoved = this.plannedTargetPosition == null
                        || this.plannedTargetPosition.distanceToSqr(target.position()) > 16.0;
                if (stalled || this.activeDestination == null || this.manaphor.getNavigation().isDone() || targetMoved) {
                    boolean pathStarted = this.updateFlightPath(target, Math.sqrt(distanceSqr), clearShot);
                    this.pathUpdateCooldown = pathStarted ? 5 : 20;
                } else {
                    this.pathUpdateCooldown = 5;
                }
            }

            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
            if (distanceSqr <= ATTACK_RANGE * ATTACK_RANGE
                    && clearShot
                    && this.attackCooldown <= 0) {
                this.manaphor.performRangedAttack(target, 1.0F);
                this.attackCooldown = (int) (ATTACK_INTERVAL * Mth.randomBetween(manaphor.getRandom(), 0.75F, 1.25F));
            }
        }

        private void maintainHoverHeight() {
            if (this.pathUpdateCooldown-- > 0) {
                return;
            }
            Vec3 destination = new Vec3(
                    this.manaphor.getX(),
                    this.getHoverY(
                            this.manaphor.getX(),
                            this.manaphor.getZ(),
                            this.manaphor.getY() - HOVER_HEIGHT
                    ),
                    this.manaphor.getZ()
            );
            double distanceSqr = this.manaphor.position().distanceToSqr(destination);
            double clearance = this.getBlockClearance(destination);
            if (clearance < DESIRED_BLOCK_CLEARANCE) {
                if (this.moveToNearbyOpenPosition()) {
                    this.pathUpdateCooldown = 12;
                    return;
                }
                this.manaphor.getNavigation().stop();
                this.activeDestination = null;
            } else if (distanceSqr <= 0.6 * 0.6) {
                this.manaphor.getNavigation().stop();
                this.activeDestination = null;
            } else if (!this.moveToReachablePosition(destination, 0.6, null)) {
                this.moveToNearbyOpenPosition();
            }
            this.pathUpdateCooldown = 12;
        }

        private boolean updateFlightPath(LivingEntity target, double distance, boolean clearShot) {
            if (!clearShot) {
                if (this.moveToOpenAttackPosition(target, true, true)
                        || this.moveToOpenAttackPosition(target, false, true)) {
                    return true;
                }
            }

            double offsetX = this.manaphor.getX() - target.getX();
            double offsetZ = this.manaphor.getZ() - target.getZ();
            double angle = Math.atan2(offsetZ, offsetX);
            if (offsetX * offsetX + offsetZ * offsetZ < 1.0E-4) {
                angle = this.manaphor.getRandom().nextDouble() * Math.PI * 2.0;
            }
            if (distance >= MIN_TARGET_DISTANCE && distance <= MAX_PREFERRED_TARGET_DISTANCE) {
                angle += this.orbitDirection * 0.25;
            }

            double destinationX = target.getX() + Math.cos(angle) * PREFERRED_TARGET_DISTANCE;
            double destinationZ = target.getZ() + Math.sin(angle) * PREFERRED_TARGET_DISTANCE;
            double destinationY = this.getHoverY(destinationX, destinationZ, target.getY());

            Vec3 destination = new Vec3(destinationX, destinationY, destinationZ);
            double clearance = this.getBlockClearance(destination);
            if (clearance < DESIRED_BLOCK_CLEARANCE) {
                if (this.moveToOpenAttackPosition(target, true, false)
                        || this.moveToOpenAttackPosition(target, false, false)) {
                    return true;
                }
                if (clearance < MIN_BLOCK_CLEARANCE) {
                    return false;
                }
            }

            if (this.moveToReachablePosition(destination, 1.0, target.position())) {
                return true;
            }
            return this.moveToOpenAttackPosition(target, false, false);
        }

        private boolean moveToOpenAttackPosition(LivingEntity target, boolean requireClearShot, boolean preferBoldFlank) {
            Vec3 targetPosition = target.position();
            double offsetX = this.manaphor.getX() - target.getX();
            double offsetZ = this.manaphor.getZ() - target.getZ();
            double baseAngle = Math.atan2(offsetZ, offsetX);
            List<FlightCandidate> candidates = new ArrayList<>();

            for (double radius = 10.0; radius <= ATTACK_RANGE - 2.0; radius += 4.0) {
                for (int sample = 0; sample < 16; sample++) {
                    double angle = baseAngle + sample * (Math.PI * 2.0 / 16.0);
                    double x = target.getX() + Math.cos(angle) * radius;
                    double z = target.getZ() + Math.sin(angle) * radius;
                    double y = this.getHoverY(x, z, target.getY());
                    Vec3 candidate = new Vec3(x, y, z);
                    double targetDistanceSqr = candidate.distanceToSqr(targetPosition);
                    if (targetDistanceSqr < MIN_TARGET_DISTANCE * MIN_TARGET_DISTANCE
                            || targetDistanceSqr > ATTACK_RANGE * ATTACK_RANGE) {
                        continue;
                    }

                    if (requireClearShot && !this.hasClearShotFrom(candidate, target)) {
                        continue;
                    }

                    double clearance = this.getBlockClearance(candidate);
                    if (clearance < MIN_BLOCK_CLEARANCE) {
                        continue;
                    }
                    if (this.isRecentlyRejected(candidate)) {
                        continue;
                    }

                    double angularChange = Math.abs(Mth.wrapDegrees((float) Math.toDegrees(angle - baseAngle)));
                    double travelDistance = Math.sqrt(this.manaphor.distanceToSqr(candidate));
                    double score = clearance * 300.0 - travelDistance * (preferBoldFlank ? 0.12 : 0.5);
                    if (preferBoldFlank) {
                        score += angularChange * 0.45 + radius * 0.35;
                    } else {
                        score -= angularChange * 0.1 + Math.abs(radius - PREFERRED_TARGET_DISTANCE) * 0.5;
                    }
                    candidates.add(new FlightCandidate(candidate, score));
                }
            }

            candidates.sort(Comparator.comparingDouble(FlightCandidate::score).reversed());
            for (FlightCandidate candidate : candidates) {
                if (this.moveToReachablePosition(candidate.position(), 1.0, target.position())) {
                    return true;
                }
            }
            return false;
        }

        private boolean moveToNearbyOpenPosition() {
            Vec3 origin = this.manaphor.position();
            List<FlightCandidate> candidates = new ArrayList<>();

            for (double radius = 0.0; radius <= 8.0; radius += 2.0) {
                int samples = radius == 0.0 ? 1 : 12;
                for (int sample = 0; sample < samples; sample++) {
                    double angle = sample * (Math.PI * 2.0 / samples);
                    double x = origin.x + Math.cos(angle) * radius;
                    double z = origin.z + Math.sin(angle) * radius;
                    double baseY = this.getHoverY(x, z, origin.y - HOVER_HEIGHT);
                    for (double heightOffset : new double[]{0.0, 2.0, 4.0}) {
                        Vec3 candidate = new Vec3(x, baseY + heightOffset, z);
                        double clearance = this.getBlockClearance(candidate);
                        if (clearance < MIN_BLOCK_CLEARANCE) {
                            continue;
                        }
                        if (this.isRecentlyRejected(candidate)) {
                            continue;
                        }

                        double travelDistance = Math.sqrt(this.manaphor.distanceToSqr(candidate));
                        double score = clearance * 300.0 - travelDistance * 0.5;
                        candidates.add(new FlightCandidate(candidate, score));
                    }
                }
            }

            candidates.sort(Comparator.comparingDouble(FlightCandidate::score).reversed());
            for (FlightCandidate candidate : candidates) {
                if (this.moveToReachablePosition(candidate.position(), 0.6, null)) {
                    return true;
                }
            }
            return false;
        }

        private boolean moveToReachablePosition(Vec3 destination, double speed, Vec3 targetPosition) {
            BlockPos destinationBlock = BlockPos.containing(destination).immutable();
            long gameTime = this.manaphor.level().getGameTime();
            this.rejectedDestinations.entrySet().removeIf(entry -> entry.getValue() <= gameTime);
            if (this.rejectedDestinations.containsKey(destinationBlock)) {
                return false;
            }

            Path path = this.manaphor.getNavigation().createPath(destination.x, destination.y, destination.z, 1);
            if (path == null || !path.canReach() || !this.manaphor.getNavigation().moveTo(path, speed)) {
                this.rejectedDestinations.put(destinationBlock, gameTime + STUCK_TICK_LIMIT * 2L);
                return false;
            }

            this.activeDestination = destination;
            this.plannedTargetPosition = targetPosition;
            this.lastProgressPosition = this.manaphor.position();
            this.stuckTicks = 0;
            return true;
        }

        private boolean isRecentlyRejected(Vec3 destination) {
            long gameTime = this.manaphor.level().getGameTime();
            this.rejectedDestinations.entrySet().removeIf(entry -> entry.getValue() <= gameTime);
            return this.rejectedDestinations.containsKey(BlockPos.containing(destination));
        }

        private boolean updateMovementProgress() {
            if (this.activeDestination == null || this.manaphor.getNavigation().isDone()) {
                this.lastProgressPosition = this.manaphor.position();
                this.stuckTicks = 0;
                return false;
            }

            Vec3 currentPosition = this.manaphor.position();
            if (this.lastProgressPosition == null || currentPosition.distanceToSqr(this.lastProgressPosition) >= 0.04) {
                this.lastProgressPosition = currentPosition;
                this.stuckTicks = 0;
                return false;
            }

            this.stuckTicks++;
            if (this.stuckTicks < STUCK_TICK_LIMIT) {
                return false;
            }

            this.rejectedDestinations.put(
                    BlockPos.containing(this.activeDestination).immutable(),
                    this.manaphor.level().getGameTime() + STUCK_TICK_LIMIT * 2L
            );
            this.manaphor.getNavigation().stop();
            this.activeDestination = null;
            this.plannedTargetPosition = null;
            this.lastProgressPosition = currentPosition;
            this.stuckTicks = 0;
            return true;
        }

        private double getBlockClearance(Vec3 position) {
            Vec3 movement = position.subtract(this.manaphor.position());
            AABB bounds = this.manaphor.getBoundingBox().move(movement);
            if (!this.manaphor.level().noCollision(this.manaphor, bounds)) {
                return -1.0;
            }

            double clearance = 0.0;
            for (double margin = MIN_BLOCK_CLEARANCE; margin <= DESIRED_BLOCK_CLEARANCE; margin += 0.5) {
                if (!this.manaphor.level().noCollision(this.manaphor, bounds.inflate(margin))) {
                    break;
                }
                clearance = margin;
            }
            return clearance;
        }

        private boolean hasClearShotFrom(Vec3 position, LivingEntity target) {
            Vec3 launchPosition = position.add(0.0, this.manaphor.getBbHeight() * 0.55, 0.0);
            return this.manaphor.level().clip(new ClipContext(
                    launchPosition,
                    target.getEyePosition(),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this.manaphor
            )).getType() != HitResult.Type.BLOCK;
        }

        private double getHoverY(double x, double z, double referenceY) {
            int blockX = Mth.floor(x);
            int blockZ = Mth.floor(z);
            int startY = Math.min(Mth.floor(referenceY), this.manaphor.level().getMaxBuildHeight() - 1);
            int minY = Math.max(this.manaphor.level().getMinBuildHeight(), startY - LOCAL_GROUND_SEARCH_DEPTH);
            BlockPos.MutableBlockPos samplePos = new BlockPos.MutableBlockPos();

            for (int y = startY; y >= minY; y--) {
                samplePos.set(blockX, y, blockZ);
                VoxelShape collision = this.manaphor.level().getBlockState(samplePos).getCollisionShape(this.manaphor.level(), samplePos);
                if (!collision.isEmpty()) {
                    double groundTop = y + collision.max(net.minecraft.core.Direction.Axis.Y);
                    return groundTop + HOVER_HEIGHT;
                }
            }

            return referenceY + HOVER_HEIGHT;
        }

        private record FlightCandidate(Vec3 position, double score) {
        }
    }
}
