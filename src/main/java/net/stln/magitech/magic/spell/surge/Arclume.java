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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.util.*;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arclume extends Spell {

    public Arclume() {
        this.baseDamage = 7;
        this.baseMaxRange = 10;
    }

    public Element getElement() {
        return Element.SURGE;
    }

    public SpellShape getSpellShape() {
        return SpellShape.DASH;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 55.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 6.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 1.5);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 60;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        ItemStack stack = user.getItemInHand(hand);
        Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
        Vec3 hitPos = EntityUtil.raycast(user, getDamage(user, this.getRequiredMana(level, user, stack), (float) baseMaxRange, this.getElement())).subtract(forward.scale(user.getBbWidth() / 2 + 0.1));
        Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));

        EffectUtil.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0), start, hitPos, 2, false);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0),
                    hitPos.x, hitPos.y, hitPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
        }

        level.playSound(user, hitPos.x, hitPos.y, hitPos.z, SoundInit.ARCLUME.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (user.getRandom().nextFloat() * 0.6F));
        Vec3 oldPos = new Vec3(user.position().toVector3f());
        for (int i = 0; i < hitPos.subtract(oldPos).length() / 2; i++) {
            int finalI = i;
            TickScheduler.schedule(i * 2, () -> {
                addLightning(stack, user, level, oldPos.lerp(hitPos, finalI / hitPos.subtract(oldPos).length() * 2));
            }, level.isClientSide);
        }
        if (isHost) {
            user.setPos(hitPos);
        }
        user.fallDistance = 0;
        addCooldown(level, user, stack);
    }

    private void addLightning(ItemStack stack, Player user, Level level, Vec3 pos) {
        Vec3 surface = EntityUtil.findSurface(level, pos);
        Vec3 lightningTop = surface.add(0, Mth.randomBetween(user.getRandom(), 5, 20), 0);
        List<Entity> entities = EntityUtil.getEntitiesInBox(level, user, surface, new Vec3(2, 2, 2));

        level.playSound(user, surface.x, surface.y, surface.z, SoundInit.ARCLUME.get(), SoundSource.PLAYERS, 1.0F, 0.8F + (user.getRandom().nextFloat() * 0.6F));

        if (!level.isClientSide) {
            for (Entity target : entities) {
                this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
            }
        }

        if (level.isClientSide) {
            level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), lightningTop.toVector3f(), 2F, 3, 0), surface.x, surface.y, surface.z,
                    0, 0, 0);
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.5F, 0.5F, 1.0F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            int particleAmount = 20;

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = user.getRandom().nextInt(2, 4);

                double x = lightningTop.x;
                double y = lightningTop.y;
                double z = lightningTop.z;
                double vx = (user.getRandom().nextFloat() - 0.5) / 10;
                double vy = (user.getRandom().nextFloat() - 0.5) / 10;
                double vz = (user.getRandom().nextFloat() - 0.5) / 10;
                level.addParticle(new SquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = user.getRandom().nextInt(2, 4);

                double x = surface.x + Mth.randomBetween(user.getRandom(), -0.2F, 0.2F);
                double y = surface.y + Mth.randomBetween(user.getRandom(), -0.2F, 0.2F);
                double z = surface.z + Mth.randomBetween(user.getRandom(), -0.2F, 0.2F);
                double vx = (user.getRandom().nextFloat() - 0.5) / 2;
                double vy = (user.getRandom().nextFloat() - 0.5);
                double vz = (user.getRandom().nextFloat() - 0.5) / 2;
                level.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed), x, y, z, vx, vy, vz);
            }
        }
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
