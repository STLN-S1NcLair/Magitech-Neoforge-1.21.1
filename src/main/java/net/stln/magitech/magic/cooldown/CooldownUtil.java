package net.stln.magitech.magic.cooldown;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.Map2d;

public class CooldownUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player) {
            Map2d<Player, Spell, Cooldown> data = CooldownData.getCooldownMap(player.level().isClientSide);
            if (data != null) {
                data.forEach(player, ((spell, cooldown) -> {
                    cooldown.setProgress(cooldown.getProgress() + 1);
                    if (cooldown.getProgress() > cooldown.getCooltime()) {
                        CooldownData.removeCooldown(player, spell);
                    }
                }));
                }
            }
        }
    }
