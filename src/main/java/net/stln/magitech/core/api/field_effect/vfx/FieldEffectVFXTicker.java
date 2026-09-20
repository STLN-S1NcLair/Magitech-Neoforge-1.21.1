package net.stln.magitech.core.api.field_effect.vfx;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.stln.magitech.Magitech;

/**
 * クライアントのワールド tick ごとにフィールド効果のVFXを更新します。
 * Updates field-effect VFX on each client world tick.
 */
@EventBusSubscriber(modid = Magitech.MOD_ID)
public class FieldEffectVFXTicker {

    /**
     * クライアント側のフィールド効果VFXを更新します。
     * Updates field-effect VFX on the client side.
     */
    @SubscribeEvent
    public static void tickVFX(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (!level.isClientSide()) return;
        FieldEffectRenderer.forEachRenderablePos(level);
    }
}
