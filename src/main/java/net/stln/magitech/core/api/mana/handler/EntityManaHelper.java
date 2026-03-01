package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.core.api.mana.ManaCapabilities;

public class EntityManaHelper {

    public static void tick(LivingEntity entity) {
        if (!entity.level().isClientSide) {
            addMana(entity, getEnergyManaRegen(entity));
        }
    }

    public static long getMana(LivingEntity entity) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            return handler.getMana();
        }
        return 0;
    }

    public static void addMana(LivingEntity entity, long amount) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            handler.addMana(amount);
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
