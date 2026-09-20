package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.core.api.mana.ManaCapabilities;

/**
 * エンティティのマナ量・回復量を扱うヘルパーです。
 * Helper methods for reading and modifying entity mana and regeneration.
 */
public class EntityManaHelper {

    /**
     * サーバー側でエンティティのマナを毎 tick 回復します。
     * Regenerates entity mana on the server each tick.
     */
    public static void tick(LivingEntity entity) {
        if (!entity.level().isClientSide) {
            EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
            if (handler != null) {
                handler.addMana(getEnergyManaRegen(entity) / 20);
            }
        }
    }

    /**
     * エンティティの表示用マナ量を返します。
     * Returns the entity's mana amount in display units.
     */
    public static float getMagicMana(LivingEntity entity) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            return handler.getMana() / 1000F;
        }
        return 0;
    }

    /**
     * エンティティのマナ充填率を返します。
     * Returns the entity's mana fill ratio.
     */
    public static double getMagicManaFillRatio(LivingEntity entity) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            return handler.fillRatio();
        }
        return 0;
    }

    /**
     * 表示単位のマナをエンティティへ加算します。
     * Adds display-unit mana to an entity.
     */
    public static void addMagicMana(LivingEntity entity, float amount) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            handler.addMana((long) (amount * 1000L));
        }
    }

    // attributeとの互換性を保つためのヘルパー関数 / Helper functions for maintaining compatibility with attributes

    /**
     * 属性値からエンティティの最大マナを取得します。
     * Gets an entity's maximum mana from its attribute value.
     */
    public static long getEnergyMaxMana(LivingEntity entity) {
        return attributeToMana(entity.getAttributeValue(AttributeInit.MAX_MANA));
    }

    /**
     * 属性値からエンティティの毎秒マナ回復量を取得します。
     * Gets an entity's mana regeneration per second from its attribute value.
     */
    public static long getEnergyManaRegen(LivingEntity entity) {
        return attributeToMana(entity.getAttributeValue(AttributeInit.MANA_REGEN));
    }

    /**
     * 属性値を内部マナ単位へ変換します。
     * Converts an attribute value to internal mana units.
     */
    public static long attributeToMana(double attributeValue) {
        return (long) (attributeValue * 1000L);
    }
}
