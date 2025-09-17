package net.stln.magitech.biome;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class RenderFogEvent {

    public static float currentNearPlane = 256.0f;
    public static float targetNearPlane = 256.0f;
    public static float currentFarPlane = 256.0f;
    public static float targetFarPlane = 256.0f;
    public static final float LERP_SPEED = 0.002f;

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        LocalPlayer player = Minecraft.getInstance().player;
        Level level = player.level();
        BlockPos pos = player.blockPosition();

        if (level.getBiome(pos).is(BiomeInit.MISTJADE_FOREST)) {
            targetNearPlane = 10;
            targetFarPlane = 100;
        } else if (level.getBiome(pos).is(BiomeInit.SCORCHED_PLAINS)) {
            targetNearPlane = 0;
            targetFarPlane = 125;
        } else {
            targetNearPlane = event.getNearPlaneDistance();
            targetFarPlane = event.getFarPlaneDistance();
        }
        // 線形補間 (Lerp)
        currentFarPlane += (targetFarPlane - currentFarPlane) * LERP_SPEED;
        currentNearPlane += (targetNearPlane - currentNearPlane) * LERP_SPEED;

        if ((currentNearPlane != event.getNearPlaneDistance() || currentFarPlane != event.getFarPlaneDistance()) && player.getEyeInFluidType() == NeoForgeMod.EMPTY_TYPE.value()) {
            event.setCanceled(true);
            event.setNearPlaneDistance(currentNearPlane);
            event.setFarPlaneDistance(currentFarPlane);
            event.setFogShape(FogShape.SPHERE);
        }
    }
}
