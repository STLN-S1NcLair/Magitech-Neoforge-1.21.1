package net.stln.magitech.magic.spell.ember;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.mobeffect.MobEffectInit;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.*;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blazewend extends Spell {

    public Blazewend() {
        this.baseDamage = 8;
        this.baseEffectStrength = 1.0F;
    }

    public Element getElement() {
        return Element.EMBER;
    }

    public SpellShape getSpellShape() {
        return SpellShape.DASH;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 70.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 10.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 100;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        ItemStack stack = user.getItemInHand(hand);
        float strength = this.getDamage(user, getCost(level, user, stack), this.baseEffectStrength, this.getElement());
        int duration = 10;
        Vec3 front = Vec3.directionFromRotation(user.getRotationVector());
        user.addDeltaMovement(front.scale(strength * 2));
        for (int i = 0; i < duration; i++) {
            int finalI = i;
            TickScheduler.schedule(i, () -> {
                Vec3 back = Vec3.directionFromRotation(user.getRotationVector()).scale(-1);
                Vec3 bodyPos = user.position().add(0, user.getBbHeight() * 0.7, 0);
                Vec3 offset = bodyPos.add(back.scale(1));
                Vec3 center = user.getEyePosition().add(back);
                Vec3 center2 = center.add(back.scale(2));
                List<Entity> nearbyEntities = EntityUtil.getEntitiesInBox(level, user, user.position(), new Vec3(3, 3, 3));
                Vec3 newFront = Vec3.directionFromRotation(user.getRotationVector());
                user.addDeltaMovement(newFront.scale(strength / 3));
                level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.BLAZEWEND.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
                for (int j = 0; j < 10; j++) {
                    level.addParticle(new FlameParticleEffect(new Vector3f(1), new Vector3f(1),
                                    5F, 1, 0.3F), offset.x + (user.getRandom().nextFloat() - 0.5) / 4, offset.y + (user.getRandom().nextFloat() - 0.5) / 4, offset.z + (user.getRandom().nextFloat() - 0.5) / 4,
                            back.x * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2);
                }
                for (Entity entity : nearbyEntities) {
                    EffectUtil.entityEffect(level, new FlameParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), entity, 30);
                    if (entity instanceof LivingEntity && entity.isAlive()) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, entity);
                        entity.setRemainingFireTicks(Math.min(200, entity.getRemainingFireTicks() + 100));
                    }
                }
                if (finalI + 1 == duration) {
                    user.setDeltaMovement(user.getDeltaMovement().scale(0.3));
                }
                user.fallDistance = 0;
            }, level.isClientSide);
        }
        addCooldown(level, user, stack);
    }

    @Override
    public boolean canHoldUsing() {
        return false;
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(1, Ease.OUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_blink")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
