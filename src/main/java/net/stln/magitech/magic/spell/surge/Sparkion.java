package net.stln.magitech.magic.spell.surge;

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
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Sparkion extends Spell {

    public Element getElement() {
        return Element.SURGE;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 25.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 2.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 1.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseTickCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 1.5);
        cost.put(ManaUtil.ManaType.LUMINIS, 0.8);
        cost.put(ManaUtil.ManaType.FLUXIA, 0.3);
        return cost;
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
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 30;
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
        super.usingTick(level, livingEntity, stack, usingTick);
        Vec3 forward = Vec3.directionFromRotation(livingEntity.getRotationVector());
        Vec3 bodyPos = livingEntity.position().add(0, livingEntity.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(forward.scale(0.5));
        Vec3 center = livingEntity.getEyePosition().add(forward);
        Vec3 center2 = center.add(forward.scale(2));
        Set<Entity> attackList = new HashSet<>();
        Set<Entity> attackListLast = new HashSet<>();
        attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center, new Vec3(3.0, 3.0, 3.0)));
        attackList.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, center2, new Vec3(4.0, 4.0, 4.0)));
        Vec3 startPos = offset;
        for (Entity entity : attackList) {
            attackListLast.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, entity.position(), new Vec3(4.0, 4.0, 4.0)));
        }
        attackList.clear();
        attackList.addAll(attackListLast);
        attackListLast.clear();
        for (Entity entity : attackList) {
            attackListLast.addAll(EntityUtil.getEntitiesInBox(level, livingEntity, entity.position(), new Vec3(3.0, 3.0, 3.0)));
        }
        boolean hitEntity = false;
        for (Entity entity : attackListLast) {
            Vec3 targetBodyPos = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
            if (startPos.distanceTo(targetBodyPos) > offset.distanceTo(targetBodyPos)) {
                startPos = offset;
            }
            if (level.clip(new ClipContext(targetBodyPos, offset, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK) {
                continue;
            }
            hitEntity = true;
            if (usingTick % 2 == 0 || startPos == offset) {
                level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), targetBodyPos.toVector3f(), 2F, 3, 0), startPos.x, startPos.y, startPos.z,
                        0, 0, 0);
            }
            startPos = targetBodyPos;
        }
        if (!hitEntity) {
            level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1),
                            center2.add(new Vec3(livingEntity.getRandom().nextFloat() - 0.5, livingEntity.getRandom().nextFloat() - 0.5, livingEntity.getRandom().nextFloat() - 0.5).scale(3)).toVector3f(),
                            2F, 3, 0), offset.x, offset.y, offset.z,
                    0, 0, 0);
        }
        if (livingEntity instanceof Player player) {
            if (usingTick % 5 == 0) {
                level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundInit.ZAP.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
        }
        for (Entity target : attackListLast) {
            if (livingEntity instanceof Player user) {
                this.applyDamage(2.0F, this.getTickCost(level, user, stack), this.getElement(), stack, user, target);
            }
        }
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player player) {
            addCooldown(level, player, stack);
        }
    }
}
