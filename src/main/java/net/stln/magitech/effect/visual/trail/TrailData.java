package net.stln.magitech.effect.visual.trail;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.function.Function;

public record TrailData(Level level, Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc, TrailPointBuilder trail, Color primary, Color secondary, float scale, float alpha) {

    public void addTrailPoint(Vec3 point) {
        trail.addTrailPoint(point);
    }
}
