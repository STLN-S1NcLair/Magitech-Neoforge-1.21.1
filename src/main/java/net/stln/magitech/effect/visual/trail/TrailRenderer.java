package net.stln.magitech.effect.visual.trail;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.*;

@EventBusSubscriber(value = Dist.CLIENT, modid = Magitech.MOD_ID)
public class TrailRenderer {

    private static final List<TrailData> trails = new ArrayList<>();

    // エンティティ用Trailデータマップ
    private static final Map<Entity, Map<Integer, TrailData>> entityTrails = new HashMap<>();

    public static final int TRAIL = 0;
    public static final int LONG_TRAIL = 1;
    public static final int HIGHLIGHT_TRAIL = 2;

    @OnlyIn(Dist.CLIENT)
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
                entityTrails.forEach((entity, integerTrailDataMap) -> new HashMap<>(integerTrailDataMap).forEach((integer, data) -> {
                    if (data.equals(trailData)) {
                        integerTrailDataMap.remove(integer);
                    }
                }));
            }
            trail.tickTrailPoints();
        }
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getRenderTick() > 20 && event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES && RenderSystem.getShaderFogShape() != null) {
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

    // 毎Tick実行するなどの追跡できないTrail用
    public static void add(Entity entity, TrailData data, int index) {
        Map<Integer, TrailData> datas = entityTrails.computeIfAbsent(entity, k -> new HashMap<>());
        datas.put(index, data);
        trails.add(data);
    }

    public static void addTrailPoint(Entity entity, TrailPoint point, int index) {
        TrailData data = entityTrails.get(entity).get(index);
        data.addTrailPoint(point);
    }

    public static void updateTrail(Entity entity, TrailData data, TrailPoint point, int index) {
        if (!entityTrails.containsKey(entity) || !entityTrails.get(entity).containsKey(index)) {
            add(entity, data, index);
        }
        addTrailPoint(entity, point, index);
    }
}
