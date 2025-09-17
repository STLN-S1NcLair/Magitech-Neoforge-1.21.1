package net.stln.magitech.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellInit;
import net.stln.magitech.util.ComponentHelper;

import java.util.Objects;

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
            ItemStack stack = new ItemStack(ItemInit.GLISTENING_LEXICON.get());
            Spell enercrux = SpellInit.ENERCRUX.get();
            MagitechRegistries.SPELL.stream().filter(spell -> !Objects.equals(spell, enercrux)).findAny().ifPresent(spell -> {
                ComponentHelper.updateSpells(stack, spellComponent -> new SpellComponent(enercrux, spell));;
                player.getInventory().add(stack);

                persisted.putBoolean("hasReceivedInitialItems", true);
            });
        }
    }
}
