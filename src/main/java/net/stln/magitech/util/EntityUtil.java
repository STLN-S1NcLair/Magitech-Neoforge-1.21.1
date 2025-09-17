package net.stln.magitech.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EntityUtil {

    public static Vec3 findSurface(Level level, Vec3 origin) {
        int x = Mth.floor(origin.x);
        int z = Mth.floor(origin.z);
        int y = Mth.floor(origin.y);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);

        // 真下に向かって最初の非空気ブロックを探す
        while (y > level.getMinBuildHeight() && level.getBlockState(mutablePos).isAir()) {
            y--;
            mutablePos.set(x, y, z);
        }

        // 地面の上に出る：空気になるまで上昇
        while (y < level.getMaxBuildHeight() && !level.getBlockState(mutablePos.above()).isAir()) {
            y++;
            mutablePos.set(x, y, z);
        }

        // ちょうどその上の空中位置をVec3で返す（中心に寄せる）
        return new Vec3(x + 0.5, y + 1, z + 0.5);
    }

    public static List<Entity> getEntitiesInBox(Level world, Entity owner, Vec3 center, Vec3 size) {
        AABB box = new AABB(
                center.x - size.x / 2, center.y - size.y / 2, center.z - size.z / 2,
                center.x + size.x / 2, center.y + size.y / 2, center.z + size.z / 2
        );

        return world.getEntities(owner, box, e -> e != owner && !e.isSpectator());
    }

    public static List<LivingEntity> getLivingEntitiesInBox(Level world, Entity owner, Vec3 center, Vec3 size) {
        AABB box = new AABB(
                center.x - size.x / 2, center.y - size.y / 2, center.z - size.z / 2,
                center.x + size.x / 2, center.y + size.y / 2, center.z + size.z / 2
        );

        return world.getEntitiesOfClass(LivingEntity.class, box, e -> e != owner && !e.isSpectator());
    }

    /**
     * プレイヤーの視線の先にある障害物 or エンティティの位置を取得する
     *
     * @param player      プレイヤー
     * @param attackRange 最大攻撃範囲 (例: 3.0)
     * @return 攻撃が届く座標
     */
    public static Vec3 getAttackTargetPosition(Player player, double attackRange, double missRange, double offset) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        // 視線の方向を取得
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        // 最大射程の位置
        Vec3 maxReachPos = playerEyePos.add(forward.multiply(attackRange, attackRange, attackRange));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? attackRange + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : attackRange + 1;

        // 近い方を採用
        double hitDistance = Math.min(blockHitDist, entityHitDist);
        if (hitDistance > attackRange) {
            hitDistance = missRange;
            return playerEyePos.add(forward.multiply(hitDistance, hitDistance, hitDistance));
        }
        return playerEyePos.add(forward.multiply(hitDistance, hitDistance, hitDistance)).subtract(forward.multiply(offset, offset, offset));
    }

    public static Vec3 raycast(Player player, double maxReachLength) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        // 視線の方向を取得
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        Vec3 maxReachPos = playerEyePos.add(forward.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : maxReachLength + 1;

        // 近い方を採用
        double hitDistance = Math.min(blockHitDist, entityHitDist);
        return playerEyePos.add(forward.multiply(hitDistance, hitDistance, hitDistance));
    }

    public static Vec3 raycastBeam(Entity player, double maxReachLength, double radius) {
        return raycastBeam(player, maxReachLength, radius, Vec3.directionFromRotation(player.getRotationVector()));
    }

    public static Vec3 raycastBeam(Entity player, double maxReachLength, double radius, Vec3 directionNormalized) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        Vec3 maxReachPos = playerEyePos.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());
        entityHit = getCylinderHit(player, maxReachLength, playerEyePos, radius, entityHit, world, maxReachPos);

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : maxReachLength + 1;

        // 近い方を採用
        double hitDistance = Math.min(blockHitDist, entityHitDist);
        return playerEyePos.add(directionNormalized.multiply(hitDistance, hitDistance, hitDistance));
    }

    public static BlockHitResult raycastBeamBlockHit(Entity player, double maxReachLength, double radius) {
        return raycastBeamBlockHit(player, maxReachLength, radius, Vec3.directionFromRotation(player.getRotationVector()));
    }

    public static BlockHitResult raycastBeamBlockHit(Entity player, double maxReachLength, double radius, Vec3 directionNormalized) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        Vec3 maxReachPos = playerEyePos.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());
        entityHit = getCylinderHit(player, maxReachLength, playerEyePos, radius, entityHit, world, maxReachPos);

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : maxReachLength + 1;

        if (blockHitDist < entityHitDist) {
            return blockHit;
        } else {
            // エンティティにヒットした場合は、空のBlockHitResultを返す
            return null;
        }
    }

    public static Vec3 raycast(Player player, double maxReachLength, Vec3 start, Vec3 directionNormalized) {
        Level world = player.level();

        Vec3 maxReachPos = start.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                start, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, start, maxReachPos, player.level());

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(start);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(start) : maxReachLength + 1;

        // 近い方を採用
        double hitDistance = Math.min(blockHitDist, entityHitDist);
        return start.add(directionNormalized.multiply(hitDistance, hitDistance, hitDistance));
    }

    public static Vec3 raycastBeam(Player player, double maxReachLength, Vec3 start, Vec3 directionNormalized, double radius) {
        Level world = player.level();

        Vec3 maxReachPos = start.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                start, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, start, maxReachPos, player.level());
        entityHit = getCylinderHit(player, maxReachLength, start, radius, entityHit, world, maxReachPos);

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(start);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(start) : maxReachLength + 1;

        // 近い方を採用
        double hitDistance = Math.min(blockHitDist, entityHitDist);
        return start.add(directionNormalized.multiply(hitDistance, hitDistance, hitDistance));
    }

    public static Entity raycastEntity(Player player, double maxReachLength) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        // 視線の方向を取得
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        // 最大射程の位置
        Vec3 maxReachPos = playerEyePos.add(forward.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : maxReachLength + 1;

        if (blockHitDist > entityHitDist) {
            return entityHit.getEntity();
        }
        return null;
    }

    public static Entity raycastBeamEntity(Entity player, double maxReachLength, double radius) {
        return raycastBeamEntity(player, maxReachLength, radius, Vec3.directionFromRotation(player.getRotationVector()));
    }

    public static Entity raycastBeamEntity(Entity player, double maxReachLength, double radius, Vec3 directionNormalized) {
        Level world = player.level();

        // プレイヤーの目線の位置（頭の高さ）
        Vec3 playerEyePos = player.getEyePosition();
        // 最大射程の位置
        Vec3 maxReachPos = playerEyePos.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                playerEyePos, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, playerEyePos, maxReachPos, player.level());
        entityHit = getCylinderHit(player, maxReachLength, playerEyePos, radius, entityHit, world, maxReachPos);

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(playerEyePos);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(playerEyePos) : maxReachLength + 1;

        if (blockHitDist > entityHitDist) {
            return entityHit.getEntity();
        }
        return null;
    }

    public static Entity raycastEntity(Player player, double maxReachLength, Vec3 start, Vec3 directionNormalized) {
        Level world = player.level();

        Vec3 maxReachPos = start.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                start, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, start, maxReachPos, player.level());

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(start);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(start) : maxReachLength + 1;

        if (blockHitDist > entityHitDist) {
            return entityHit.getEntity();
        }
        return null;
    }

    public static Entity raycastBeamEntity(Player player, double maxReachLength, Vec3 start, Vec3 directionNormalized, double radius) {
        Level world = player.level();

        Vec3 maxReachPos = start.add(directionNormalized.scale(maxReachLength));

        // Raycast (ブロック)
        BlockHitResult blockHit = world.clip(new ClipContext(
                start, maxReachPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // Raycast (エンティティ)
        EntityHitResult entityHit = getEntityHitResult(player, start, maxReachPos, player.level());
        entityHit = getCylinderHit(player, maxReachLength, start, radius, entityHit, world, maxReachPos);

        double blockHitDist = blockHit.getType() == HitResult.Type.MISS ? maxReachLength + 1 : blockHit.getLocation().distanceTo(start);
        double entityHitDist = entityHit != null ? entityHit.getLocation().distanceTo(start) : maxReachLength + 1;

        if (blockHitDist > entityHitDist) {
            return entityHit.getEntity();
        }
        return null;
    }

    private static @Nullable EntityHitResult getCylinderHit(Entity player, double maxReachLength, Vec3 start, double radius, EntityHitResult entityHit, Level world, Vec3 maxReachPos) {
        if (entityHit == null) {

            for (Entity entity : world.getEntities(player, new AABB(start, maxReachPos).inflate(radius))) {
                if (entity.isAttackable()) {
                    Vec3 closestPoint = getClosestPointOnLine(entity.position().add(0, entity.getBbHeight() * 0.5, 0), start, maxReachPos);
                    double scaledRadius = radius + 1;
                    Vec3 scaledDistance = closestPoint.subtract(entity.position().add(0, entity.getBbHeight() * 0.5, 0)).multiply(1 / entity.getBbWidth(), 1 / entity.getBbHeight(), 1 / entity.getBbWidth());
                    if (scaledDistance.length() <= scaledRadius && closestPoint.distanceTo(start) < (entityHit != null ? entityHit.getLocation().distanceTo(start) : maxReachLength)) {
                        entityHit = new EntityHitResult(entity, new Vec3(entity.getX(), entity.getY(0.5), entity.getZ()));
                    }
                }
            }
        }
        return entityHit;
    }

    /**
     * プレイヤーの視線上のエンティティを取得する
     */
    public static EntityHitResult getEntityHitResult(Entity player, Vec3 startLoc, Vec3 endLoc, Level level) {
        double distance = startLoc.distanceTo(endLoc);
        AABB searchBox = new AABB(startLoc, endLoc).inflate(1.0); // 線分を含む範囲を検索

        List<Entity> entities = level.getEntities(player, searchBox, entity ->
                entity.isAttackable() && entity.getBoundingBox().clip(startLoc, endLoc).isPresent()
        );

        EntityHitResult closestHit = null;
        double closestDistance = distance;

        for (Entity entity : entities) {
            AABB boundingBox = entity.getBoundingBox();
            Optional<Vec3> hit = boundingBox.clip(startLoc, endLoc);

            if (hit.isPresent()) {
                double hitDistance = startLoc.distanceTo(hit.get());
                if (hitDistance < closestDistance) {
                    closestDistance = hitDistance;
                    closestHit = new EntityHitResult(entity, hit.get());
                }
            }
        }

        return closestHit;
    }

    public static Vec3 getClosestPointOnLine(Vec3 point, Vec3 a, Vec3 b) {
        Vec3 ab = b.subtract(a);
        double t = point.subtract(a).dot(ab) / ab.lengthSqr();
        t = Math.max(0, Math.min(1, t));
        return a.add(ab.scale(t));
    }
}
