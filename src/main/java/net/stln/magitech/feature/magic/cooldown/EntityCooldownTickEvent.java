package net.stln.magitech.feature.magic.cooldown;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.data.DataAttachmentInit;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityCooldownTickEvent {

    @SubscribeEvent
    public static void cooldownTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entity instanceof LivingEntity livingEntity) {
            CooldownData data = livingEntity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
            data.tick();
            livingEntity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
        }
    }
}
