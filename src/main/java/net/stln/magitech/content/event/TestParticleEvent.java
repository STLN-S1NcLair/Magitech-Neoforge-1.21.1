package net.stln.magitech.content.event;

//import com.sammy.malum.registry.common.MalumParticles;
//import com.sammy.malum.visual_effects.SpiritLightSpecs;

import com.sammy.malum.registry.client.MalumRenderTypeTokens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.vfx.renderer.TrailRenderHelper;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT, modid = Magitech.MOD_ID)
public class TestParticleEvent {

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
            trailMap.put(player, TrailPointBuilder.create(20));
        }
        TrailPointBuilder trail = trailMap.get(player);
        trail.tickTrailPoints();
        trail.addTrailPoint(player.position());
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            TrailPointBuilder trailPointBuilder = trailMap.get(Minecraft.getInstance().player);
            if (trailPointBuilder != null) {
                var poseStack = event.getPoseStack();
                var trail = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(MalumRenderTypeTokens.CONCENTRATED_TRAIL);
                var builder = new VFXBuilders.WorldVFXBuilder().setColor(new Color(0x00FFFF));

                TrailRenderHelper.renderTrail(poseStack, builder.setRenderType(trail), trailPointBuilder, Color.CYAN, Color.MAGENTA, 1, 1, event.getPartialTick().getRealtimeDeltaTicks());
            }
        }
    }
}
