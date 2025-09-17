package net.stln.magitech.magic.spell.tremor;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.Element;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.SpellShape;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Sonistorm extends Spell {

    public Sonistorm() {
        tickBaseDamage = 5.0F;
    }

    public Element getElement() {
        return Element.TREMOR;
    }

    public SpellShape getSpellShape() {
        return SpellShape.SPRAY;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 35.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 25.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 3.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseTickCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 2.5);
        cost.put(ManaUtil.ManaType.NOCTIS, 0.4);
        return cost;
    }

    @Override
    public boolean needsTickCost(Level level, Player user, ItemStack stack) {
        return ChargeData.getCurrentCharge(user) == null;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        addCharge(user, 10, this.getElement());
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.INSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_spray")))
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
        return 30;
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
        super.usingTick(level, livingEntity, stack, usingTick);
        if (livingEntity instanceof Player player && ChargeData.getCurrentCharge(player) == null) {
            Vec3 forward = Vec3.directionFromRotation(livingEntity.getRotationVector());
            Vec3 bodyPos = livingEntity.position().add(0, livingEntity.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(forward.scale(1));
            Vec3 center = livingEntity.getEyePosition().add(forward);
            Vec3 center2 = center.add(forward.scale(2));
            Set<Entity> attackList = new HashSet<>();
            attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center, new Vec3(3.0, 3.0, 3.0)));
            attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center2, new Vec3(4.0, 4.0, 4.0)));
            for (int i = 0; i < 5; i++) {
                level.addParticle(new WaveParticleEffect(new Vector3f(1), new Vector3f(1),
                                5F, 1, 0), offset.x + (livingEntity.getRandom().nextFloat() - 0.5) / 4, offset.y + (livingEntity.getRandom().nextFloat() - 0.5) / 4, offset.z + (livingEntity.getRandom().nextFloat() - 0.5) / 4,
                        forward.x * 0.5 + (livingEntity.getRandom().nextFloat() - 0.5) / 2, forward.y * 0.5 + (livingEntity.getRandom().nextFloat() - 0.5) / 2, forward.z * 0.5 + (livingEntity.getRandom().nextFloat() - 0.5) / 2);
            }
            if (usingTick % 5 == 0) {
                level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundInit.SONISTORM.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
            for (Entity target : attackList) {
                if (target.isAttackable()) {
                    Vec3 targetBodyPos = target.position().add(0, target.getBbHeight() * 0.7, 0);
                    if (level.clip(new ClipContext(targetBodyPos, offset, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK) {
                        continue;
                    }
                    if (target instanceof LivingEntity livingTarget) {
                        livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3));
                        livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
                    }
                    if (livingEntity instanceof Player user) {
                        this.applyDamage(tickBaseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                    }
                }
            }
        }
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player player) {
            if (ChargeData.getCurrentCharge(player) == null) {
                addCooldown(level, player, stack);
            }
            ChargeData.removeCharge(player);
        }
    }
}
