package net.stln.magitech.magic.mana;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EntityManaTickEvent {

    @SubscribeEvent
    public static void manaTick(EntityTickEvent.Pre event) {
        ManaUtil.tick(event.getEntity());
    }

    @SubscribeEvent
    public static void levelJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            ManaData.setCurrentMana(player, ManaUtil.ManaType.MANA, player.getAttributeValue(AttributeInit.MAX_MANA));
            ManaData.setCurrentMana(player, ManaUtil.ManaType.NOCTIS, player.getAttributeValue(AttributeInit.MAX_NOCTIS));
            ManaData.setCurrentMana(player, ManaUtil.ManaType.LUMINIS, player.getAttributeValue(AttributeInit.MAX_LUMINIS));
            ManaData.setCurrentMana(player, ManaUtil.ManaType.FLUXIA, player.getAttributeValue(AttributeInit.MAX_FLUXIA));
        }
    }
}
