package net.stln.magitech.magic.cooldown;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.Map2d;

import java.util.ArrayList;
import java.util.List;

public class CooldownUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player) {
            Map2d<Player, Spell, Cooldown> prevData = CooldownData.getPrevCooldownMap(player.level().isClientSide);
            if (prevData != null) {
                List<Spell> remove = new ArrayList<>();
                prevData.forEach(player, ((spell, currentCooldown) ->
                        remove.add(spell)
                ));
                for (Spell spell : remove) {
                    CooldownData.removePrevCooldown(player, spell);
                }
            }
            Map2d<Player, Spell, Cooldown> data = CooldownData.getCooldownMap(player.level().isClientSide);
            if (data != null) {
                List<Spell> remove = new ArrayList<>();
                data.forEach(player, ((spell, currentCooldown) -> {
                    Cooldown cooldown = currentCooldown;
                    cooldown.setProgress(cooldown.getProgress() + 1);
                    if (cooldown.getProgress() > cooldown.getCooltime()) {
                        remove.add(spell);
                    } else {
                        CooldownData.addCurrentCooldown(player, spell, cooldown);
                    }
                }));
                for (Spell spell : remove) {
                    CooldownData.removeCooldown(player, spell);
                }
            }
        }
    }
}
