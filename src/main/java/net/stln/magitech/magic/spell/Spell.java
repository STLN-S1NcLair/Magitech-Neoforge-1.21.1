package net.stln.magitech.magic.spell;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.network.ReleaseUsingSpellPayload;
import net.stln.magitech.network.UseSpellPayload;

import java.util.HashMap;
import java.util.Map;

public abstract class Spell {

    public Element getElement() {
        return Element.NONE;
    }

    public Map<ManaUtil.ManaType, Double> getCost() {
        return new HashMap<>();
    }

    public Map<ManaUtil.ManaType, Double> getTickCost() {
        return new HashMap<>();
    }

    public boolean needsUseCost(Level level, Player user, ItemStack stack) {
        return true;
    }

    public boolean needsTickCost(Level level, Player user, ItemStack stack) {
        return true;
    }

    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        if (level.isClientSide) {
            playAnimation(user);
            if (isHost) {
                PacketDistributor.sendToServer(new UseSpellPayload(hand == InteractionHand.MAIN_HAND, user.getUUID().toString()));
            }
        }
        if (canHoldUsing()) {
            user.startUsingItem(hand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "swing_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public boolean canHoldUsing() {
        return false;
    }

    public boolean stopAnimOnRelease() {
        return true;
    }

    public boolean releaseOnCharged() {
        return true;
    }

    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
    };

    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        if (livingEntity instanceof Player player) {
            Charge charge = ChargeData.getCurrentCharge(player);
            if (charge != null) {
                if (charge.getCharge() >= charge.getMaxCharge() || timeCharged >= charge.getMaxCharge()) {
                    ChargeData.removeCharge(player);
                }
            }
        }
        if (level.isClientSide) {
            if (isHost) {
            PacketDistributor.sendToServer(new ReleaseUsingSpellPayload(stack, timeCharged, livingEntity.getUUID().toString()));
            }
            if (canHoldUsing() && stopAnimOnRelease() && livingEntity instanceof Player player) {
                stopAnim(player);
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
