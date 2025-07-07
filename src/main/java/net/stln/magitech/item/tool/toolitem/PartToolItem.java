package net.stln.magitech.item.tool.toolitem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.entity.AdjustableAttackStrengthEntity;
import net.stln.magitech.item.LeftClickOverrideItem;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.MiningLevel;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.Trait;
import net.stln.magitech.network.TraitTickPayload;
import net.stln.magitech.particle.particle_option.*;
import net.stln.magitech.util.ColorHelper;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.MathUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Predicate;

public abstract class PartToolItem extends Item implements LeftClickOverrideItem {
    ResourceLocation atkId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_damage_modifier");
    ResourceLocation spdId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_speed_modifier");
    ResourceLocation defId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_defense_modifier");
    ResourceLocation rngId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_range_modifier");

    public PartToolItem(Properties settings) {
        super(settings);
    }

    public static ToolStats getDefaultStats(ItemStack stack) {

        if (stack.has(ComponentInit.PART_MATERIAL_COMPONENT)) {
            List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
            ToolType toolType = ((PartToolItem) stack.getItem()).getToolType();
            List<ToolStats> stats = new ArrayList<>();
            ToolStats base = ToolMaterialRegister.getBaseStats(toolType);
            for (int i = 0; i < materials.size(); i++) {
                ToolPart toolPart = ToolMaterialRegister.getToolPartFromIndex(toolType, i);
                float value = 0;
                if (toolPart != null) {
                    value = ((PartToolItem) stack.getItem()).getMultiplier(toolPart);
                }
                if (materials.get(i) != null) {
                    if (!(stack.getItem() instanceof SpellCasterItem)) {
                        stats.add(ToolStats.mulWithoutElementCode(ToolStats.mulWithoutElementCode(materials.get(i).getStats(), base), value));
                    } else {
                        stats.add(ToolStats.mulWithoutElementCode(ToolStats.mulWithoutElementCode(materials.get(i).getSpellCasterStats(), base), value));
                    }
                }
            }
            return ToolStats.add(stats);
        }
        return ToolStats.DEFAULT;
    }

    public static @NotNull Set<ToolMaterial> getMaterialSet(List<ToolMaterial> materials) {
        Set<ToolMaterial> materialSet = new HashSet<>(Set.of());
        materialSet.addAll(materials);
        return materialSet;
    }

    public static List<Trait> getTraits(ItemStack stack) {

        if (stack.has(ComponentInit.PART_MATERIAL_COMPONENT)) {
            List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
            List<Trait> traits = new ArrayList<>();

            for (int i = 0; i < materials.size(); i++) {
                if (materials.get(i) != null) {
                    traits.add(materials.get(i).getTrait());
                }
            }
            return traits;
        }
        return new ArrayList<>();
    }

    public static @NotNull Map<Trait, Integer> getTraitLevel(List<Trait> traits) {
        Map<Trait, Integer> traitLevel = new HashMap<>();
        for (Trait trait : traits) {
            if (trait != null) {
                traitLevel.put(trait, traitLevel.getOrDefault(trait, 0) + 1);
            }
        }
        return traitLevel;
    }

    private static boolean hasCorrectTier(ItemStack stack, BlockState state, ToolStats stats) {
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

    public static Direction getBreakDirection(double range, BlockPos initalBlockPos, Player player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return traceResult.getDirection();
    }

    public ToolStats getSumStats(Player player, Level level, ItemStack stack) {
        return getModifiedStats(player, level, stack);
    }

    public ToolStats getModifiedStats(Player player, Level level, ItemStack stack) {
        Map<Trait, Integer> traits = getTraitLevel(getTraits(stack));
        List<ToolStats> statsList = new ArrayList<>();
        statsList.add(getDefaultStats(stack));
        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats1(stack, value, getDefaultStats(stack)));
                statsList.add(trait.modifyStatsConditional1(player, level, stack, value, getDefaultStats(stack)));
            }
        });
        ToolStats stats1 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats2(stack, value, stats1));
                statsList.add(trait.modifyStatsConditional2(player, level, stack, value, stats1));
            }
        });
        ToolStats stats2 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats3(stack, value, stats2));
                statsList.add(trait.modifyStatsConditional3(player, level, stack, value, stats2));
            }
        });
        return ToolStats.add(statsList);
    }

    public ToolStats getSumStatsWithoutConditional(ItemStack stack) {
        return getModifiedStatsWithoutConditional(stack);
    }

    public ToolStats getModifiedStatsWithoutConditional(ItemStack stack) {
        Map<Trait, Integer> traits = getTraitLevel(getTraits(stack));
        List<ToolStats> statsList = new ArrayList<>();
        statsList.add(getDefaultStats(stack));
        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats1(stack, value, getDefaultStats(stack)));
            }
        });
        ToolStats stats1 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats2(stack, value, stats1));
            }
        });
        ToolStats stats2 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifyStats3(stack, value, stats2));
            }
        });
        return ToolStats.add(statsList);
    }

    public boolean isCorrectTool(ItemStack stack, BlockState state, PartToolItem partToolItem, ToolStats stats) {
        final Boolean[] flag = {null};
        getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
            Boolean isCorrect = trait.isCorrectTool(stack, integer, this.getModifiedStatsWithoutConditional(stack), state);
            if (isCorrect != null) {
                flag[0] = isCorrect;
                if (!isCorrect) {
                    return;
                }
            }
        });
        if (flag[0] != null) {
            return flag[0];
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_AXE)) && partToolItem.getToolType() == ToolType.AXE) {
            return hasCorrectTier(stack, state, stats);
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_PICKAXE)) && (partToolItem.getToolType() == ToolType.PICKAXE || partToolItem.getToolType() == ToolType.HAMMER)) {
            return hasCorrectTier(stack, state, stats);
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_SHOVEL)) && partToolItem.getToolType() == ToolType.SHOVEL) {
            return hasCorrectTier(stack, state, stats);
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.MINEABLE_WITH_HOE)) && partToolItem.getToolType() == ToolType.SCYTHE) {
            return hasCorrectTier(stack, state, stats);
        }
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.SWORD_EFFICIENT)) && (partToolItem.getToolType() == ToolType.DAGGER
                || partToolItem.getToolType() == ToolType.LIGHT_SWORD || partToolItem.getToolType() == ToolType.HEAVY_SWORD || partToolItem.getToolType() == ToolType.SCYTHE)) {
            return hasCorrectTier(stack, state, stats);
        }
        return false;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        PartToolItem partToolItem = (PartToolItem) stack.getItem();
        if (ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility) && partToolItem.getToolType() == ToolType.AXE) {
            return true;
    }
        if (ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) && (partToolItem.getToolType() == ToolType.PICKAXE || partToolItem.getToolType() == ToolType.HAMMER)) {
            return true;
    }
        if (ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility) && partToolItem.getToolType() == ToolType.SHOVEL) {
            return true;
    }
        if (ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility) && partToolItem.getToolType() == ToolType.SCYTHE) {
            return true;
    }
        if (ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility) && (partToolItem.getToolType() == ToolType.DAGGER
                || partToolItem.getToolType() == ToolType.LIGHT_SWORD || partToolItem.getToolType() == ToolType.HEAVY_SWORD || partToolItem.getToolType() == ToolType.SCYTHE)) {
        return true;
    }
        return super.canPerformAction(stack, itemAbility);
    }

    public abstract ToolType getToolType();

    public abstract float getMultiplier(ToolPart part);

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof Player player && player.getItemInHand(InteractionHand.MAIN_HAND) == stack) {
            getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
                trait.tick(player, world, stack, integer, getDefaultStats(stack));
            });
            if (world.isClientSide) {
                PacketDistributor.sendToServer(new TraitTickPayload(((Player) entity).getItemInHand(InteractionHand.MAIN_HAND) == stack, entity.getUUID().toString()));
            }
        }

        if (entity instanceof Player player) {
            reloadComponent(player, world, stack);
        }
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
            ToolType toolType = ((PartToolItem) stack.getItem()).getToolType();

            if (materials.size() == toolType.getSize() && materials.get(0) != null && materials.get(1) != null) {
                if (materials.get(0).equals(materials.get(1))) {
                    component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get() + ".simple",
                            Component.translatable("material." + materials.get(0).getId().getNamespace() + "." + materials.get(0).getId().getPath())));
                } else {
                    component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get() + ".complex",
                            Component.translatable("material." + materials.get(0).getId().getNamespace() + "." + materials.get(0).getId().getPath()),
                            Component.translatable("material." + materials.get(1).getId().getNamespace() + "." + materials.get(1).getId().getPath())));
                }
            } else {
                component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get()));
            }

        } else {
            component.append(Component.translatable("item.magitech." + ((PartToolItem) stack.getItem()).getToolType().get()));
        }
        return component;
    }

    public void reloadComponent(Player player, Level level, ItemStack stack) {

        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();


        ToolStats finalStats = getSumStats(player, level, stack);
        Map<String, Float> map = finalStats.getStats();

        entries.add(new ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, new AttributeModifier(atkId, map.get(ToolStats.ATK_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, new AttributeModifier(spdId, map.get(ToolStats.SPD_STAT) - 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ARMOR, new AttributeModifier(defId, map.get(ToolStats.DEF_STAT), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(rngId, map.get(ToolStats.RNG_STAT) - 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND));
        modifyTraitAttribute(player, level, stack, finalStats, entries);
        ItemAttributeModifiers component = new ItemAttributeModifiers(entries, false);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, component);
        setTier(stack, finalStats);

        if (stack.getMaxDamage() != map.get(ToolStats.DUR_STAT)) {
            int newMaxDamage = Math.round(map.get(ToolStats.DUR_STAT));
            int oldMaxDamage = stack.getMaxDamage();
            int oldDamage = stack.getDamageValue();
            if (newMaxDamage <= 0) {
                newMaxDamage = 1;
            }
            stack.set(DataComponents.MAX_DAMAGE, newMaxDamage);
            stack.setDamageValue((int) (((float) oldDamage) * newMaxDamage / oldMaxDamage));
        }
    }

    protected void setTier(ItemStack stack, ToolStats finalStats) {
        if (!stack.has(ComponentInit.TIER_COMPONENT)) {
            stack.set(ComponentInit.TIER_COMPONENT, finalStats.getTier() * 5 / this.getToolType().getSize());
        }
    }

    protected void modifyTraitAttribute(Player player, Level level, ItemStack stack, ToolStats finalStats, List<ItemAttributeModifiers.Entry> entries) {
        getTraitLevel(getTraits(stack)).forEach((trait, value) -> {
            trait.modifyAttribute(player, level, stack, value, finalStats, entries);
        });
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        ToolStats stats = getSumStatsWithoutConditional(stack);
        return isCorrectTool(stack, state, (PartToolItem) stack.getItem(), stats);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        addStatsHoverText(stack, tooltipComponents);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    protected void addStatsHoverText(@NotNull ItemStack stack, List<Component> tooltipComponents) {
        ToolStats finalStats = getSumStatsWithoutConditional(stack);
        setTier(stack, finalStats);

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("attribute.magitech.tier").append(" ")
                .append(String.valueOf(stack.get(ComponentInit.TIER_COMPONENT))
                ).withColor(ColorHelper.getTierColor(stack.get(ComponentInit.TIER_COMPONENT))));

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
                        MathUtil.round(finalStats.getStats().get(ToolStats.SPD_STAT), 2)
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
                        MathUtil.round(finalStats.getStats().get(ToolStats.RNG_STAT), 2)
                )).withColor(0x80c0FF)));

        tooltipComponents.add(Component.translatable("attribute.magitech.sweep_range").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.SWP_STAT), 2)
                )).withColor(0xFFFF80)));

        tooltipComponents.add(Component.translatable("attribute.magitech.durability").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        (finalStats.getStats().get(ToolStats.DUR_STAT).intValue() - stack.getDamageValue()) + " / " + finalStats.getStats().get(ToolStats.DUR_STAT).intValue()
                ).withColor(0xFFFFFF)));
        Map<Trait, Integer> traitIntegerMap = getTraitLevel(getTraits(stack));
        traitIntegerMap.forEach(((trait, integer) -> {
            if (trait != null) {
                MutableComponent component = trait.getComponent().append(" ");
                if (integer < 6 && trait.getMaxLevel() != 1) {
                    for (int i = 0; i < (trait.getMaxLevel() == -1 ? integer : Math.min(integer, trait.getMaxLevel())); i++) {
                        component.append("|");
                    }
                } else if (trait.getMaxLevel() != 1) {
                    component.append(integer.toString());
                }
                tooltipComponents.add(component);

            }
        }
        ));
    }

    public void traitAction(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        Vec3 playerEyePos = player.getEyePosition();
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        double mul = 256;
        Vec3 maxReachPos = playerEyePos.add(forward.multiply(mul, mul, mul));

        EntityHitResult result = EntityUtil.getEntityHitResult(player, playerEyePos, maxReachPos, level);
        if (result != null) {
            getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
                trait.traitAction(player, level, result.getEntity(), result.getLocation(), stack, integer, getSumStats(player, level, stack), usedHand, true);
            });
        } else {
            getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
                trait.traitAction(player, level, null, new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), stack, integer, getSumStats(player, level, stack), usedHand, true);
            });
        }
    }

    @Override
    public InteractionResult onLeftClick(Player user, InteractionHand hand, Level world) {

        Vec3 playerEyePos = user.getEyePosition(1.0F);
        Vec3 forward = Vec3.directionFromRotation(user.getRotationVector());
        double mul = user.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 maxReachPos = playerEyePos.add(forward.multiply(mul, mul, mul));

        if (EntityUtil.getEntityHitResult(user, playerEyePos, maxReachPos, world) != null || getPlayerPOVHitResult(world, user, ClipContext.Fluid.NONE).getType() != BlockHitResult.Type.BLOCK) {
            if (user.getAttackStrengthScale(0.5F) > 0.7F) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, (getSumStats(user, world, user.getItemInHand(hand)).getStats().get(ToolStats.SPD_STAT)) / 2);
                sweepAttack(world, hand, user);
            } else {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, SoundSource.PLAYERS, 1.0F, (getSumStats(user, world, user.getItemInHand(hand)).getStats().get(ToolStats.SPD_STAT)) / 2);
            }
            user.swing(hand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        List<Trait> traits = getTraits(stack);
        stack.hurtAndBreak(1, (ServerLevel) level, miningEntity, item -> {
        });
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    public void sweepAttack(Level world, InteractionHand hand, Player user) {
        ItemStack stack = user.getItemInHand(hand);
        ToolStats stats = getSumStats(user, world, stack);
        float swp = stats.getStats().get(ToolStats.SWP_STAT);
        Vec3 effectCenter = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 0.5, swp * 0.7F);

        float red = ((float) ((stats.getElement().getColor() & 0xFF0000) >> 16)) / 255;
        float green = ((float) ((stats.getElement().getColor() & 0x00FF00) >> 8)) / 255;
        float blue = ((float) (stats.getElement().getColor() & 0x0000FF)) / 255;
        float red2 = red * red;
        float green2 = green * green;
        float blue2 = blue * blue;

        if (stats.getElement() == Element.EMBER) {
            EffectUtil.sweepEffect(user, world, new FlameParticleEffect(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.GLACE) {
            EffectUtil.sweepEffect(user, world, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.SURGE) {
            EffectUtil.sweepEffect(user, world, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 3, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.PHANTOM) {
            EffectUtil.sweepEffect(user, world, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.TREMOR) {
            EffectUtil.sweepEffect(user, world, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.MAGIC) {
            EffectUtil.sweepEffect(user, world, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.FLOW) {
            EffectUtil.sweepEffect(user, world, new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else if (stats.getElement() == Element.HOLLOW) {
            EffectUtil.sweepEffect(user, world, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), 0, effectCenter, -45.0, 45.0, 50, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        } else {
            EffectUtil.sweepEffect(user, world, new SquareParticleEffect(new Vector3f(red, green, blue), new Vector3f(red2, green2, blue2), 1F, 1, 0), 0.01, effectCenter, -45.0, 45.0, 100, swp * 0.7F, (user.getRandom().nextFloat() - 0.5) * 45.0, false);
        }

        Vec3 center = EntityUtil.getAttackTargetPosition(user, user.entityInteractionRange(), 2, 0.0);
        List<Entity> attackList = EntityUtil.getEntitiesInBox(world, user, center, new Vec3(swp, swp / 3.0F, swp));

        float cooldown = ((AdjustableAttackStrengthEntity) user).getLastAttackedTicks();
        for (Entity target : attackList) {
            if (target.isAttackable()) {
                ((AdjustableAttackStrengthEntity) user).setLastAttackedTicks((int) cooldown);
                applyElementDamage(user, target, stack);
                user.attack(target);
                getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
                    trait.onAttackEntity(user, world, stack, integer, getModifiedStats(user, world, stack), target);
                });
            }
        }
        if (!attackList.isEmpty()) {
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
        }

        user.resetAttackStrengthTicker();
    }


    public void applyElementDamage(Player attacker, Entity target, ItemStack stack) {
        ResourceKey<DamageType> damageType = getSumStats(attacker, attacker.level(), stack).getElement().getDamageType();
        if (damageType != null) {
            ToolStats stats = getSumStats(attacker, attacker.level(), stack);
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

            DamageSource ElementalDamageSource = stack.has(DataComponents.CUSTOM_NAME) ? attacker.damageSources().source(damageType, attacker) : attacker.damageSources().source(damageType);
            float damage = baseAttackDamage * EntityElementRegister.getElementAffinity(target, stats.getElement()).getMultiplier();
            if (target instanceof LivingEntity livingEntity) {
                float targetHealth = livingEntity.getHealth();
                livingEntity.setLastHurtByPlayer(attacker);
                target.hurt(ElementalDamageSource, damage);
                attacker.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingEntity.getHealth()) * 10));
            } else {
                Vec3 delta = target.getDeltaMovement();
                target.hurt(ElementalDamageSource, damage);
                target.setDeltaMovement(delta);
            }
            target.invulnerableTime = 0;
        }
    }
}
