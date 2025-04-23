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
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Ocsilbeam extends Spell {

    public Element getElement() {
        return Element.TREMOR;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 15.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 4.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getTickCost() {
        return new HashMap<>();
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        ChargeData.setCurrentCharge(user, new Charge(10, this, Element.TREMOR));
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            Magitech.LOGGER.info(String.valueOf(ChargeData.getCurrentCharge(user) == null));
            if (ChargeData.getCurrentCharge(user) == null) {
                Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
                Vec3 hitPos = EntityUtil.raycast(user, 24);
                Entity target = EntityUtil.raycastEntity(user, 24);
                Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
                EffectUtil.lineEffect(level, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
                level.addParticle(new BeamParticleEffect(new Vector3f(0.0F, 0.7F, 0.7F), new Vector3f(0.0F, 1.0F, 1.0F), hitPos.toVector3f(), 1.8F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
                for (int i = 0; i < 20; i++) {
                    level.addParticle(new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                            hitPos.x + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.y + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.z + (user.getRandom().nextFloat() - 0.5) / 3, 0, 0, 0);
                }
                level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.SONICBOOM.get(), SoundSource.PLAYERS, 1.0F, 0.5F + (user.getRandom().nextFloat() * 0.5F));


                if (target instanceof LivingEntity livingTarget) {

                    ResourceKey<DamageType> damageType = DamageTypeInit.TREMOR_DAMAGE;
                    float damage = 8.0F;

                    DamageSource elementalDamageSource = stack.has(DataComponents.CUSTOM_NAME) ? user.damageSources().source(damageType, user) : user.damageSources().source(damageType);
                    float targetHealth = livingTarget.getHealth();
                    if (livingTarget instanceof Player player) {
                        player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingTarget.getHealth()) * 10));
                    }
                    if (target.isAttackable()) {
                        livingTarget.setLastHurtByMob(livingTarget);
                        damage *= EntityElementRegister.getElementAffinity(target, Element.GLACE).getMultiplier();
                        target.hurt(elementalDamageSource, damage);
                    }
                }

                if (level.isClientSide) {
                    playShootAnimation(user);
                }
            } else {
                ChargeData.removeCharge(user);
            }
        }
    }

    private static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_beam")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    @Override
    public boolean canHoldUsing() {
        return true;
    }

    @Override
    public boolean stopAnimOnRelease() {
        return false;
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null && !playerAnimationData.isActive()) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(4, Ease.INSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_charge_beam")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
