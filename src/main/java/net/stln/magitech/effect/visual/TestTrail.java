package net.stln.magitech.effect.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.preset.BehaviorPreset;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@EventBusSubscriber(value = Dist.CLIENT, modid = Magitech.MOD_ID)
public class TestTrail {

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            spawnExampleParticles(player);
        }
    }

    private static final Map<Player, TrailPointBuilder> trailMap = new HashMap<>();

    public static void spawnExampleParticles(Player player) {
        if (!trailMap.containsKey(player)) {
            trailMap.put(player, TrailPointBuilder.create(10));
        }
        TrailPointBuilder trail = trailMap.get(player);
        trail.tickTrailPoints();
        trail.addTrailPoint(player.position());

        Vec3 direction = player.getDeltaMovement().normalize();
        Vec3 motion = direction.scale(0.25f);
        Consumer<LodestoneWorldParticle> behavior = BehaviorPreset.toDestination(player.position());

        ParticleEffectSpawner spawner = SquareParticles.squareParticle(player.level(), player.position(), Element.MANA);
        spawner.getBuilder().setMotion(motion).addTickActor(behavior);
        spawner.getBloomBuilder().setMotion(motion).addTickActor(behavior);
        spawner.spawnParticles();
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            TrailPointBuilder trailPointBuilder = trailMap.get(Minecraft.getInstance().player);
            if (trailPointBuilder != null) {
                var poseStack = event.getPoseStack();
                var trail = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
                var builder = new VFXBuilders.WorldVFXBuilder().setColor(new Color(0xFFFFFF));

                TrailRenderHelper.renderTrail(poseStack, builder.setRenderType(trail), trailPointBuilder, Element.MANA.getTextColor(), Element.MANA.getSecondary(), 0.5F, 0.3F, event.getPartialTick().getRealtimeDeltaTicks());
            }
        }
    }
}
