package net.stln.magitech.magic.spell.flow;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BlowParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.SpellShape;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Syllaeze extends Spell {

    public Syllaeze() {
        baseEffectStrength = 1;
    }

    public Element getElement() {
        return Element.FLOW;
    }

    public SpellShape getSpellShape() {
        return SpellShape.DASH;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 5.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 5.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 0.8);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseTickCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 2.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 0.3);
        return cost;
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.INSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_flight")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    @Override
    public boolean canHoldUsing() {
        return true;
    }

    @Override
    public boolean releaseOnCharged() {
        return false;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 80;
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
        super.usingTick(level, livingEntity, stack, usingTick);
        if (livingEntity instanceof Player player) {
            Vec3 back = Vec3.directionFromRotation(livingEntity.getRotationVector()).scale(-1);
            Vec3 bodyPos = livingEntity.position().add(0, livingEntity.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(back.scale(1));
            Vec3 center = livingEntity.getEyePosition().add(back);
            Vec3 center2 = center.add(back.scale(2));
            Set<Entity> attackList = new HashSet<>();
            attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center, new Vec3(3.0, 3.0, 3.0)));
            attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center2, new Vec3(4.0, 4.0, 4.0)));
            for (int i = 0; i < 5; i++) {
                level.addParticle(new BlowParticleEffect(new Vector3f(1), new Vector3f(1),
                                5F, 1, 0.3F), offset.x + (livingEntity.getRandom().nextFloat() - 0.5) / 4, offset.y + (livingEntity.getRandom().nextFloat() - 0.5) / 4, offset.z + (livingEntity.getRandom().nextFloat() - 0.5) / 4,
                        back.x * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (livingEntity.getRandom().nextFloat() - 0.5) / 2);
            }
            if (usingTick % 5 == 0) {
                level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundInit.SYLLAEZE.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
            livingEntity.fallDistance = 0;
            livingEntity.addDeltaMovement(Vec3.directionFromRotation(livingEntity.getRotationVector()).scale(this.getDamage(player, new HashMap<>(), this.baseEffectStrength, this.getElement()) / 10));
        }
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            addCooldown(level, user, stack);
        }
    }
}
