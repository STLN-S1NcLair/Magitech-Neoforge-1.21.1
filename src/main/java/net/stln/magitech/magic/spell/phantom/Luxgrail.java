package net.stln.magitech.magic.spell.phantom;

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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.SpellShape;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Luxgrail extends Spell {

    public Luxgrail() {
    }

    protected static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(Magitech.id("animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("wand_shoot")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public Element getElement() {
        return Element.PHANTOM;
    }

    public SpellShape getSpellShape() {
        return SpellShape.UTILITY;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 70.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 8.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 4000;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        int delay = (int) Math.round(80 / user.getAttributeValue(AttributeInit.CASTING_SPEED)) - 80;
        level.playSound(user, user.position().x, user.position().y, user.position().z, SoundInit.LUXGRAIL_CHARGE.get(), SoundSource.PLAYERS, 1.0F, (float) 80 / (Math.round(80 / user.getAttributeValue(AttributeInit.CASTING_SPEED)) - Math.max(delay, 0)));
        addCharge(user, 80, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                Vec3 userPos = user.position();
                Vec3 above = userPos.add(0, 10, 0);
                Vector3f fromCol = new Vector3f(1.0F, 1.0F, 0.7F);
                Vector3f toCol = new Vector3f(1.0F, 1.0F, 0.5F);


                EffectUtil.lineEffect(level, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 3.0F, 1, 0, level.random.nextInt(10, 40), 0.85F), userPos, above, 4, false);
                level.addParticle(new BeamParticleEffect(fromCol, toCol, above.toVector3f(), 5F, 1, 1, 5, 1), userPos.x, userPos.y - 0.5, userPos.z, 0, 0, 0);
                for (int i = 0; i < 20; i++) {
                    level.addParticle(new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 3.0F, 1, 0, level.random.nextInt(10, 40), 0.85F),
                            userPos.x, userPos.y, userPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
                }

                level.playSound(user, userPos.x, userPos.y, userPos.z, SoundInit.LUXGRAIL.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
                TickScheduler.schedule(2, () -> {
                    Vec3 newPos = user.position();
                    Vec3 newAbove = newPos.add(0, 10, 0);
                    EffectUtil.lineEffect(level, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 3.0F, 1, 0, level.random.nextInt(10, 40), 0.85F), newPos, newAbove, 4, false);
                    level.addParticle(new BeamParticleEffect(fromCol, toCol, newAbove.toVector3f(), 5F, 1, 1, 5, 1), newPos.x, newPos.y - 0.5, newPos.z, 0, 0, 0);
                    for (int i = 0; i < 20; i++) {
                        level.addParticle(new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 3.0F, 1, 0, level.random.nextInt(10, 40), 0.85F),
                                newPos.x, newPos.y, newPos.z, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3, (user.getRandom().nextFloat() - 0.5) / 3);
                    }

                    level.playSound(user, newPos.x, newPos.y, newPos.z, SoundInit.LUXGRAIL.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (user.getRandom().nextFloat() * 0.6F));
                    livingEntity.fallDistance = 0;
                }, level.isClientSide);

                if (level.isClientSide) {
                    playShootAnimation(user);
                }

                BlockPos targetPos = user.blockPosition();
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos.getX(), targetPos.getZ());
                user.teleportTo(targetPos.getX() + 0.5, y, targetPos.getZ() + 0.5);

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
