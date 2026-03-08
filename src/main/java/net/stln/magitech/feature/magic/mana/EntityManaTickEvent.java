package net.stln.magitech.feature.magic.mana;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.data.DataAttachmentInit;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityManaTickEvent {

    @SubscribeEvent
    public static void manaTick(EntityTickEvent.Pre event) {
        // サーバーサイドのみで処理(Sync済み)
        if (event.getEntity() instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide) {
            EntityManaHelper.tick(livingEntity);
        }
    }
}
