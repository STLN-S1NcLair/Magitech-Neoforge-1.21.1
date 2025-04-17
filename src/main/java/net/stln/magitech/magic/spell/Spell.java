package net.stln.magitech.magic.spell;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.network.UseSpellPayload;

import java.util.HashMap;
import java.util.Map;

public abstract class Spell {

    public Map<ManaUtil.ManaType, Double> getCost() {
        return new HashMap<>();
    }

    public Map<ManaUtil.ManaType, Double> getTickCost() {
        return new HashMap<>();
    }

    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        if (level.isClientSide && isHost) {
            PacketDistributor.sendToServer(new UseSpellPayload(hand == InteractionHand.MAIN_HAND, user.getUUID().toString()));
        }
    }

    public abstract void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration);

    public abstract void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity);
}
