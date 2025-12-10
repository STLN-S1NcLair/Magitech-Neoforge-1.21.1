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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.SpellShape;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Glistelda extends Spell {

    public Glistelda() {
        this.baseDamage = 8;
        this.baseDuration = 15;
    }

    public Element getElement() {
        return Element.GLACE;
    }

    public SpellShape getSpellShape() {
        return SpellShape.DASH;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 60.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 6.0);
        cost.put(ManaUtil.ManaType.FLUXIA, 2.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 80;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        ItemStack stack = user.getItemInHand(hand);
        int duration = (int) this.getDamage(user, new HashMap<>(), this.baseDuration, this.getElement());
        if (!level.isClientSide) {
            user.addEffect(new MobEffectInstance(MobEffectInit.LEAP_STEP, (int) duration, 6, false, false, true));
        }
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.GLISTELDA.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            TickScheduler.schedule(i, () -> {
                List<Entity> nearbyEntities = EntityUtil.getEntitiesInBox(level, user, user.position(), new Vec3(3, 3, 3));
                entities.addAll(nearbyEntities);
                EffectUtil.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F), user, 4);
                EffectUtil.entityEffect(level, new SquareParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, 1, 0, 15, 1.0F), user, 10);
                for (Entity entity : nearbyEntities) {
                    EffectUtil.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F), entity, 1);
                    level.addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, user.getRandom().nextInt(3, 6), 0, 15, 1.0F), entity.getX(), entity.getY() + 0.1, entity.getZ(), 0, 0, 0);
                }
                level.addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, user.getRandom().nextInt(3, 6), 0, 15, 1.0F), user.getX(), user.getY() + 0.1, user.getZ(), 0, 0, 0);
            }, level.isClientSide);
        }
        TickScheduler.schedule(duration, () -> {
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingTarget && entity.isAlive()) {
                    this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, entity);
                    livingTarget.setTicksFrozen(Math.min(livingTarget.getTicksFrozen() + 170, 300));
                    level.playSound(user, entity.getX(), entity.getY(), entity.getZ(), SoundInit.GLISTELDA_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
                }
                for (int i = 0; i < 60; i++) {
                    float rotSpeed = user.getRandom().nextFloat() / 5 - 0.1F;

                    Vec3 offset = new Vec3(3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5), 3 * (user.getRandom().nextFloat() - 0.5));
                    Vec3 randomBody = entity.position().add(0, entity.getBbHeight() / 2, 0).add(offset);

                    level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, rotSpeed, level.random.nextInt(50, 60), 0.99F),
                            randomBody.x, randomBody.y, randomBody.z, offset.x / 10, offset.y / 10, offset.z / 10);
                }
            }
        }, level.isClientSide);
        addCooldown(level, user, stack);
    }

    @Override
    public boolean canHoldUsing() {
        return false;
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(1, Ease.OUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_blink")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
