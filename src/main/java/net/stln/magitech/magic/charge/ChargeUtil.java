package net.stln.magitech.magic.charge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.network.SyncManaPayload;

import java.util.Map;

public class ChargeUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player) {
            Charge charge = ChargeData.getCurrentCharge(player);
            if (charge != null) {
                charge.setCharge(charge.getCharge() + 1);
                if (charge.getCharge() > charge.getMaxCharge()) {
                    if (charge.getSpell().releaseOnCharged()){
                        player.releaseUsingItem();
                    }
                    ChargeData.removeCharge(player);
                } else {
                    ChargeData.setCurrentCharge(player, charge);
                }
            }
        }
    }
}
