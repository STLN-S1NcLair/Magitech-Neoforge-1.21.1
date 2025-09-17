package net.stln.magitech.magic.spell.magic;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.RuneParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.item.tool.element.Element;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.SpellShape;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Envistra extends Spell {

    public Envistra() {
        this.baseDamage = 6;
        this.baseMaxRange = 10;
    }

    protected static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_blink")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public Element getElement() {
        return Element.MAGIC;
    }

    public SpellShape getSpellShape() {
        return SpellShape.DASH;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 50.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 6.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 40;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 5, this.getElement());
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
                Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
                Vec3 hitPos = EntityUtil.raycast(user, getDamage(user, this.getRequiredMana(level, user, stack), (float) baseMaxRange, this.getElement())).subtract(forward.scale(livingEntity.getBbWidth() / 2 + 0.1));
                Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));

                EffectUtil.lineEffect(level, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
                level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 0.0F, 0.7F), new Vector3f(1.0F, 0.0F, 0.3F), hitPos.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
                for (int i = 0; i < 20; i++) {
                    level.addParticle(new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                            hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
                }
                Vec3 back = Vec3.directionFromRotation(user.getRotationVector()).scale(-1);
                Vec3 bodyPos = user.position().add(0, user.getBbHeight() * 0.7, 0);
                Vec3 offset = bodyPos.add(back.scale(1));
                Vec3 center = user.getEyePosition().add(back);
                Vec3 center2 = center.add(back.scale(2));
                List<Entity> nearbyEntities = new ArrayList<>();
                for (int i = 0; i < hitPos.distanceTo(user.position()); i++) {
                    nearbyEntities.addAll(EntityUtil.getEntitiesInBox(level, user, user.position().lerp(hitPos, i / hitPos.distanceTo(user.position())), new Vec3(3, 3, 3)));
                }
                for (int j = 0; j < 10; j++) {
                    level.addParticle(new RuneParticleEffect(new Vector3f(1), new Vector3f(1),
                                    5F, 1, 0.3F), offset.x + (user.getRandom().nextFloat() - 0.5) / 4, offset.y + (user.getRandom().nextFloat() - 0.5) / 4, offset.z + (user.getRandom().nextFloat() - 0.5) / 4,
                            back.x * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (user.getRandom().nextFloat() - 0.5) / 2);
                }
                for (Entity entity : nearbyEntities) {
                    EffectUtil.entityEffect(level, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), entity, 30);
                    if (entity instanceof LivingEntity && entity.isAlive()) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, entity);
                    }
                }
                for (Entity entity : EntityUtil.getEntitiesInBox(level, user, hitPos, new Vec3(6, 6, 6))) {
                    EffectUtil.entityEffect(level, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), entity, 30);
                    if (entity instanceof LivingEntity && entity.isAlive()) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, entity);
                    }
                }
                livingEntity.setPos(hitPos);
                for (int i = 0; i < 60; i++) {
                    Vec3 off = new Vec3(3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5));
                    Vec3 randomBody = user.position().add(0, user.getBbHeight() / 2, 0).add(off);
                    level.addParticle(new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0),
                            randomBody.x, randomBody.y, randomBody.z, off.x / 10, off.y / 10, off.z / 10);
                }
                for (int i = 0; i < 60; i++) {
                    Vec3 off = new Vec3(3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5));
                    Vec3 randomBody = user.position().add(0, user.getBbHeight() / 2, 0).add(off);
                    level.addParticle(new SquareParticleEffect(new Vector3f(1.0F, 0.0F, 0.7F), new Vector3f(1.0F, 0.0F, 0.3F), 1F, 1, 0),
                            randomBody.x, randomBody.y, randomBody.z, off.x / 10, off.y / 10, off.z / 10);
                }
                addCooldown(level, user, stack);

                level.playSound(user, hitPos.x, hitPos.y, hitPos.z, SoundInit.ENVISTRA.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
                livingEntity.fallDistance = 0;

                if (level.isClientSide) {
                    playShootAnimation(user);
                }
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
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(1, Ease.OUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("charge_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
