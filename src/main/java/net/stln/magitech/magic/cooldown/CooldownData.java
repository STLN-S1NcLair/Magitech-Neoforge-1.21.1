package net.stln.magitech.magic.cooldown;

import net.minecraft.world.entity.player.Player;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.Map2d;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CooldownData {
    private static Map2d<Player, Spell, Cooldown> cooldownMapClient = new Map2d<>();
    private static Map2d<Player, Spell, Cooldown> cooldownMapServer = new Map2d<>();

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

    public static void cleanUp() {
        cooldownMapClient = new Map2d<>();
        cooldownMapServer = new Map2d<>();
    }

    public static void addCurrentCooldown(Player player, Spell spell, Cooldown cooldown) {
        if (player.level().isClientSide) {
            cooldownMapClient.put(player, spell, cooldown);
        } else {
            CooldownData.cooldownMapServer.put(player, spell, cooldown);
        }
    }

    public static void removeCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            CooldownData.cooldownMapClient.remove(player, spell);
        } else {
            CooldownData.cooldownMapServer.remove(player, spell);
        }
    }

    public static Cooldown getCurrentCooldown(Player player, Spell spell) {
        if (player.level().isClientSide) {
            return CooldownData.cooldownMapClient.getOrDefault(player, spell, null);
        }
        return CooldownData.cooldownMapServer.getOrDefault(player, spell, null);
    }
}
