package net.stln.magitech.item.tool;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.EntityElementDictionary;
import net.stln.magitech.entity.AdjustableAttackStrengthEntity;
import net.stln.magitech.item.LeftClickOverrideItem;
import net.stln.magitech.item.comopnent.ComponentInit;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.MathUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class PartToolItem extends TieredItem implements LeftClickOverrideItem {
    ResourceLocation atkId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_damage_modifier");
    ResourceLocation spdId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_speed_modifier");
    ResourceLocation defId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_defense_modifier");
    ResourceLocation rngId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_range_modifier");
    private int sweepDamage = 6;

    public PartToolItem(Properties settings) {
        super(Tiers.WOOD, settings);
    }

    public static ToolStats getSumStats(ItemStack stack) {
        List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
        ToolType toolType = ((PartToolItem) stack.getItem()).getToolType();
        List<ToolStats> stats = new ArrayList<>();
        for (int i = 0; i < materials.size(); i++) {
            ToolPart toolPart = ToolMaterialDictionary.getToolPartFromIndex(toolType, i);
            stats.add(materials.get(i).getStats(toolPart));
        }
        return ToolStats.add(stats);
    }

    public abstract ToolType getToolType();

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        reloadComponent(stack);
    }

    @Override
    public void onCraftedPostProcess(ItemStack stack, Level level) {
        super.onCraftedPostProcess(stack, level);

    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        MutableComponent component = Component.empty();
        if (stack.has(ComponentInit.PART_MATERIAL_COMPONENT)) {
            List<ToolMaterial> materials = stack.get(ComponentInit.PART_MATERIAL_COMPONENT).materials();
            if (materials.get(0).equals(materials.get(1))) {
                component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get() + ".simple",
                        Component.translatable("material.magitech." + materials.get(0).id)));
            } else {
                component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get() + ".complex",
                        Component.translatable("material.magitech." + materials.get(0).id),
                        Component.translatable("material.magitech." + materials.get(1).id)));
            }
        } else {
            component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get()));
        }
        return component;
    }

    public void reloadComponent(ItemStack stack) {

        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();


        ToolStats finalStats = getSumStats(stack);
        Map<String, Float> map = finalStats.getStats();

        entries.add(new ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, new AttributeModifier(atkId, map.get(ToolStats.ATK_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, new AttributeModifier(spdId, map.get(ToolStats.SPD_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ARMOR, new AttributeModifier(defId, map.get(ToolStats.DEF_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(rngId, map.get(ToolStats.RNG_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        ItemAttributeModifiers component = new ItemAttributeModifiers(entries, false);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, component);

        if (stack.getMaxDamage() != map.get(ToolStats.DUR_STAT)) {
            int newMaxDamage = Math.round(map.get(ToolStats.DUR_STAT));
            int oldMaxDamage = stack.getMaxDamage();
            int oldDamage = stack.getDamageValue();
            stack.set(DataComponents.MAX_DAMAGE, newMaxDamage);
            stack.setDamageValue((int) (((float) oldDamage) * newMaxDamage / oldMaxDamage));
        }
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        ToolStats stats = getSumStats(stack);
        if (stack.getItem() instanceof PartToolItem partToolItem && isCorrectTool(stack, state, partToolItem, stats)) {
            return stats.getStats().get(ToolStats.MIN_STAT);
        }
        return super.getDestroySpeed(stack, state);
    }

    private boolean isCorrectTool(ItemStack stack, BlockState state, PartToolItem partToolItem, ToolStats stats) {
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_AXE)) && partToolItem.getToolType() != ToolType.AXE) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_PICKAXE)) && partToolItem.getToolType() != ToolType.PICKAXE && partToolItem.getToolType() != ToolType.HAMMER) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_SHOVEL)) && partToolItem.getToolType() != ToolType.SHOVEL) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_HOE)) && partToolItem.getToolType() != ToolType.SCYTHE) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.SWORD_EFFICIENT)) && partToolItem.getToolType() != ToolType.DAGGER
                && partToolItem.getToolType() != ToolType.LIGHT_SWORD && partToolItem.getToolType() != ToolType.HEAVY_SWORD && partToolItem.getToolType() != ToolType.SCYTHE) {
            return false;
        }
        return hasCorrectTier(stack, state, stats);
    }

    private boolean hasCorrectTier(ItemStack stack, BlockState state, ToolStats stats) {
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.NETHERITE.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.DIAMOND.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_IRON_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.IRON.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_STONE_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.STONE.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_GOLD_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.NONE.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) && stats.getMiningLevel().getTier() <= MiningLevel.NONE.getTier()) {
            return false;
        }
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        ToolStats stats = getSumStats(stack);
        return isCorrectTool(stack, state, (PartToolItem) stack.getItem(), stats);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        ToolStats finalStats = getSumStats(stack);

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("attribute.magitech.attack_damage").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.ATK_STAT) + 1.0F, 2)
                )).withColor(0xFF4040)));

        tooltipComponents.add(Component.translatable("attribute.magitech.elemental_damage").append(": ").withColor(0xa0a0a0)
                .append(Component.translatable("element.magitech." + finalStats.getElement().get())
                        .append(" ")
                        .append(Component.literal(String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.ELM_ATK_STAT), 2)
                        ))).withColor(finalStats.getElement().getColor())));

        tooltipComponents.add(Component.translatable("attribute.magitech.attack_speed").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.SPD_STAT) + 4.0F, 2)
                )).withColor(0x40FFC0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.mining_speed").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.MIN_STAT), 2)
                )).withColor(0x4080C0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.defense").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.DEF_STAT), 2)
                )).withColor(0xA0C0C0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.attack_range").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.RNG_STAT) + 3.0F, 2)
                )).withColor(0x80c0FF)));

        tooltipComponents.add(Component.translatable("attribute.magitech.sweep_range").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.SWP_STAT), 2)
                )).withColor(0xFFFF80)));

        tooltipComponents.add(Component.translatable("attribute.magitech.durability").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        (finalStats.getStats().get(ToolStats.DUR_STAT).intValue() - stack.getDamageValue()) + " / " + finalStats.getStats().get(ToolStats.DUR_STAT).intValue()
                ).withColor(0xFFFFFF)));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult onLeftClick(Player user, InteractionHand hand, Level world) {

            Vec3 playerEyePos = user.getEyePosition(1.0F);
            Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
            double mul = user.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
            Vec3 maxReachPos = playerEyePos.add(forward.multiply(mul, mul, mul));

            if (EntityUtil.getEntityHitResult(user, playerEyePos, maxReachPos, world) != null || getPlayerPOVHitResult(world, user, ClipContext.Fluid.NONE).getType() != BlockHitResult.Type.BLOCK) {
                if (user.getAttackStrengthScale(0.5F) > 0.7F) {
                    world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
                    sweepAttack(world, hand, user);
                    user.awardStat(Stats.DAMAGE_DEALT, sweepDamage * 10);
                } else {
                    world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, SoundSource.PLAYERS);
                }
                user.swing(hand);
                return InteractionResult.SUCCESS;
            }
        return InteractionResult.PASS;
    }

    private void sweepAttack(Level world, InteractionHand hand, Player user) {
        ToolStats stats = getSumStats(user.getItemInHand(hand));
        float swp = stats.getStats().get(ToolStats.SWP_STAT);
        Vec3 effectCenter = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 0.5, swp * 0.7F);

        float red = ((float) ((stats.getElement().getColor() & 0xFF0000) >> 16)) / 255;
        float green = ((float) ((stats.getElement().getColor() & 0x00FF00) >> 8)) / 255;
        float blue = ((float) (stats.getElement().getColor() & 0x0000FF)) / 255;
        float red2 = red * red;
        float green2 = green * green;
        float blue2 = blue * blue;

        EffectUtil.sweepEffect(user, world, new UnstableSquareParticleEffect(new Vector3f(red, green, blue), new Vector3f(red2, green2, blue2), 1.0F, 1), effectCenter, 45.0, -45.0, 100, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0);

        Vec3 center = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 2, 0.0);
        List<Entity> attackList = EntityUtil.getEntitiesInBox(world, user, center, new Vec3(swp, swp / 3.0F, swp));

        float cooldown = ((AdjustableAttackStrengthEntity) user).getLastAttackedTicks();
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                ((AdjustableAttackStrengthEntity) user).setLastAttackedTicks((int) cooldown);
                applyElementDamage(user, target, user.getItemInHand(hand));
                user.attack(target);
            }
        }

        user.resetAttackStrengthTicker();
    }


    public void applyElementDamage(Player attacker, Entity target, ItemStack stack) {
        ResourceKey<DamageType> damageType = getSumStats(stack).getElement().getDamageType();
        if (damageType != null) {
            ToolStats stats = getSumStats(stack);
            float baseAttackDamage = (float) stats.getStats().get(ToolStats.ELM_ATK_STAT);

            float h = attacker.getAttackStrengthScale(0.5F);
            baseAttackDamage *= 0.2F + h * h * 0.8F;
            boolean bl3 = h > 0.9F
                    && attacker.fallDistance > 0.0F
                    && !attacker.onGround()
                    && !attacker.onClimbable()
                    && !attacker.isInWater()
                    && !attacker.hasEffect(MobEffects.BLINDNESS)
                    && attacker.getVehicle() == null
                    && target instanceof LivingEntity
                    && !attacker.isSprinting();
            if (bl3) {
                baseAttackDamage *= 1.5F;
            }

            DamageSource ElementalDamageSource = attacker.damageSources().source(damageType);
            float damage = baseAttackDamage * EntityElementDictionary.getElementAffinity(target, stats.getElement()).getMultiplier();
            if (target instanceof LivingEntity livingEntity) {
                float targetHealth = livingEntity.getHealth();
                target.hurt(ElementalDamageSource, damage);
                attacker.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingEntity.getHealth()) * 10));
                livingEntity.setLastHurtByPlayer(attacker);
            } else {
                Vec3 delta = target.getDeltaMovement();
                target.hurt(ElementalDamageSource, damage);
                target.setDeltaMovement(delta);
            }
            target.invulnerableTime = 0;
        }
    }
}
