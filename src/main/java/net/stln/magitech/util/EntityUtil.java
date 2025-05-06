package net.stln.magitech.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Optional;

public class EntityUtil {

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

    /**
     * プレイヤーの視線上のエンティティを取得する
     */
    public static EntityHitResult getEntityHitResult(Player player, Vec3 startLoc, Vec3 endLoc, Level level) {
        double distance = startLoc.distanceTo(endLoc);
        AABB searchBox = new AABB(startLoc, endLoc).inflate(1.0); // 線分を含む範囲を検索

        List<Entity> entities = level.getEntities(player, searchBox, entity ->
                entity.isPickable() && entity.getBoundingBox().clip(startLoc, endLoc).isPresent()
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
}
