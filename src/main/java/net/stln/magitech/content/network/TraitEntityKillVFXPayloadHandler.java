package net.stln.magitech.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.effect.visual.preset.BlockVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.PowerupParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.tool.trait.Trait;
import org.joml.Vector3f;

public class TraitEntityKillVFXPayloadHandler {

    public static void handleDataOnMainS2C(final TraitEntityKillVFXPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        if (player != null) {
            Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            Vector3f pos = payload.pos();
            if (item instanceof SynthesisedToolItem) {
                Trait trait = payload.material().trait();
                BlockVFX.traitBreak(level, trait, new Vec3(pos), 10);
            }
        }
    }
}
