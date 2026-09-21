package net.stln.magitech.effect.visual;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.stln.magitech.Magitech;

/**
 * フィールド効果のクライアントシェーダー登録を担当します。
 * Registers client shaders used by field effects.
 */
@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public final class FieldEffectShaderRegistry {
    private FieldEffectShaderRegistry() {
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        RenderTypeTokenInit.registerShaders(event);
    }
}
