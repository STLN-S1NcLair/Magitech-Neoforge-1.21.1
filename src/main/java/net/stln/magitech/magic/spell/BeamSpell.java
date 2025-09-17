package net.stln.magitech.magic.spell;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.util.EntityUtil;

import java.util.HashMap;
import java.util.List;

public class BeamSpell extends Spell {

    protected double beamradius = 0.2;

    protected static void playShootAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand_beam")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        callSuperFinishUsing(stack, level, livingEntity, timeCharged, isHost);
        if (livingEntity instanceof Player user) {
            if (ChargeData.getCurrentCharge(user) == null && timeCharged > 1 && ManaUtil.useManaServerOnly(user, this.getRequiredMana(level, user, stack))) {
                Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
                Vec3 hitPos = EntityUtil.raycastBeam(user, this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), beamradius);
                BlockHitResult blockHitResult = EntityUtil.raycastBeamBlockHit(user, this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), beamradius);
                Entity target = EntityUtil.raycastBeamEntity(user, this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), beamradius);
                Vec3 start = user.position().add(0, user.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
                addVisualEffect(level, user, start, hitPos);
                playBeamSound(level, user);


                if (!level.isClientSide) {
                    if (target != null) {
                        this.applyDamage(baseDamage, this.getRequiredMana(level, user, stack), this.getElement(), stack, user, target);
                        if (target instanceof LivingEntity livingTarget) {
                            applyEffectToLivingTarget(level, user, livingTarget);
                        }
                    }
                    List<Entity> entities = EntityUtil.getEntitiesInBox(level, user, hitPos, new Vec3(1, 1, 1));
                    for (Entity entity : entities) {
                        if (entity instanceof ItemEntity) {
                            applyEffectToItem(level, user, entity);
                        }
                    }
                }
                if (blockHitResult != null && blockHitResult.getType() == BlockHitResult.Type.BLOCK) {
                    BlockPos targetBlock = blockHitResult.getBlockPos();
                    applyEffectToBlock(level, user, stack, targetBlock);
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

    protected void callSuperFinishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
    }

    protected void applyEffectToLivingTarget(Level level, Player user, LivingEntity target) {

    }

    protected void applyEffectToBlock(Level level, Player user, ItemStack stack, BlockPos target) {

    }

    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
    }

    protected void playBeamSound(Level level, Player user) {
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
