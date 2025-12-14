package net.stln.magitech.magic.spell.phantom;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.magicentity.frosblast.FrosblastEntity;
import net.stln.magitech.entity.magicentity.illusflare.IllusflareEntity;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.SpellShape;

import java.util.HashMap;
import java.util.Map;

public class Illusflare extends Spell {

    public Illusflare() {
        baseDamage = 7.0F;
        baseSpeed = 1.2;
    }

    private static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(1, Ease.INSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("swing_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public Element getElement() {
        return Element.PHANTOM;
    }

    public SpellShape getSpellShape() {
        return SpellShape.BOMB;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 70.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 7.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 200;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 20, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public boolean isActiveUse(Level level, Player user, InteractionHand hand, boolean isHost) {
        return CooldownData.getCurrentCooldown(user, this) == null;
    }

    @Override
    public boolean canHoldUsing() {
        return true;
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.VOLKARIN_SHOOT.get(), SoundSource.PLAYERS);
                if (!level.isClientSide && !isHost) {
                    IllusflareEntity bullet = new IllusflareEntity(level, user, stack, getDamage(user, this.getCost(level, user, stack), baseDamage, this.getElement()));
                    Vec3 velocity = Vec3.directionFromRotation(user.getRotationVector());
                    velocity = velocity.normalize().scale(getProjectileSpeed(user, baseSpeed));
                    bullet.setDeltaMovement(velocity);
                    bullet.setPos(user.getX(), user.getEyeY() - 0.3, user.getZ());
                    level.addFreshEntity(bullet);
                }

                if (level.isClientSide) {
                    playShootAnimation(user);
                }
                addCooldown(level, user, stack);
            } else {
                ChargeData.removeCharge(user);
            }
        }
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("charge_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
