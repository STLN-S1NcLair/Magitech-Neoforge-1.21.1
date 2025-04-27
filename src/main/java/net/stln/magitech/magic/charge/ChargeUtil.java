package net.stln.magitech.magic.charge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ChargeUtil {

    public static void tick(Entity entity) {
        if (entity instanceof Player player) {
            Charge charge = ChargeData.getCurrentCharge(player);
            if (charge != null) {
                charge.setCharge(charge.getCharge() + 1);
                if (charge.getCharge() > charge.getMaxCharge()) {
                    if (charge.getSpell().releaseOnCharged()) {
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
