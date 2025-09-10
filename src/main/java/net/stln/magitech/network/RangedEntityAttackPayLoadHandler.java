package net.stln.magitech.network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.entity.mob.WeaverEntity;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.UUID;

public class RangedEntityAttackPayLoadHandler {

    public static void handleDataOnMainS2C(final RangedEntityAttackPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        if (level.getEntity(payload.id()) instanceof RangedAttackMob mob && level.getEntity(payload.targetId()) instanceof LivingEntity target) {
            if (mob instanceof WeaverEntity weaver) {
                weaver.performSpell(level, target, payload.index());
            }
        }
    }
}
