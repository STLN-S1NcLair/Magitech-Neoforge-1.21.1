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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BlowParticleEffect;
import net.stln.magitech.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.SpellShape;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Hydraerun extends Spell {

    public Hydraerun() {
        this.baseEffectStrength = 1.0F;
    }

    protected static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(1, Ease.OUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_shoot")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public Element getElement() {
        return Element.FLOW;
    }

    public SpellShape getSpellShape() {
        return SpellShape.UTILITY;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 60.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 9.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 450;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 10, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                Vec3 userPos = user.position();
                level.addParticle(new SquareFieldParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, 1, 0, 15, 1.0F), user.getX(), user.getY() + 0.1, user.getZ(), 0, 0, 0);
                for (int i = 0; i < 50; i++) {
                    level.addParticle(new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 30), 0.87F),
                            userPos.x + Mth.nextDouble(user.getRandom(), -1, 1), userPos.y + user.getBbHeight() * 0.5 + Mth.nextDouble(user.getRandom(), -1, 1), userPos.z + Mth.nextDouble(user.getRandom(), -1, 1),
                            (user.getRandom().nextFloat() - 0.5) / 2, (user.getRandom().nextFloat() - 0.5) / 2 + 0.1, (user.getRandom().nextFloat() - 0.5) / 2);
                    level.addParticle(new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, user.getRandom().nextInt(5, 7), (float) ((user.getRandom().nextFloat() - 0.5) / 10), 15, 0.8F),
                            userPos.x + Mth.nextDouble(user.getRandom(), -1, 1), userPos.y + user.getBbHeight() * 0.5 + Mth.nextDouble(user.getRandom(), -1, 1), userPos.z + Mth.nextDouble(user.getRandom(), -1, 1),
                            (user.getRandom().nextFloat() - 0.5) / 2, (user.getRandom().nextFloat() - 0.5) / 2 + 0.1, (user.getRandom().nextFloat() - 0.5) / 2);
                }
                EffectUtil.entityEffect(level, () -> new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 0.75F, user.getRandom().nextInt(5, 7), (float) ((user.getRandom().nextFloat() - 0.5) / 10), 15, 1.0F),
                        () -> new Vec3(user.getRandom().nextFloat() / 8, user.getRandom().nextFloat() / 8, user.getRandom().nextFloat() / 8), user, 30);
                EffectUtil.entityEffect(level, () -> new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 0, 0, level.random.nextInt(10, 30), 0.87F),
                        () -> new Vec3(user.getRandom().nextFloat() / 8, user.getRandom().nextFloat() / 8, user.getRandom().nextFloat() / 8), user, 30);

                level.playSound(user, userPos.x, userPos.y, userPos.z, SoundInit.HYDRAERUN.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));

                if (level.isClientSide) {
                    playShootAnimation(user);
                }

                if (!level.isClientSide) {
                    user.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, (int) (getDamage(user, this.getRequiredMana(level, user, stack), this.baseEffectStrength, this.getElement()) * 2400), 0));
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
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.OUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_chant")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
