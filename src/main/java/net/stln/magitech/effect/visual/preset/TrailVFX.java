package net.stln.magitech.effect.visual.preset;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.helper.VectorHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.InterpolatedTrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class TrailVFX {

    final static float FACTOR = 1.4F;

    public static void directionalTrail(Level level, Vec3 start, Vec3 end, float scale, int trailLength, Element element) {
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(trailLength);

        //端点処理
        addTrailPoint(level, start, 0.0F, trailLength, trail);

        for (int i = 1; i < trailLength; i++) {
            float progress = (float) i / trailLength;
            Vec3 lerped = start.lerp(end, progress);
            addTrailPoint(level, lerped, 0.0F, trailLength, trail);
            trail.tickTrailPoints();
        }
        addTrailPoint(level, end, 0.0F, trailLength, trail);

        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), scale * 0.5F, 0.9F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), scale, 0.5F));
    }

    public static void arcTrail(Level level, Vec3 center, Vec2 normal, float startDeg, float endDeg, float slopeDeg, float scale, float radius, float resolution, int trailLength, Element element) {

        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(trailLength);

        int points = (int) (resolution * radius * org.joml.Math.abs(endDeg - startDeg) / 50);

        List<Integer> tickPoints = new ArrayList<>();

        float tickDist = (float) points / trailLength * 2;
        for (int i = 1; i <= trailLength; i++) {
            tickPoints.add((int) Math.floor(tickDist * i));
        }

        Vec3 lookVec = Vec3.directionFromRotation(normal); // プレイヤーの視線方向
        double yawRad = org.joml.Math.toRadians(normal.y);

        // **視線方向に基づく「右方向ベクトル」を計算**
        Vec3 rightVec = new Vec3(org.joml.Math.cos(yawRad), 0, org.joml.Math.sin(yawRad)).normalize(); // 視線の右方向
        Vec3 upVec = lookVec.cross(rightVec).normalize(); // 視線に対する上方向

        for (int i = 0; i <= points; i++) {
            double t = (double) i / (points - 1);
            double angleDeg = startDeg + (endDeg - startDeg) * t;

            Vec3 axisVec = VectorHelper.rotateVector(upVec, lookVec, slopeDeg);
            Vec3 offset = VectorHelper.rotateVector(lookVec, axisVec, angleDeg);

            double x = center.x - offset.x * radius;
            double y = center.y - offset.y * radius;
            double z = center.z - offset.z * radius;

            addTrailPoint(level, new Vec3(x, y, z), 0.0F, trailLength, trail);
            if (tickPoints.contains(i)) {
                trail.tickTrailPoints();
            }
        }

        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), scale * 0.5F, 0.9F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), scale, 0.5F));
    }

    // complexity: 1mあたりの端点の数, randomness: 端点の散らばり
    public static void zapTrail(Level level, Vec3 start, Vec3 end, float scale, float complexity, float randomness, int trailLength, Element element) {
        var renderType = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = (builder) -> builder.setRenderType(renderType).setColor(new Color(0xFFFFFF));
        TrailPointBuilder trail = TrailPointBuilder.create(trailLength);

        //端点処理
        double length = start.distanceTo(end);
        int segments = (int) Math.max((length * complexity), 1);
        List<TrailPoint> trailPoints = new ArrayList<>();
        addTrailPoint(level, start, 0.0F, trailLength, trail, trailPoints);
        trail.tickTrailPoints();
        for (int i = 1; i < segments; i++) {
            float progress = (float) i / segments;
            Vec3 lerped = start.lerp(end, progress);
            addTrailPoint(level, lerped, randomness, trailLength, trail, trailPoints);
            if (segments / trailLength == 0 || i % (segments / trailLength) == 0) {
                trail.tickTrailPoints();
            }
        }
        addTrailPoint(level, end, 0.0F, trailLength, trail, trailPoints);

        // 逆の軌跡も追加
        trailPoints = trailPoints.reversed();
        TrailPointBuilder reversed = TrailPointBuilder.create(trailLength);
        for (int i = 0; i < trailPoints.size(); i++) {
            reversed.addTrailPoint(trailPoints.get(i));
            if (segments / trailLength == 0 || i % (segments / trailLength) == 0) {
                reversed.tickTrailPoints();
            }
        }

        addZap(level, scale, element, builderFunc, trail);
        addZap(level, scale, element, builderFunc, reversed);
    }

    public static void directionalZapTrail(Level level, Vec3 start, Vec3 end, float scale, float complexity, float randomness, int trailLength, Element element) {
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(trailLength);

        //端点処理
        double length = start.distanceTo(end);
        int segments = (int) Math.max((length * complexity), 1);

        addTrailPoint(level, end, 0.0F, trailLength, trail);

        for (int i = 1; i < segments; i++) {
            float progress = (float) i / segments;
            Vec3 lerped = end.lerp(start, progress);
            addTrailPoint(level, lerped, randomness, trailLength, trail);
            if (segments / trailLength == 0 || i % (segments / trailLength) == 0) {
                trail.tickTrailPoints();
            }
        }
        addTrailPoint(level, start, 0.0F, trailLength, trail);

        addZap(level, scale, element, builderFunc, trail);
    }

    private static void addZap(Level level, float scale, Element element, Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc, TrailPointBuilder trail) {
        TrailRenderer.add(new TrailData(level, builderFunc, trail, ColorHelper.Argb.dilute(element.getPrimary()), element.getPrimary(), scale * 0.5F, 1.0F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), scale, 1.0F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getSecondary(), element.getDark(), scale * 2, 1.0F));
    }

    // delta: start - end間のlerp位置
    private static TrailPoint addTrailPoint(Level level, Vec3 pos, float randomness, int trailLength, TrailPointBuilder builder) {
        Vec3 offset = VectorHelper.random(level.random).scale(randomness);


        Vec3 start = pos.add(offset);
        Vec3 end = pos.add(offset.scale(FACTOR));
        builder.addTrailPoint(new InterpolatedTrailPoint(start, end, trailLength));

        Vec3 start2 = pos.add(offset);
        Vec3 end2 = pos.add(offset.scale(FACTOR));
        return new InterpolatedTrailPoint(start2, end2, trailLength);
    }

    private static void addTrailPoint(Level level, Vec3 pos, float randomness, int trailLength, TrailPointBuilder builder, List<TrailPoint> trailPoints) {
        trailPoints.add(addTrailPoint(level, pos, randomness, trailLength, builder));
    }
}
