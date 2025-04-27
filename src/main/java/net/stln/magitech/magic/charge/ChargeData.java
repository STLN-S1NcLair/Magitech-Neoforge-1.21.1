package net.stln.magitech.magic.charge;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class ChargeData {
    private static Map<Player, Charge> chargeMapClient = new HashMap<>();
    private static Map<Player, Charge> chargeMapServer = new HashMap<>();

    public static Map<Player, Charge> getChargeMapClient() {
        return chargeMapClient;
    }

    public static Map<Player, Charge> getChargeMapServer() {
        return chargeMapServer;
    }

    public static Map<Player, Charge> getChargeMap(boolean isClient) {
        if (isClient) {
            return chargeMapClient;
        }
        return chargeMapServer;
    }

    public static void cleanUp() {
        chargeMapClient = new HashMap<>();
        chargeMapServer = new HashMap<>();
    }

    public static void setCurrentCharge(Player player, Charge charge) {
        if (player.level().isClientSide) {
            ChargeData.chargeMapClient.put(player, charge);
        } else {
            ChargeData.chargeMapServer.put(player, charge);
        }
    }

    public static void removeCharge(Player player) {
        if (player.level().isClientSide) {
            ChargeData.chargeMapClient.remove(player);
        } else {
            ChargeData.chargeMapServer.remove(player);
        }
    }

    public static Charge getCurrentCharge(Player player) {
        if (player.level().isClientSide) {
            return ChargeData.chargeMapClient.getOrDefault(player, null);
        }
        return ChargeData.chargeMapServer.getOrDefault(player, null);
    }
}
