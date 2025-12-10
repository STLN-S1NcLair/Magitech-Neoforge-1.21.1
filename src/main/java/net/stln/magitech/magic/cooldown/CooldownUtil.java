package net.stln.magitech.magic.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ibm.icu.impl.Pair;
import com.ibm.icu.impl.UResource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.TableHelper;

import java.util.ArrayList;
import java.util.List;

public class CooldownUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player) {
            Table<Player, Spell, Cooldown> prevData = CooldownData.getPrevCooldownMap(player.level().isClientSide);
            List<Spell> spellsToAdd = new ArrayList<>();
            TableHelper.forEach(prevData, (r, spell, v) -> {
                if (r.equals(player)) {
                    spellsToAdd.add(spell);
                }
            });
            for (Spell spell : spellsToAdd) {
                CooldownData.removePrevCooldown(player, spell);
            }
            Table<Player, Spell, Cooldown> data = CooldownData.getCooldownMap(player.level().isClientSide);
            List<Pair<Spell, Cooldown>> spellsToAdd2 = new ArrayList<>();
            List<Spell> spellsToRemove = new ArrayList<>();
            TableHelper.forEach(data, (player1, spell, cooldown) -> {
                if (player1.equals(player)) {
                    cooldown.setProgress(cooldown.getProgress() + 1);
                    if (cooldown.getProgress() > cooldown.getCooltime()) {
                        spellsToRemove.add(spell);
                    } else {
                        spellsToAdd2.add(Pair.of(spell, cooldown));
                    }
                }
            });
            for (Pair<Spell, Cooldown> pair : spellsToAdd2) {
                CooldownData.addCurrentCooldown(player, pair.first, pair.second);
            }
            for (Spell spell : spellsToRemove) {
                CooldownData.removeCooldown(player, spell);
            }
        }
    }
}
