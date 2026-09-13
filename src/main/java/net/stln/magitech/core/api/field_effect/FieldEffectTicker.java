package net.stln.magitech.core.api.field_effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.field_effect.data.FieldEffectProcessingManager;

/**
 * ワールド tick ごとにフィールド効果を適用します。
 * Applies field effects on each world tick.
 */
@EventBusSubscriber(modid = Magitech.MOD_ID)
public class FieldEffectTicker {

    private FieldEffectTicker() {
    }

    /**
     * ロード済みのフィールド範囲内にあるエンティティへ効果を適用します。
     * Applies effects to entities inside loaded field ranges.
     *
     * @param event ワールド tick イベント / world tick event
     */
    @SubscribeEvent
    public static void tickFieldEffects(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;
        FieldEffectProcessingManager.get(serverLevel).tick(serverLevel);
    }
}
