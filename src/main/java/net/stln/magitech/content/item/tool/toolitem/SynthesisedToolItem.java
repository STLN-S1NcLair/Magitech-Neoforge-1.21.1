package net.stln.magitech.content.item.tool.toolitem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.entity.AdjustableAttackStrengthEntity;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.LeftClickOverrideItem;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.network.TierUpToastPayload;
import net.stln.magitech.content.network.TraitTickPayload;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.ToolHelper;
import net.stln.magitech.feature.tool.material.MiningLevel;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.*;
import net.stln.magitech.feature.tool.property.modifier.CrossRefToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.tool_type.MineType;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.feature.tool.trait.TraitHelper;
import net.stln.magitech.feature.tool.trait.TraitInstance;
import net.stln.magitech.feature.tool.upgrade.UpgradeInstance;
import net.stln.magitech.helper.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public abstract class SynthesisedToolItem extends Item implements LeftClickOverrideItem {

    private final ToolTypeLike toolTypeLike;

    public SynthesisedToolItem(Properties settings, ToolTypeLike toolTypeLike) {
        super(settings);
        this.toolTypeLike = toolTypeLike;
    }

    public ToolType getToolType() {
        return toolTypeLike.asToolType();
    }

    // PROPERTIES

    // 特性効果を上乗せしないツールの基本性能値
    public static ToolProperties getDefaultProperties(ItemStack stack) {
        List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
        if (!materials.isEmpty()) {
            SynthesisedToolItem item = (SynthesisedToolItem) stack.getItem();
            ToolType toolType = item.getToolType();
            ToolProperties baseProperties = toolType.defaultProperties();
            ToolCategory group = baseProperties.getGroup();
            ToolProperties result = baseProperties.copy();

            List<ToolType.PartData> parts = toolType.parts();
            for (int i = 0; i < materials.size(); i++) {
                ToolMaterial toolMaterial = materials.get(i);
                ToolType.PartData toolPart = parts.get(i);
                float weight = toolPart.weight();
                ToolProperties materialProp = group.cast(toolMaterial.properties());
                result = ToolPropertyHelper.simpleAdd(result, ToolPropertyHelper.scalarMul(materialProp, weight));
            }
            return result;
        } else return new ToolProperties(ToolCategoryInit.NONE);
    }

    // アップグレードを参照した性能値
    public static ToolProperties getUpgradedProperties(ItemStack stack) {
        ToolProperties defaultProperties = getDefaultProperties(stack);
        if (!stack.has(ComponentInit.BROKEN_COMPONENT)) {
            stack.set(ComponentInit.BROKEN_COMPONENT, false);
        }
        ToolCategory group = defaultProperties.getGroup();
        List<UpgradeInstance> upgrades = ComponentHelper.getUpgrades(stack);
        ToolProperties result = defaultProperties.copy();
        List<ToolPropertyModifier> selfRef = new ArrayList<>();
        List<ToolPropertyModifier> crossRef = new ArrayList<>();

        // selfRefを先に処理する
        for (UpgradeInstance upgrade : upgrades) {
            for (ToolPropertyModifier modifier : upgrade.getModifiers()) {
                if (modifier instanceof CrossRefToolPropertyModifier) {
                    crossRef.add(modifier);
                } else {
                    selfRef.add(modifier);
                }
            }
        }
        for (ToolPropertyModifier modifier : selfRef) {
            result = ToolPropertyHelper.simpleAdd(result, modifier.apply(defaultProperties));
        }
        for (ToolPropertyModifier modifier : crossRef) {
            result = ToolPropertyHelper.simpleAdd(result, modifier.apply(defaultProperties));
        }
        return result;
    }

    // 特性条件を参照した性能値
    public ToolProperties getAppliedProperties(Player player, Level level, ItemStack stack) {
        List<TraitInstance> traits = TraitHelper.getTrait(stack);
        ToolProperties upgradedProp = getUpgradedProperties(stack);
        ToolProperties result = upgradedProp.copy();
        List<ToolPropertyModifier> selfRef = new ArrayList<>();
        List<ToolPropertyModifier> crossRef = new ArrayList<>();

        // selfRefを先に処理する
        traits.forEach((instance) -> {
            for (ToolPropertyModifier modifier : instance.trait().modifyProperty(player, level, stack, instance.level(), upgradedProp)) {
                if (modifier instanceof CrossRefToolPropertyModifier) {
                    crossRef.add(modifier);
                } else {
                    selfRef.add(modifier);
                }
            }
        });

        for (ToolPropertyModifier modifier : selfRef) {
            result = ToolPropertyHelper.simpleAdd(result, modifier.apply(upgradedProp));
        }
        for (ToolPropertyModifier modifier : crossRef) {
            result = ToolPropertyHelper.simpleAdd(result, modifier.apply(upgradedProp));
        }
        return result;
    }

    private static boolean hasCorrectTier(ItemStack stack, BlockState state, ToolProperties properties) {
        if (ComponentHelper.isBroken(stack)) {
            return false;
        }
        int miningLevel = ((MiningLevel) properties.get(ToolPropertyInit.MINING_LEVEL)).getTier();
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)) && miningLevel <= MiningLevel.NETHERITE.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) && miningLevel <= MiningLevel.DIAMOND.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_IRON_TOOL)) && miningLevel <= MiningLevel.IRON.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_STONE_TOOL)) && miningLevel <= MiningLevel.STONE.getTier()) {
            return false;
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_GOLD_TOOL)) && miningLevel <= MiningLevel.NONE.getTier()) {
            return false;
        }
        return state.getTags().noneMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) || miningLevel > MiningLevel.NONE.getTier();
    }

    public static Direction getBreakDirection(double range, BlockPos initalBlockPos, Player player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue()))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return traceResult.getDirection();
    }

    public static void progress(ItemStack stack, Level level, Entity entity) {
        if (ComponentHelper.isBroken(stack)) {
            return;
        }
        if (stack.has(ComponentInit.PROGRESSION_COMPONENT)) {
            ComponentHelper.updateProgression(stack, 0, integer -> integer + 1);
            int progression = ComponentHelper.getProgression(stack);
            int maxProgression = ComponentHelper.getMaxProgression(stack);
            if (progression >= maxProgression) {
                ComponentHelper.updateTier(stack, 0, integer -> integer + 1);
                ComponentHelper.updateUpgradePoint(stack, value -> value + 1);
                ComponentHelper.updateProgression(stack, 0, integer -> 0);
                int tier = ComponentHelper.getTier(stack);
                int newMax = ToolHelper.getDefaultMaxProgression(tier);
                ComponentHelper.updateMaxProgression(stack, newMax, integer -> newMax);
                if (entity instanceof Player player) {
                    if (!level.isClientSide) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new TierUpToastPayload(player.getInventory().findSlotMatchingItem(stack), tier + 1, player.getUUID()));
                    }
                }
            }
        }
    }

    // COMPONENTS

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        List<TraitInstance> traits = TraitHelper.getTrait(stack);

        if (entity instanceof Player player) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND) == stack || player.getItemInHand(InteractionHand.OFF_HAND) == stack) {
                traits.forEach((instance) -> {
                    instance.trait().handTick(player, world, stack, instance.level(), getUpgradedProperties(stack), true);
                });
                if (world.isClientSide) {
                    PacketDistributor.sendToServer(new TraitTickPayload(player.getItemInHand(InteractionHand.MAIN_HAND) == stack, false, slot, entity.getUUID()));
                }
            }
            traits.forEach((instance) -> {
                instance.trait().inventoryTick(player, world, stack, instance.level(), getUpgradedProperties(stack), true);
            });
            if (world.isClientSide) {
                PacketDistributor.sendToServer(new TraitTickPayload(player.getItemInHand(InteractionHand.MAIN_HAND) == stack, true, slot, entity.getUUID()));
            }
        }

        if (entity instanceof Player player) {
            reloadComponent(player, world, stack);
        }
    }

    public void reloadComponent(Player player, Level level, ItemStack stack) {

        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();

        ToolProperties appliedProperties = getAppliedProperties(player, level, stack);

        stack.set(ComponentInit.BROKEN_COMPONENT, stack.getDamageValue() + 1 >= stack.getMaxDamage());

        // 壊れていなければAttributeを変化させる
        if (!ComponentHelper.isBroken(stack)) {
            for (Map.Entry<IToolProperty<?>, Object> propVal : appliedProperties.getValues().entrySet()) {
                IToolProperty<?> property = propVal.getKey();
                if (property instanceof AttributeToolProperty<?> attributeToolProperty) {
                    Holder<Attribute> attribute = attributeToolProperty.getAttribute(appliedProperties);
                    ResourceLocation typeKey = MagitechRegistries.TOOL_TYPE.getKey(this.getToolType());
                    ResourceLocation key = MagitechRegistries.TOOL_PROPERTY.getKey(attributeToolProperty);
                    entries.add(new ItemAttributeModifiers.Entry(attribute, new AttributeModifier(key.withPrefix("_" + typeKey.getPath()), attributeToolProperty.apply(appliedProperties), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
                }
            }

            // 特性によるAttribute変化
            TraitHelper.getTrait(stack).forEach((instance -> {
                instance.trait().modifyAttribute(player, level, stack, instance.level(), appliedProperties, entries);
            }));
        }

        ItemAttributeModifiers component = new ItemAttributeModifiers(entries, false);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, component);

        setInitialProperties(stack, appliedProperties);
    }

    protected void setInitialProperties(ItemStack stack, ToolProperties properties) {
        int tier = (int) properties.get(ToolPropertyInit.TIER);
        int upgradePoint = (int) properties.get(ToolPropertyInit.UPGRADE_POINT);
        int progression = (int) properties.get(ToolPropertyInit.PROGRESSION);
        double coefficient = (double) properties.get(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT);
        int maxProgression = (int) (ToolHelper.getDefaultMaxProgression(tier) * coefficient);
        int duration = (int) properties.get(ToolPropertyInit.DURATION);

        // 熟練度が最大以上ならTierを上げ続ける
        while (maxProgression <= progression) {
            progression -= maxProgression;
            tier++;
            upgradePoint++;
            maxProgression = (int) (ToolHelper.getDefaultMaxProgression(tier) * coefficient);
        }
        ComponentHelper.updateTier(stack, tier, UnaryOperator.identity());
        ComponentHelper.updateUpgradePoint(stack, upgradePoint, UnaryOperator.identity());
        ComponentHelper.updateProgression(stack, progression, UnaryOperator.identity());
        ComponentHelper.updateMaxProgression(stack, maxProgression, UnaryOperator.identity());

        if (stack.getMaxDamage() != duration) {
            int newMaxDamage = duration;
            int oldMaxDamage = stack.getMaxDamage();
            int oldDamage = stack.getDamageValue();
            if (newMaxDamage <= 0) {
                newMaxDamage = 1;
            }
            stack.set(DataComponents.MAX_DAMAGE, newMaxDamage);
            stack.setDamageValue((int) (((float) oldDamage) * newMaxDamage / oldMaxDamage));
        }
    }

    // DURATION

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        progress(stack, attacker.level(), attacker);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (stack.getMaxDamage() - stack.getDamageValue() - 1 <= amount) {
            if (stack.getMaxDamage() - stack.getDamageValue() <= amount) {
                return 0;
            }
            if (entity != null && !ComponentHelper.isBroken(stack)) {
                entity.level().playSound(null, entity.getOnPos(),
                        SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            stack.set(ComponentInit.BROKEN_COMPONENT, true);
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    // DISPLAY

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        MutableComponent component = Component.empty();
        List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
        if (stack.getItem() instanceof SynthesisedToolItem partToolItem) {
            ToolType toolType = partToolItem.getToolType();
            var firstMaterial = materials.getFirst();
            var secondMaterial = materials.get(1);
            ResourceLocation key = MagitechRegistries.TOOL_TYPE.getKey(toolType);
            if (materials.size() == toolType.parts().size() && firstMaterial != null && secondMaterial != null) {
                if (Objects.equals(firstMaterial, secondMaterial)) {
                    component.append(Component.translatable("item." + key.getNamespace() + "." + key.getPath() + ".simple", firstMaterial.getDescription()));
                } else {
                    component.append(Component.translatable("item." + key.getNamespace() + "." + key.getPath() + ".complex", firstMaterial.getDescription(), secondMaterial.getDescription()));
                }
            } else {
                component.append(Component.translatable("item." + key.getNamespace() + "." + key.getPath()));
            }
        }
        return component;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        addPropertiesHoverText(stack, tooltipComponents);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public void addPropertiesHoverText(@NotNull ItemStack stack, List<Component> tooltipComponents) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        ToolProperties appliedProperties = getAppliedProperties(player, player.level(), stack);
        setInitialProperties(stack, appliedProperties);

        tooltipComponents.add(Component.empty());
        if (ComponentHelper.isBroken(stack)) {
            tooltipComponents.add(Component.translatable("tool.magitech.broken").withColor(0xFF8080));
        }

        for (IToolProperty<?> property: appliedProperties.getValues().keySet()) {
            property.addTooltip(stack, appliedProperties, tooltipComponents);
        }

        tooltipComponents.add(Component.empty());

        TraitHelper.getTrait(stack).forEach(((instance) -> {
            tooltipComponents.add(TraitHelper.getTooltip(instance));
        }
        ));
    }

    public void traitAction(Level level, Player player, InteractionHand usedHand) {
        if (ComponentHelper.isBroken(player.getItemInHand(usedHand))) {
            return;
        }
        ItemStack stack = player.getItemInHand(usedHand);

        Vec3 playerEyePos = player.getEyePosition();
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        Vec3 maxReachPos = playerEyePos.add(forward.scale(256));

        EntityHitResult result = CombatHelper.getEntityHitResult(player, playerEyePos, maxReachPos, level);
        if (result != null) {
            TraitHelper.getTrait(stack).forEach(((instance) -> {
                instance.trait().traitAction(player, level, result.getEntity(), result.getLocation(), stack, instance.level(), getAppliedProperties(player, level, stack), usedHand, true);
            }));
        } else {
            TraitHelper.getTrait(stack).forEach(((instance) -> {
                instance.trait().traitAction(player, level, null, new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), stack, instance.level(), getAppliedProperties(player, level, stack), usedHand, true);
            }));
        }
    }

    // BLOCK

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity miningEntity) {
        if (state.getDestroySpeed(level, pos) != 0) {
            stack.hurtAndBreak(1, (ServerLevel) level, miningEntity, item -> {
            });
            progress(stack, level, miningEntity);
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    public boolean isCorrectTool(ItemStack stack, BlockState state, SynthesisedToolItem partToolItem, ToolProperties properties) {
        if (ComponentHelper.isBroken(stack)) {
            return false;
        }
        final Boolean[] flag = {null};
        // falseを優先
        TraitHelper.getTrait(stack).forEach((instance) -> {
            Boolean isCorrect = instance.trait().modifyCorrectTool(stack, instance.level(), state);
            if (isCorrect != false) {
                flag[0] = isCorrect;
            }
        });
        if (flag[0] != null) {
            return flag[0];
        }
        ToolType toolType = partToolItem.getToolType();
        for (MineType mineType : toolType.mineType().types()) {
            if (state.getTags().anyMatch(Predicate.isEqual(mineType.getMinable()))) {
                return hasCorrectTier(stack, state, properties);
            }
        }
        return false;
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        if (ComponentHelper.isBroken(stack)) {
            return false;
        }

        SynthesisedToolItem partToolItem = (SynthesisedToolItem) stack.getItem();
        ToolType toolType = partToolItem.getToolType();
        for (MineType mineType : toolType.mineType().types()) {
            if (mineType.getAbilities().contains(itemAbility)) {
                return true;
            }
        }
        return super.canPerformAction(stack, itemAbility);
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        if (ComponentHelper.isBroken(stack)) {
            return false;
        }
        ToolProperties properties = getUpgradedProperties(stack);
        return isCorrectTool(stack, state, (SynthesisedToolItem) stack.getItem(), properties);
    }

    // ATTACK

    @Override
    public InteractionResult onLeftClick(Player user, InteractionHand hand, Level world) {
        if (ComponentHelper.isBroken(user.getItemInHand(hand))) {
            return InteractionResult.PASS;
        }

        Vec3 playerEyePos = user.getEyePosition(1.0F);
        Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
        double mul = user.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 maxReachPos = playerEyePos.add(forward.scale(mul));

        if (CombatHelper.getEntityHitResult(user, playerEyePos, maxReachPos, world) != null || getPlayerPOVHitResult(world, user, ClipContext.Fluid.NONE).getType() != BlockHitResult.Type.BLOCK) {
            float pitch = (float) getAppliedProperties(user, world, user.getItemInHand(hand)).get(ToolPropertyInit.ATTACK_SPEED) * 0.5F;
            if (user.getAttackStrengthScale(0.5F) > 0.7F) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, pitch);
                sweepAttack(world, hand, user);
            } else {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, SoundSource.PLAYERS, 1.0F, pitch);
            }
            user.swing(hand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void sweepAttack(Level world, InteractionHand hand, Player user) {
        ItemStack stack = user.getItemInHand(hand);
        ToolProperties stats = getAppliedProperties(user, world, stack);
        float swp = (float) stats.get(ToolPropertyInit.SWEEP);
        sweepVFX(world, user, swp, stats);

        Vec3 center = CombatHelper.getAttackTargetPosition(user, user.entityInteractionRange(), 2, 0.0);
        List<Entity> attackList = CombatHelper.getEntitiesInBox(world, user, center, new Vec3(swp, swp / 3.0F, swp));
        attackList.removeIf(e -> !(e instanceof LivingEntity livingEntity) || e == user || !user.canAttack(livingEntity) || e.isInvulnerable());

        float cooldown = ((AdjustableAttackStrengthEntity) user).getLastAttackedTicks();
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                ((AdjustableAttackStrengthEntity) user).setLastAttackedTicks((int) cooldown);
                applyElementDamage(user, target, stack);
                user.attack(target);
                callTraitDamageEntity(world, user, target, stack);
            }
        }
        if (!attackList.isEmpty() && !user.level().isClientSide) {
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
            progress(stack, world, user);
        }

        user.resetAttackStrengthTicker();
    }

    public void applyElementDamage(Player attacker, Entity target, ItemStack stack) {
        ToolProperties appliedProperties = getAppliedProperties(attacker, attacker.level(), stack);
        ElementalAttributeToolProperty prop = (ElementalAttributeToolProperty) ToolPropertyInit.ELEMENTAL_DAMAGE.asToolProperty();
        Element element = prop.getElement(appliedProperties.get(prop));

        float baseDamage = (float) (attacker.getAttribute(AttributeInit.ELEMENTAL_DAMAGE).getValue());

        // 基本ダメージ量の計算
        float h = attacker.getAttackStrengthScale(0.5F);
        baseDamage *= 0.2F + h * h * 0.8F;
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
            baseDamage *= 1.5F;
        }
        ResourceKey<DamageType> damageType = element.getDamageType();
        DamageSource elementalDamageSource = attacker.damageSources().source(damageType, attacker);

        float effectiveDamage = baseDamage * DataMapHelper.getElementMultiplier(target, element);
        if (target instanceof LivingEntity livingEntity) {
            float targetHealth = livingEntity.getHealth();
            if (!target.isInvulnerableTo(elementalDamageSource)) {
                livingEntity.setLastHurtByPlayer(attacker);
                attacker.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingEntity.getHealth()) * 10));
            }
            target.hurt(elementalDamageSource, effectiveDamage);
        } else {
            Vec3 delta = target.getDeltaMovement();
            target.hurt(elementalDamageSource, effectiveDamage);
            target.setDeltaMovement(delta);
        }
        target.invulnerableTime = 0;
    }

    private static void sweepVFX(Level world, Player user, float swp, ToolProperties properties) {
//        Vec3 effectCenter = CombatHelper.getAttackTargetPosition(user, user.entityInteractionRange(), 0.5, swp * 0.7F);
//
//        int color = properties.getElement().getTextColor().getRGB();
//        float red = ((float) ((color & 0xFF0000) >> 16)) / 255;
//        float green = ((float) ((color & 0x00FF00) >> 8)) / 255;
//        float blue = ((float) (color & 0x0000FF)) / 255;
//        float red2 = red * red;
//        float green2 = green * green;
//        float blue2 = blue * blue;
//
//        if (properties.getElement() == Element.EMBER) {
//            EffectHelper.sweepEffect(user, world, () -> new FlameParticleEffect(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), 2F, 1, 0, world.random.nextInt(5, 8), 0.9F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.GLACE) {
//            EffectHelper.sweepEffect(user, world, () -> new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(50, 60), 0.99F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.SURGE) {
//            EffectHelper.sweepEffect(user, world, () -> new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 3, 0, world.random.nextInt(5, 15), 0.99F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.PHANTOM) {
//            EffectHelper.sweepEffect(user, world, () -> new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(10, 40), 0.85F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.TREMOR) {
//            EffectHelper.sweepEffect(user, world, () -> new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(5, 10), 0.9F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.MAGIC) {
//            EffectHelper.sweepEffect(user, world, () -> new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(5, 20), 0.9F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.FLOW) {
//            EffectHelper.sweepEffect(user, world, () -> new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(10, 30), 0.87F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else if (properties.getElement() == Element.HOLLOW) {
//            EffectHelper.sweepEffect(user, world, () -> new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, world.random.nextInt(1, 21), 1.0F), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        } else {
//            EffectHelper.sweepEffect(user, world, () -> new SquareParticleEffect(new Vector3f(red, green, blue), new Vector3f(red2, green2, blue2), 1F, 1, 0, 15, 1.0F), 0.01, effectCenter, -45.0, 45.0, 100, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
//        }
    }

    // ENCHANTS

    @Override
    public int getEnchantmentLevel(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
        if (ComponentHelper.isBroken(stack)) {
            return super.getEnchantmentLevel(stack, enchantment);
        }
        int level = super.getEnchantmentLevel(stack, enchantment);
        final int[] add = {0};
        final int[] enhance = {0};
        TraitHelper.getTrait(stack).forEach(((instance) -> {
            add[0] = Math.max(add[0], instance.trait().addEnchantments(stack, instance.level(), getUpgradedProperties(stack), enchantment, level));
        }));
        TraitHelper.getTrait(stack).forEach(((instance) -> {
            enhance[0] += instance.trait().enhanceEnchantments(stack, instance.level(), getUpgradedProperties(stack), enchantment, add[0] + level);
        }));
        return Math.max(0, level + add[0] + enhance[0]);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
        return false;
    }

    // CALL METHODS

    public void callTraitDamageEntity(Level world, Player user, Entity target, ItemStack stack) {
        TraitHelper.getTrait(stack).forEach(((instance) -> {
            instance.trait().onDamageEntity(user, world, stack, instance.level(), getAppliedProperties(user, world, stack), target);
        }));
    }

    public void callTestRepair(Level world, Player user, int amount, ItemStack stack) {
        TraitHelper.getTrait(stack).forEach(((instance) -> {
            instance.trait().testRepair(user, world, stack, instance.level(), getAppliedProperties(user, world, stack), amount);
        }));
    }

    public void callOnRepair(Level world, Player user, int amount, ItemStack stack) {
        TraitHelper.getTrait(stack).forEach(((instance) -> {
            instance.trait().onRepair(user, world, stack, instance.level(), getAppliedProperties(user, world, stack), amount);
        }));
    }
}
