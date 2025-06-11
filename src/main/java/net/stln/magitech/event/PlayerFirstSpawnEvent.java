package net.stln.magitech.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.magic.spell.mana.Enercrux;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class PlayerFirstSpawnEvent {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        CompoundTag data = player.getPersistentData();
        CompoundTag persisted;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            persisted = new CompoundTag();
            data.put(Player.PERSISTED_NBT_TAG, persisted);
        } else {
            persisted = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        if (!persisted.getBoolean("hasReceivedInitialItems")) {
            ItemStack glisteningLexicon = new ItemStack(ItemInit.GLISTENING_LEXICON.get());
            List<Spell> list = new ArrayList<>(SpellRegister.getRegister().values());
            list.remove(new Enercrux());
            glisteningLexicon.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(List.of(new Enercrux(), list.get(player.getRandom().nextInt(0, list.size()))), 0));
            player.getInventory().add(glisteningLexicon);

            persisted.putBoolean("hasReceivedInitialItems", true);
        }
    }
}
