package net.stln.magitech.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

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
     * @param player プレイヤー
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

    /**
     * プレイヤーの視線上のエンティティを取得する
     */
    public static EntityHitResult getEntityHitResult(Player player, Vec3 start, Vec3 end, Level level) {
        double maxDistance = start.distanceTo(end);
        EntityHitResult closestHit = null;
        double closestDistance = maxDistance;

        for (var entity : level.getEntities(player, player.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0))) {
            if (!entity.isAlive()) continue;

            // エンティティの当たり判定ボックスを取得
            var aabb = entity.getBoundingBox().inflate(0.1);
            var optionalHit = aabb.clip(start, end);

            if (optionalHit.isPresent()) {
                double distance = start.distanceToSqr(optionalHit.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestHit = new EntityHitResult(entity, optionalHit.get());
                }
            }
        }

        return closestHit;
    }
}
