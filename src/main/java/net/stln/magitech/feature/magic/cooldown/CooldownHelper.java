package net.stln.magitech.feature.magic.cooldown;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.Spell;
import org.jetbrains.annotations.Nullable;

public class CooldownHelper {

    public static void addCooldown(LivingEntity entity, Spell spell, int cooldownTick) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        data.add(spell, cooldownTick);
        entity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
    }

    public static void addCooldown(LivingEntity entity, Spell spell, @Nullable ItemStack wand) {
        int cooldownTick = MagicPerformanceHelper.getEffectiveCooldown(entity, wand, spell);
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        data.add(spell, cooldownTick);
        entity.setData(DataAttachmentInit.SPELL_COOLDOWNS.get(), data);
    }

    public static boolean isCooldown(LivingEntity entity, Spell spell) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        return data.isCooldown(spell);
    }

    public static int getCooldownTime(LivingEntity entity, Spell spell) {
        CooldownData data = entity.getData(DataAttachmentInit.SPELL_COOLDOWNS.get());
        return data.get(spell).remaining();
    }

}
