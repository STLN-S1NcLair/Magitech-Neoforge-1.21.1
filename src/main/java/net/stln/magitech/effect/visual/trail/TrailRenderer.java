package net.stln.magitech.effect.visual.trail;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = Magitech.MOD_ID)
public class TrailRenderer {

    private static final List<TrailData> trails = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        Level level = Minecraft.getInstance().level;
        tick(level);
    }

    public static void tick(Level level) {
        for (TrailData trailData : new HashSet<>(trails)) {
            TrailPointBuilder trail = trailData.trail();
            // 描画しない場合はremove
            if (!trailData.level().equals(level) || trail.getTrailPoints().isEmpty()) {
                trails.remove(trailData);
            }
            trail.tickTrailPoints();
        }
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getRenderTick() > 20 && event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            for (TrailData trailData : trails) {
                if (trailData != null) {
                    var poseStack = event.getPoseStack();
                    var builder = trailData.builderFunc().apply(new VFXBuilders.WorldVFXBuilder());

                    TrailRenderHelper.renderTrail(poseStack, builder, trailData.trail(), trailData.primary(), trailData.secondary(), trailData.scale(), trailData.alpha(), event.getPartialTick().getRealtimeDeltaTicks());
                }
            }
        }
    }

    public static void add(TrailData data) {
        trails.add(data);
    }
}
