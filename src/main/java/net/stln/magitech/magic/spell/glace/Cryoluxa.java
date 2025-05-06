package net.stln.magitech.magic.spell.glace;

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

public class Cryoluxa extends Spell {

    public Cryoluxa() {
        baseDamage = 4.0F;
    }

    private static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_beam")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public Element getElement() {
        return Element.GLACE;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 40.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 4.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 3.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 120;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 4, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
                Vec3 hitPos = EntityUtil.raycast(user, 63);
                Entity target = EntityUtil.raycastEntity(user, 63);
                Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
                EffectUtil.lineEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
                level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), hitPos.toVector3f(), 0.7F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
                for (int i = 0; i < 20; i++) {
                    level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                            hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
                }
                level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.FROST_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.6F + (user.getRandom().nextFloat() * 0.6F));


                if (!level.isClientSide) {
                    if (target instanceof LivingEntity livingTarget) {
                        livingTarget.setTicksFrozen(Math.min(livingTarget.getTicksFrozen() + 160, 320));
                    }
                    if (target != null) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                    }
                }
                addCooldown(level, user, stack);

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
    public boolean canHoldUsing() {
        return true;
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
