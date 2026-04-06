package net.stln.magitech.content.item.tool.toolitem;

import net.minecraft.client.gui.screens.Screen;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.phys.HitResult;
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
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.ToolHelper;
import net.stln.magitech.feature.tool.material.MiningLevel;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.property.*;
import net.stln.magitech.feature.tool.property.modifier.CrossRefToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.tool_type.MineType;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.feature.tool.trait.TraitHelper;
import net.stln.magitech.feature.tool.trait.TraitInstance;
import net.stln.magitech.feature.tool.upgrade.UpgradeInstance;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.DataMapHelper;
import net.stln.magitech.registry.DeferredToolProperty;
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
            ToolProperties baseProperties = toolType.defaultProperties().get();
            ToolCategory category = baseProperties.getCategory();
            ToolProperties result = new ToolProperties(category);

            List<ToolType.PartData> parts = toolType.parts();
            for (int i = 0; i < materials.size(); i++) {
                ToolMaterial toolMaterial = materials.get(i);
                ToolType.PartData toolPart = parts.get(i);
                float weight = toolPart.weight();
                ToolProperties materialProp = category.cast(toolMaterial.properties().get());
                result = ToolPropertyHelper.simpleAdd(result, ToolPropertyHelper.scalarMul(ToolPropertyHelper.simpleMul(baseProperties, materialProp), weight / parts.size()));
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
        ToolProperties selfRefProps = result.copy();
        for (ToolPropertyModifier modifier : crossRef) {
            result = ToolPropertyHelper.simpleAdd(result, modifier.apply(selfRefProps));
        }
        return result;
    }

    private static boolean hasCorrectTier(ItemStack stack, BlockState state, ToolProperties properties) {
        if (ComponentHelper.isBroken(stack)) {
            return false;
        }
        int miningLevel = ((MiningLevel) properties.getOrId(ToolPropertyInit.MINING_LEVEL.get())).getTier();
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
            double coefficient = getUpgradedProperties(stack).getOrId(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT.get());
            int maxProgression = ComponentHelper.getMaxProgression(stack);
            if (progression >= maxProgression) {
                ComponentHelper.updateTier(stack, 0, integer -> integer + 1);
                ComponentHelper.updateUpgradePoint(stack, value -> value + 1);
                ComponentHelper.updateProgression(stack, 0, integer -> 0);
                int tier = ComponentHelper.getTier(stack);
                int newMax = (int) (ToolHelper.getDefaultMaxProgression(tier) * coefficient);
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
                    entries.add(new ItemAttributeModifiers.Entry(attribute, new AttributeModifier(key.withPrefix(typeKey.getPath() + "_"), attributeToolProperty.apply(appliedProperties), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
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
        int tier = Math.toIntExact(Math.round(properties.getOrId(ToolPropertyInit.TIER.get())));
        int upgradePoint = properties.getOrId(ToolPropertyInit.UPGRADE_POINT.get());
        int progression = properties.getOrId(ToolPropertyInit.PROGRESSION.get());
        double coefficient = properties.getOrId(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT.get());
        int maxProgression = Math.toIntExact(Math.round(ToolHelper.getDefaultMaxProgression(tier) * coefficient));
        int duration = properties.getOrId(ToolPropertyInit.DURABILITY.get()).intValue();

        // 熟練度が最大以上ならTierを上げ続ける
        while (maxProgression <= progression && maxProgression > 0) {
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
        damage(stack, attacker, attacker.level());
    }

    protected void damage(ItemStack stack, @NotNull LivingEntity entity, Level level) {
        stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
        progress(stack, level, entity);
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

        List<TraitInstance> traits = TraitHelper.getTrait(stack);

        if (Screen.hasShiftDown()) {

            for (TraitInstance instance : traits) {
                tooltipComponents.add(TraitHelper.getTooltip(instance));

                for (ToolPropertyModifier mod : instance.trait().modifyProperty(player, player.level(), stack, instance.level(), appliedProperties)) {
                    tooltipComponents.add(mod.getDisplayText());
                }
                tooltipComponents.add(Component.empty());
            }
        } else if (Screen.hasControlDown()) {

            for (TraitInstance instance : traits) {
                Trait trait = instance.trait();
                tooltipComponents.add(trait.getComponent());
                trait.addDescription(tooltipComponents);
                tooltipComponents.add(Component.empty());
            }

        } else {

            tooltipComponents.add(Component.translatable("tooltip.magitech.tool.shift").withColor(0x808080));
            tooltipComponents.add(Component.translatable("tooltip.magitech.tool.ctrl").withColor(0x808080));

            tooltipComponents.add(Component.empty());

            if (ComponentHelper.isBroken(stack)) {
                tooltipComponents.add(Component.translatable("tool.magitech.broken").withColor(0xFF8080));
            }

            addPropertyTooltip(stack, tooltipComponents, appliedProperties);

            tooltipComponents.add(Component.empty());

            TraitHelper.getTrait(stack).forEach(((instance) -> {
                tooltipComponents.add(TraitHelper.getTooltip(instance));
            }
            ));
        }
    }

    protected void addPropertyTooltip(@NotNull ItemStack stack, List<Component> tooltipComponents, ToolProperties appliedProperties) {
        for (IToolProperty<?> property : appliedProperties.getCategory().getKeys().stream().map(ToolPropertyLike::asToolProperty).toList()) {
            property.addTooltip(stack, appliedProperties, tooltipComponents);
        }
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
            if (isCorrect == null) {
                flag[0] = isCorrect;
            }
        });
        if (flag[0] != null) {
            return flag[0];
        }
        ToolType toolType = partToolItem.getToolType();
        for (MineType mineType : toolType.mineType().types()) {
            if (state.getTags().anyMatch(key -> mineType.getMinable().contains(key))) {
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
            float pitch = getAppliedProperties(user, world, user.getItemInHand(hand)).getOrId(ToolPropertyInit.ATTACK_SPEED.get()).floatValue() * 0.5F;
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

    public void sweepAttack(Level level, InteractionHand hand, Player user) {
        ItemStack stack = user.getItemInHand(hand);
        ToolProperties stats = getAppliedProperties(user, level, stack);
        float swp = stats.getOrId(ToolPropertyInit.SWEEP.get()).floatValue();
        if (level.isClientSide) {
            sweepVFX(level, user, swp, stats);
        }

        Vec3 center = CombatHelper.getAttackTargetPosition(user, user.entityInteractionRange(), 2, 0.0);
        List<Entity> attackList = new ArrayList<>(CombatHelper.getEntitiesInBox(level, user, center, new Vec3(swp, swp / 3.0F, swp)).stream()
                .filter(entity -> level.clip(new ClipContext(CombatHelper.getBodyPos(entity), CombatHelper.getBodyPos(user), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user)).getType() != HitResult.Type.BLOCK).toList());
        attackList.removeIf(e -> !(e instanceof LivingEntity livingEntity) || e == user || !user.canAttack(livingEntity) || e.isInvulnerable());

        float cooldown = ((AdjustableAttackStrengthEntity) user).getLastAttackedTicks();
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                ((AdjustableAttackStrengthEntity) user).setLastAttackedTicks((int) cooldown);
                applyElementDamage(user, target, stack);
                user.attack(target);
                callTraitDamageEntity(level, user, target, stack);
            }
        }
        if (!attackList.isEmpty() && !user.level().isClientSide) {
            damage(stack, user, level);
        }

        user.resetAttackStrengthTicker();
    }

    public void applyElementDamage(Player attacker, Entity target, ItemStack stack) {
        ToolProperties appliedProperties = getAppliedProperties(attacker, attacker.level(), stack);
        ElementalAttributeToolProperty prop = (ElementalAttributeToolProperty) ToolPropertyInit.ELEMENTAL_DAMAGE.asToolProperty();
        Element element = prop.getElement(appliedProperties.getOrId(prop));

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
        target.invulnerableTime = 0;
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

    private static void sweepVFX(Level level, Player user, float swp, ToolProperties properties) {
        float radius = swp * 0.7F;
        Vec3 effectCenter = CombatHelper.getAttackTargetPosition(user, user.entityInteractionRange(), 0.5, radius);

        float startDeg = -45.0F;
        float endDeg = 45.0F;

        RandomSource random = level.random;
        if (random.nextFloat() < (user.getMainArm() == HumanoidArm.LEFT ? 0.8F : 0.2F)) {
            startDeg *= -1;
            endDeg *= -1;
        }
        float slopeDeg = Mth.randomBetween(random, -10F, 10F);

        if (random.nextFloat() < 0.2F) {
            slopeDeg = Mth.randomBetween(random, 30F, 90F);
        }

        ElementalAttributeToolProperty elementalDamage = ToolPropertyInit.ELEMENTAL_DAMAGE.get();
        Element element = elementalDamage.getElement(properties.get(elementalDamage));
        TrailVFX.arcTrail(level, effectCenter, user.getRotationVector(), startDeg, endDeg, slopeDeg, 0.5F, radius, 20F, 20, element);
        LineVFX.arcLinedSquare(level, effectCenter, user.getRotationVector(), element, startDeg, endDeg, slopeDeg, radius, 7.0F, 0.07F, 0.02F);
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
