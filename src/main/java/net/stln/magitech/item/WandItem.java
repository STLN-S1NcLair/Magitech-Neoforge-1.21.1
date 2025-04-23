package net.stln.magitech.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.AdjustableAttackStrengthEntity;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;

public class WandItem extends Item implements LeftClickOverrideItem {

    private int sweepDamage = 6;
    Sparkion stormhaze = new Sparkion();
    Arcaleth arcether = new Arcaleth();

    public WandItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(user).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        if (!threadbound.isEmpty()) {
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            if (spellComponent.selected() < spellComponent.spells().size()) {
                Spell spell = spellComponent.spells().get(spellComponent.selected());
                if (user.isCrouching()) {
                    threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), spellComponent.selected() >= spellComponent.spells().size() - 1 ? 0 : spellComponent.selected() + 1));
                    user.releaseUsingItem();
                    return InteractionResultHolder.pass(itemStack);
                }
                boolean flag = false;
                if (spell.needsUseCost(world, user, itemStack)) {
                    flag = ManaUtil.useManaServerOnly(user, spell.getCost());
                } else {
                    flag = true;
                }
                if (flag) {
                    spell.use(world, user, hand, true);
                } else {
                    user.releaseUsingItem();
                }
            } else {
                Magitech.LOGGER.info("out of range");
                threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), 0));
            }
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
        if (livingEntity instanceof Player user) {
            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(user).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

            if (!threadbound.isEmpty()) {
                SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
                Spell spell = spellComponent.spells().get(spellComponent.selected());
                boolean flag = false;
                if (spell.needsTickCost(level, user, stack)) {
                    flag = ManaUtil.useManaServerOnly(user, spell.getTickCost());
                } else {
                    flag = true;
                }
                if (flag) {
                    spell.usingTick(level, livingEntity, stack, getUseDuration(stack, livingEntity) - remainingUseDuration);
                } else {
                    livingEntity.releaseUsingItem();
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        super.releaseUsing(stack, level, livingEntity, timeCharged);
        if (livingEntity instanceof Player user && level.isClientSide) {
            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(user).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

            if (!threadbound.isEmpty()) {
                SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
                Spell spell = spellComponent.spells().get(spellComponent.selected());

                spell.finishUsing(stack, level, livingEntity, getUseDuration(stack, livingEntity) - timeCharged, true);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResult onLeftClick(Player user, InteractionHand hand, Level world) {
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
        sweepAttack(world, user);
        user.awardStat(Stats.DAMAGE_DEALT, sweepDamage * 10);
        user.swing(hand);
        return InteractionResult.SUCCESS;
    }

    private void sweepAttack(Level world, Player user) {
        Vec3 effectCenter = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 0.5, 2);
        EffectUtil.sweepEffect(user, world, new UnstableSquareParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.5F, 1.0F, 1.0F), 1.0F, 1, 1F), effectCenter, -45.0, 45.0, 100, 2, (user.getRandom().nextFloat() - 0.5) * 45.0, false);

        Vec3 center = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 2, 0.5);
        List<Entity> attackList = EntityUtil.getEntitiesInBox(world, user, center, new Vec3(3.0, 1.0, 3.0));

        float cooldown = ((AdjustableAttackStrengthEntity) user).getLastAttackedTicks();
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                ((AdjustableAttackStrengthEntity) user).setLastAttackedTicks((int) cooldown);
                user.attack(target);
            }
        }

        user.resetAttackStrengthTicker();
    }
}
