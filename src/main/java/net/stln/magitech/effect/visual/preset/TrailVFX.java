package net.stln.magitech.effect.visual.preset;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class TrailVFX {

    // complexity: 1mあたりの端点の数, randomness: 端点の散らばり
    public static void zapTrail(Level level, Vec3 start, Vec3 end, float scale, float complexity, float randomness, int trailLength, Element element) {
        var renderType = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = (builder) -> builder.setRenderType(renderType).setColor(new Color(0xFFFFFF));
        TrailPointBuilder trail = TrailPointBuilder.create(trailLength);

        //端点処理
        double length = start.distanceTo(end);
        int segments = (int) (length * complexity);
        trail.addTrailPoint(start);
        trail.tickTrailPoints();
        for (int i = 1; i < segments; i++) {
            float progress = (float) i / segments;
            Vec3 offset = VectorHelper.random().scale(randomness);
            Vec3 lerped = start.lerp(end, progress);
            trail.addTrailPoint(lerped.add(offset));
            if (segments / trailLength == 0 || i % (segments / trailLength) == 0) {
                trail.tickTrailPoints();
            }
        }
        trail.addTrailPoint(end);

        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getColor(), element.getSecondary(), scale * 0.5F, 1.0F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getColor(), element.getDark(), scale, 1.0F));
        TrailRenderer.add(new TrailData(level, builderFunc, trail, element.getColor(), element.getSecondary(), scale * 2, 1.0F));
    }
}
