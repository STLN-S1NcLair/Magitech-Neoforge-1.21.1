package net.stln.magitech.network;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.entity.mob.WeaverEntity;

public class RangedEntityAttackPayLoadHandler {

    public static void handleDataOnMainS2C(final RangedEntityAttackPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        if (level.getEntity(payload.id()) instanceof RangedAttackMob mob && level.getEntity(payload.targetId()) instanceof LivingEntity target) {
            if (mob instanceof WeaverEntity weaver) {
                weaver.performSpell(level, target, payload.index());
            }
        }
    }
}
