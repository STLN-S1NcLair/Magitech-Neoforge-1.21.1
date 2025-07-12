package net.stln.magitech.magic.spell;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.cooldown.Cooldown;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.mana.UsedHandData;
import net.stln.magitech.network.ReleaseUsingSpellPayload;
import net.stln.magitech.network.UseSpellPayload;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.SpellConversionRecipe;
import net.stln.magitech.util.MathUtil;

import java.util.*;

public abstract class Spell {

    public float baseDamage = 0;
    public float baseEffectStrength = 0;
    public float tickBaseDamage = 0;
    public double baseSpeed = 0;
    public double baseMaxRange = 0;

    @OnlyIn(Dist.CLIENT)
    private static void stopAnim(Player player) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null && playerAnimationData.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimationPlayer) {

            keyframeAnimationPlayer.stop();
        }
    }

    public Element getElement() {
        return Element.NONE;
    }

    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        return this.getBaseCost();
    }

    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        return new HashMap<>();
    }

    public Map<ManaUtil.ManaType, Double> getBaseTickCost() {
        return new HashMap<>();
    }

    public Map<ManaUtil.ManaType, Double> getRequiredMana(Level level, Player user, ItemStack stack) {
        Map<ManaUtil.ManaType, Double> map = new HashMap<>(this.getBaseRequiredMana());
        if (map.containsKey(ManaUtil.ManaType.MANA)) {
            map.put(ManaUtil.ManaType.MANA, map.get(ManaUtil.ManaType.MANA) / user.getAttributeValue(AttributeInit.MANA_EFFICIENCY));
        }
        return map;
    }

    public Map<ManaUtil.ManaType, Double> getCost(Level level, Player user, ItemStack stack) {
        Map<ManaUtil.ManaType, Double> map = new HashMap<>(this.getBaseCost());
        if (map.containsKey(ManaUtil.ManaType.MANA)) {
            map.put(ManaUtil.ManaType.MANA, map.get(ManaUtil.ManaType.MANA) / user.getAttributeValue(AttributeInit.MANA_EFFICIENCY));
        }
        return map;
    }

    public Map<ManaUtil.ManaType, Double> getTickCost(Level level, Player user, ItemStack stack) {
        Map<ManaUtil.ManaType, Double> map = new HashMap<>(this.getBaseTickCost());
        if (map.containsKey(ManaUtil.ManaType.MANA)) {
            map.put(ManaUtil.ManaType.MANA, map.get(ManaUtil.ManaType.MANA) / user.getAttributeValue(AttributeInit.MANA_EFFICIENCY));
        }
        return map;
    }

    public boolean needsUseCost(Level level, Player user, ItemStack stack) {
        return true;
    }

    public boolean needsTickCost(Level level, Player user, ItemStack stack) {
        return true;
    }

    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 60;
    }

    public int getModifiedCooldown(Level level, Player user, ItemStack stack) {
        return (int) Math.round(this.getCooldown(level, user, stack) / user.getAttributeValue(AttributeInit.COOLDOWN_SPEED));
    }

    public Element getElement(Level level, Player user, ItemStack stack) {
        return Element.NONE;
    }

    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        if (level.isClientSide) {
            playAnimation(user);
            if (isHost) {
                PacketDistributor.sendToServer(new UseSpellPayload(hand == InteractionHand.MAIN_HAND, user.getUUID().toString()));
            }
        }
        if (canHoldUsing()) {
            user.startUsingItem(hand);
        }
        UsedHandData.setUsedHand(user, (hand == InteractionHand.OFF_HAND) ^ (user.getMainArm() == HumanoidArm.LEFT));
        ItemStack stack = user.getItemInHand(hand);
        if (!level.isClientSide && isHost) {
            stack.hurtAndBreak(1, user, LivingEntity.getSlotForHand(hand));
        }
    }

    public boolean isActiveUse(Level level, Player user, InteractionHand hand, boolean isHost) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "swing_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }

    public boolean canHoldUsing() {
        return false;
    }

    public boolean stopAnimOnRelease() {
        return true;
    }

    public boolean releaseOnCharged() {
        return true;
    }

    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
        if (livingEntity instanceof Player user) {
            InteractionHand hand = user.getItemInHand(InteractionHand.MAIN_HAND) == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            UsedHandData.setUsedHand(user, (hand == InteractionHand.OFF_HAND) ^ (user.getMainArm() == HumanoidArm.LEFT));
        }
    }

    public boolean isActiveUsingTick(Level level, LivingEntity livingEntity, ItemStack stack, int usingTick) {
        return true;
    }

    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        if (livingEntity instanceof Player player) {
            Charge charge = ChargeData.getCurrentCharge(player);
            if (charge != null) {
                if (charge.getCharge() >= charge.getMaxCharge() || timeCharged >= charge.getMaxCharge()) {
                    ChargeData.removeCharge(player);
                }
            }
        }
        if (isHost) {
            if (level.isClientSide) {
                PacketDistributor.sendToServer(new ReleaseUsingSpellPayload(stack, timeCharged, livingEntity.getUUID().toString()));
            }
        }
        if (level.isClientSide) {
            if (canHoldUsing() && livingEntity instanceof Player player) {
                if (stopAnimOnRelease()) {
                    stopAnim(player);
                }
            }
        }
        if (!isHost) {
            livingEntity.stopUsingItem();
        }
    }

    public void addCharge(Player user, int ticks, Element element) {
        ChargeData.setCurrentCharge(user, new Charge(Math.max(Math.round(ticks / user.getAttributeValue(AttributeInit.CASTING_SPEED)), 2), this, element));
    }

    public void addCooldown(Level level, Player user, ItemStack stack) {
        CooldownData.addCurrentCooldown(user, this, new Cooldown(this.getCooldown(level, user, stack) / user.getAttributeValue(AttributeInit.COOLDOWN_SPEED), this.getElement()));
    }

    public float getDamage(Player user, Map<ManaUtil.ManaType, Double> cost, float baseDamage, Element element) {
        baseDamage = ManaUtil.checkStrandDamageMul(user, cost, baseDamage);
        double power = user.getAttributeValue(AttributeInit.SPELL_POWER);
        double elementPower;
        if (element != Element.NONE) {
            DeferredHolder<Attribute, Attribute> elementAttribute = switch (element) {
                case EMBER -> AttributeInit.EMBER_SPELL_POWER;
                case GLACE -> AttributeInit.GLACE_SPELL_POWER;
                case SURGE -> AttributeInit.SURGE_SPELL_POWER;
                case PHANTOM -> AttributeInit.PHANTOM_SPELL_POWER;
                case TREMOR -> AttributeInit.TREMOR_SPELL_POWER;
                case MAGIC -> AttributeInit.MAGIC_SPELL_POWER;
                case FLOW -> AttributeInit.FLOW_SPELL_POWER;
                case HOLLOW -> AttributeInit.HOLLOW_SPELL_POWER;
                default -> throw new IllegalStateException("Unexpected value: " + element);
            };
            elementPower = user.getAttributeValue(elementAttribute);
        } else {
            elementPower = 1.0F;
        }
        return (float) (baseDamage * power + baseDamage * (elementPower - 1));
    }

    public double getProjectileSpeed(Player user, double baseSpeed) {
        double power = user.getAttributeValue(AttributeInit.PROJECTILE_SPEED);
        return baseSpeed * power;
    }

    public void applyDamage(float baseDamage, Map<ManaUtil.ManaType, Double> cost, Element element, ItemStack stack, Player user, Entity target) {
        float damage = this.getDamage(user, cost, baseDamage, element);
        ResourceKey<DamageType> damageType = element.getDamageType();

        DamageSource elementalDamageSource = user.damageSources().source(damageType, user);
        if (target instanceof Player) {
            elementalDamageSource = stack.has(DataComponents.CUSTOM_NAME) ? user.damageSources().source(damageType, user) : user.damageSources().source(damageType);
        }
        if (target.isAttackable()) {
            if (target instanceof LivingEntity livingTarget) {
                float targetHealth = livingTarget.getHealth();
                livingTarget.setLastHurtByMob(user);
                user.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingTarget.getHealth()) * 10));
            }
            damage *= EntityElementRegister.getElementAffinity(target, element).getMultiplier();
            target.hurt(elementalDamageSource, damage);
            user.setLastHurtMob(target);
        }
    }

    public List<Component> getTooltip(Level level, Player user, ItemStack stack) {
        List<Component> list = new ArrayList<>();
        if (this.baseDamage != 0) {
            list.add(Component.translatable("tooltip.magitech.spell.damage").append(": " + MathUtil.round(this.getDamage(user, new HashMap<>(), this.baseDamage, this.getElement()), 2)));
        }
        if (this.tickBaseDamage != 0) {
            list.add(Component.translatable("tooltip.magitech.spell.tick_damage").append(": " + MathUtil.round(this.getDamage(user, new HashMap<>(), this.tickBaseDamage, this.getElement()), 2)));
        }
        if (this.baseSpeed != 0) {
            list.add(Component.translatable("tooltip.magitech.spell.projectile_speed").append(": " + MathUtil.round(this.getProjectileSpeed(user, this.baseSpeed), 2)));
        }
        if (this.baseEffectStrength != 0) {
            list.add(Component.translatable("tooltip.magitech.spell.effect_strength").append(": " + MathUtil.round(this.getDamage(user, new HashMap<>(), (float) this.baseEffectStrength, this.getElement()), 2)));
        }
        if (this.baseMaxRange != 0) {
            list.add(Component.translatable("tooltip.magitech.spell.max_range").append(": " + MathUtil.round(this.getDamage(user, new HashMap<>(), (float) this.baseMaxRange, this.getElement()), 2)));
        }
        list.add(Component.translatable("tooltip.magitech.spell.cooldown").append(": " + MathUtil.round((double) this.getModifiedCooldown(level, user, stack) / 20, 2) + "s"));
        list.add(Component.empty());
        if (!Objects.equals(this.getBaseRequiredMana().get(ManaUtil.ManaType.MANA), this.getBaseCost().get(ManaUtil.ManaType.MANA))) {
            list.add(Component.translatable("tooltip.magitech.spell.required_mana").append(": " + MathUtil.round(this.getRequiredMana(level, user, stack).get(ManaUtil.ManaType.MANA), 2)).withColor(0x40FFF0));
        }
        if (this.needsUseCost(level, user, stack) && this.getBaseCost().containsKey(ManaUtil.ManaType.MANA)) {
            list.add(Component.translatable("tooltip.magitech.spell.use_cost").append(": " + MathUtil.round(this.getCost(level, user, stack).get(ManaUtil.ManaType.MANA), 2)).withColor(0x40FFF0));
        }
        if (this.needsTickCost(level, user, stack) && this.canHoldUsing() && this.getBaseTickCost().containsKey(ManaUtil.ManaType.MANA)) {
            list.add(Component.translatable("tooltip.magitech.spell.tick_cost").append(": " + MathUtil.round(this.getTickCost(level, user, stack).get(ManaUtil.ManaType.MANA), 2) + "/tick").withColor(0x40FFF0));
        }
        return list;
    }

    protected void applyEffectToItem(Level level, Player user, Entity target) {
        if (target instanceof ItemEntity item) {
            Optional<RecipeHolder<SpellConversionRecipe>> recipeHolder = level.getRecipeManager().getRecipeFor(RecipeInit.SPELL_CONVERSION_TYPE.get(), new SingleRecipeInput(item.getItem()), level);
            if (recipeHolder.isPresent()) {
                SpellConversionRecipe recipe = recipeHolder.get().value();
                if (recipe.getSpell().equals(SpellRegister.getId(this))) {
                    ItemStack stack = recipe.assemble(new SingleRecipeInput(item.getItem()), null);
                    int count = item.getItem().getCount() * stack.getCount();
                    while (count > 0) {
                        int spawnCount = Math.min(stack.getMaxStackSize(), count);
                        ItemStack result = stack.copy();
                        result.setCount(spawnCount);
                        ItemEntity newItem = new ItemEntity(level, item.getX(), item.getY(), item.getZ(), result, Mth.nextFloat(item.getRandom(), -0.3F, 0.3F), 0.3, Mth.nextFloat(item.getRandom(), -0.3F, 0.3F));
                        level.addFreshEntity(newItem);
                        count -= spawnCount;
                    }
                    item.discard();
                }
            }
        }
    }
}
