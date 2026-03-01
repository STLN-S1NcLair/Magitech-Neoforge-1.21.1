package net.stln.magitech.feature.magic.cooldown;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.magic.spell.Spell;

public class CooldownHelper {

    public static void addCooldown(LivingEntity entity, Spell spell, int cooldownTick) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        data.cooldowns().put(spell, cooldownTick);
        entity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
    }

}
