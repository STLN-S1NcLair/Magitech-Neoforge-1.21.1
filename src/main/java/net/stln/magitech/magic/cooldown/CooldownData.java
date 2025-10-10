package net.stln.magitech.magic.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.TableHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CooldownData {
    private static final Table<Player, Spell, Cooldown> cooldownMapClient = HashBasedTable.create();
    private static final Table<Player, Spell, Cooldown> cooldownMapServer = HashBasedTable.create();
    private static final Table<Player, Spell, Cooldown> prevCooldownMapClient = HashBasedTable.create();
    private static final Table<Player, Spell, Cooldown> prevCooldownMapServer = HashBasedTable.create();

    public static @NotNull Table<Player, Spell, Cooldown> getCooldownMapClient() {
        return cooldownMapClient;
    }

    public static @NotNull Table<Player, Spell, Cooldown> getCooldownMapServer() {
        return cooldownMapServer;
    }

    public static @NotNull Table<Player, Spell, Cooldown> getCooldownMap(boolean isClient) {
        return isClient ? cooldownMapClient : cooldownMapServer;
    }

    public static @NotNull Table<Player, Spell, Cooldown> getPrevCooldownMap(boolean isClient) {
        return isClient ? prevCooldownMapClient : prevCooldownMapServer;
    }

    public static void cleanUp(Player player) {
        TableHelper.removeByRow(cooldownMapClient, player);
        TableHelper.removeByRow(cooldownMapServer, player);
        TableHelper.removeByRow(prevCooldownMapClient, player);
        TableHelper.removeByRow(prevCooldownMapServer, player);
    }

    public static void addCurrentCooldown(@NotNull Player player, @NotNull Spell spell, @NotNull Cooldown cooldown) {
        if (player.level().isClientSide) {
            var cooldownOld = cooldownMapClient.get(player, spell);
            if (cooldownOld != null) {
                prevCooldownMapClient.put(player, spell, cooldownOld);
            }
            cooldownMapClient.put(player, spell, cooldown);
        } else {
            var cooldownOld = cooldownMapServer.get(player, spell);
            if (cooldownOld != null) {
                prevCooldownMapServer.put(player, spell, cooldownOld);
            }
            CooldownData.cooldownMapServer.put(player, spell, cooldown);
        }
    }

    public static void removeCooldown(@NotNull Player player, @NotNull Spell spell) {
        if (player.level().isClientSide) {
            var cooldownOld = cooldownMapClient.get(player, spell);
            if (cooldownOld != null) {
                prevCooldownMapClient.put(player, spell, cooldownOld);
            }
            CooldownData.cooldownMapClient.remove(player, spell);
        } else {
            var cooldownOld = cooldownMapServer.get(player, spell);
            if (cooldownOld != null) {
                prevCooldownMapServer.put(player, spell, cooldownOld);
            }
            CooldownData.cooldownMapServer.remove(player, spell);
        }
    }

    public static void removePrevCooldown(@NotNull Player player, @NotNull Spell spell) {
        if (player.level().isClientSide) {
            CooldownData.prevCooldownMapClient.remove(player, spell);
        } else {
            CooldownData.prevCooldownMapServer.remove(player, spell);
        }
    }

    public static @Nullable Cooldown getCurrentCooldown(@NotNull Player player, @NotNull Spell spell) {
        Table<Player, Spell, Cooldown> table = player.level().isClientSide ? CooldownData.cooldownMapClient : CooldownData.cooldownMapServer;
        return table.get(player, spell);
    }

    public static @Nullable Cooldown getPrevCooldown(@NotNull Player player, @NotNull Spell spell) {
        Table<Player, Spell, Cooldown> table = player.level().isClientSide ? CooldownData.prevCooldownMapClient : CooldownData.prevCooldownMapServer;
        return table.get(player, spell);
    }
}
