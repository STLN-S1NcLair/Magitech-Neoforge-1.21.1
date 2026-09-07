package net.stln.magitech.feature.magic.cooldown;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.ISpell;
import org.jetbrains.annotations.Nullable;

public class CooldownHelper {

    public static void updateCooldown(LivingEntity entity, ISpell spell, int cooldownTick) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        data.update(spell, cooldownTick);
        entity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
    }

    public static void updateCooldown(LivingEntity entity, ISpell spell, @Nullable ItemStack wand) {
        int cooldownTick = MagicPerformanceHelper.getEffectiveCooldown(entity, wand, spell);
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        data.update(spell, cooldownTick);
        entity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
    }

    public static void extendCooldown(LivingEntity entity, ISpell spell, int cooldownTick) {
        int currentCooldown = getCooldownTime(entity, spell);
        updateCooldown(entity, spell, cooldownTick + currentCooldown);
    }

    public static boolean isCooldown(LivingEntity entity, ISpell spell) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        return data.isCooldown(spell);
    }

    public static int getCooldownTime(LivingEntity entity, ISpell spell) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        CooldownData.Cooldown cooldown = data.get(spell);
        if (cooldown == null) {
            return 0;
        }
        return cooldown.remaining();
    }

}
