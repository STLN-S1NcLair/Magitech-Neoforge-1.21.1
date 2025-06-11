package net.stln.magitech.magic.cooldown;

import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.Map2d;

public class CooldownData {
    private static Map2d<Player, Spell, Cooldown> cooldownMapClient = new Map2d<>();
    private static Map2d<Player, Spell, Cooldown> cooldownMapServer = new Map2d<>();
    private static Map2d<Player, Spell, Cooldown> prevCooldownMapClient = new Map2d<>();
    private static Map2d<Player, Spell, Cooldown> prevCooldownMapServer = new Map2d<>();

    public static Map2d<Player, Spell, Cooldown> getCooldownMapClient() {
        return cooldownMapClient;
    }

    public static Map2d<Player, Spell, Cooldown> getCooldownMapServer() {
        return cooldownMapServer;
    }

    public static Map2d<Player, Spell, Cooldown> getCooldownMap(boolean isClient) {
        if (isClient) {
            return cooldownMapClient;
        }
        return cooldownMapServer;
    }

    public static Map2d<Player, Spell, Cooldown> getPrevCooldownMap(boolean isClient) {
        if (isClient) {
            return prevCooldownMapClient;
        }
        return prevCooldownMapServer;
    }

    public static void cleanUp(Player player) {
        cooldownMapClient.remove(player);
        cooldownMapServer.remove(player);
        prevCooldownMapClient.remove(player);
        prevCooldownMapServer.remove(player);
    }

    public static void addCurrentCooldown(Player player, Spell spell, Cooldown cooldown) {
        if (player.level().isClientSide) {
            prevCooldownMapClient.put(player, spell, cooldownMapClient.get(player, spell));
            cooldownMapClient.put(player, spell, cooldown);
        } else {
            prevCooldownMapServer.put(player, spell, cooldownMapServer.get(player, spell));
            CooldownData.cooldownMapServer.put(player, spell, cooldown);
        }
    }

    public static void removeCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            prevCooldownMapClient.put(player, spell, cooldownMapClient.get(player, spell));
            CooldownData.cooldownMapClient.remove(player, spell);
        } else {
            prevCooldownMapServer.put(player, spell, cooldownMapServer.get(player, spell));
            CooldownData.cooldownMapServer.remove(player, spell);
        }
    }

    public static void removePrevCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            CooldownData.prevCooldownMapClient.remove(player, spell);
        } else {
            CooldownData.prevCooldownMapServer.remove(player, spell);
        }
    }

    public static Cooldown getCurrentCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            return CooldownData.cooldownMapClient.getOrDefault(player, spell, null);
        }
        return CooldownData.cooldownMapServer.getOrDefault(player, spell, null);
    }

    public static Cooldown getPrevCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            return CooldownData.prevCooldownMapClient.getOrDefault(player, spell, null);
        }
        return CooldownData.prevCooldownMapServer.getOrDefault(player, spell, null);
    }
}
