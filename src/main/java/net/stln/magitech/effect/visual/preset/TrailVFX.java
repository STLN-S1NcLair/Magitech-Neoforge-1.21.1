package net.stln.magitech.effect.visual.preset;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.VectorHelper;
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
        var renderType = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = (builder) -> builder.setRenderType(renderType).setColor(new Color(0xFFFFFF));
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
