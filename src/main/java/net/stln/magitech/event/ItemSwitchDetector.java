package net.stln.magitech.event;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ItemSwitchDetector {

    private static final Map<UUID, Integer> lastSlotMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        UUID id = player.getUUID();

        int currentSlot = player.getInventory().selected;

        int lastSlot = lastSlotMap.getOrDefault(id, -1);
        if (lastSlot != currentSlot) {
            // ここでアイテム切り替えを検知！
            ItemStack newItem = player.getMainHandItem();
            if (lastSlot >= 0 && player.getInventory().getItem(lastSlot).getItem() instanceof SpellCasterItem) {
                if (player.level().isClientSide) {
                    stopAnim(player);
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void stopAnim(Player player) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null && playerAnimationData.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimationPlayer) {

            keyframeAnimationPlayer.stop();
        }
    }
}
