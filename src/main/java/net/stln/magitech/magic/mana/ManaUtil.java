package net.stln.magitech.magic.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.network.SyncManaPayload;

import java.util.Map;

public class ManaUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            regenAllMana(player);
        }
    }

    private static void regenAllMana(Player player) {
        regenMana(player, ManaType.MANA);
        regenMana(player, ManaType.NOCTIS);
        regenMana(player, ManaType.LUMINIS);
        regenMana(player, ManaType.FLUXIA);
    }

    public static void regenMana(Player player, ManaType type) {
        double regenAmount = getManaRegen(player, type);
        setMana(player, type, Math.min(regenAmount + ManaData.getCurrentMana(player, type), getMaxMana(player, type)));
    }

    public static boolean useMana(Player player, Map<ManaType, Double> map) {
        boolean flag = true;
        for (Map.Entry<ManaType, Double> entry : map.entrySet()) {
            ManaType type = entry.getKey();
            Double value = entry.getValue();
            if (value > ManaData.getCurrentMana(player, type)) {
                flag = false;
            }
        }
        if (flag) {
            if (!player.isCreative()) {
                for (Map.Entry<ManaType, Double> entry : map.entrySet()) {
                    ManaType type = entry.getKey();
                    Double value = entry.getValue();
                    setMana(player, type, ManaData.getCurrentMana(player, type) - value);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean useManaServerOnly(Player player, Map<ManaType, Double> map) {
        boolean flag = true;
        for (Map.Entry<ManaType, Double> entry : map.entrySet()) {
            ManaType type = entry.getKey();
            Double value = entry.getValue();
            if (value > ManaData.getCurrentMana(player, type)) {
                flag = false;
            }
        }
        if (flag) {
            if (!player.level().isClientSide && !player.isCreative()) {
                for (Map.Entry<ManaType, Double> entry : map.entrySet()) {
                    ManaType type = entry.getKey();
                    Double value = entry.getValue();
                    setMana(player, type, ManaData.getCurrentMana(player, type) - value);
                }
            }
            return true;
        }
        return false;
    }

    private static void setMana(Player player, ManaType type, double value) {
        ManaData.setCurrentMana(player, type, value);
        if (player.level().isClientSide) {
            PacketDistributor.sendToServer(new SyncManaPayload(value, type.id, player.getStringUUID()));
        } else {
            PacketDistributor.sendToAllPlayers(new SyncManaPayload(value, type.id, player.getStringUUID()));
        }
    }

    public static double getManaRegen(Player player, ManaType type) {
        return switch (type) {
            case MANA ->
                    player.getAttribute(AttributeInit.MANA_REGEN) == null ? 0 : player.getAttribute(AttributeInit.MANA_REGEN).getValue();
            case NOCTIS ->
                    player.getAttribute(AttributeInit.NOCTIS_REGEN) == null ? 0 : player.getAttribute(AttributeInit.NOCTIS_REGEN).getValue();
            case LUMINIS ->
                    player.getAttribute(AttributeInit.LUMINIS_REGEN) == null ? 0 : player.getAttribute(AttributeInit.LUMINIS_REGEN).getValue();
            case FLUXIA ->
                    player.getAttribute(AttributeInit.FLUXIA_REGEN) == null ? 0 : player.getAttribute(AttributeInit.FLUXIA_REGEN).getValue();
        };
    }

    public static double getMaxMana(Player player, ManaType type) {
        return switch (type) {
            case MANA ->
                    player.getAttribute(AttributeInit.MAX_MANA) == null ? 0 : player.getAttribute(AttributeInit.MAX_MANA).getValue();
            case NOCTIS ->
                    player.getAttribute(AttributeInit.MAX_NOCTIS) == null ? 0 : player.getAttribute(AttributeInit.MAX_NOCTIS).getValue();
            case LUMINIS ->
                    player.getAttribute(AttributeInit.MAX_LUMINIS) == null ? 0 : player.getAttribute(AttributeInit.MAX_LUMINIS).getValue();
            case FLUXIA ->
                    player.getAttribute(AttributeInit.MAX_FLUXIA) == null ? 0 : player.getAttribute(AttributeInit.MAX_FLUXIA).getValue();
        };
    }

    public enum ManaType {
        MANA(0),
        NOCTIS(1),
        LUMINIS(2),
        FLUXIA(3);

        final int id;

        ManaType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static ManaType getManaType(int id) {
        for (ManaType manatype : ManaType.values()) {
            if (manatype.id == id) {
                return manatype;
            }
        }
        return null;
    }
}
