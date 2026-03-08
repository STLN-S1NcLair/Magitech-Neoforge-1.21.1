package net.stln.magitech.feature.magic.charge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.data.DataAttachmentInit;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityChargeTickEvent {

    @SubscribeEvent
    public static void chargeTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entity instanceof LivingEntity livingEntity) {
            ChargeData data = livingEntity.getData(DataAttachmentInit.SPELL_CHARGE);
            ChargeData.Charge charge = data.charge();
            if (charge.remaining() > 2) {
                entity.setData(DataAttachmentInit.SPELL_CHARGE, data.tick());
            } else {
                entity.setData(DataAttachmentInit.SPELL_CHARGE, ChargeData.empty());
            }
        }
    }
}
