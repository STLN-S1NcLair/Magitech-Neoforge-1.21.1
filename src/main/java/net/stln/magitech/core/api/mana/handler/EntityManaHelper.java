package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.data.DataAttachmentInit;

public class EntityManaHelper {

    public static void tick(LivingEntity entity) {
        if (!entity.level().isClientSide) {
                EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
                if (handler != null) {
                    handler.addMana(getEnergyManaRegen(entity) / 20);
                }
        }
    }

    public static float getMagicMana(LivingEntity entity) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            return handler.getMana() / 1000F;
        }
        return 0;
    }

    public static double getMagicManaFillRatio(LivingEntity entity) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            return handler.fillRatio();
        }
        return 0;
    }

    public static void addMagicMana(LivingEntity entity, float amount) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            handler.addMana(((long) amount) * 1000L);
        }
    }

    // attributeとの互換性を保つためのヘルパー関数

    public static long getEnergyMaxMana(LivingEntity entity) {
        return attributeToMana(entity.getAttributeValue(AttributeInit.MAX_MANA));
    }

    public static long getEnergyManaRegen(LivingEntity entity) {
        return attributeToMana(entity.getAttributeValue(AttributeInit.MANA_REGEN));
    }

    public static long attributeToMana(double attributeValue) {
        return (long) (attributeValue * 1000L);
    }
}
