package net.stln.magitech.item.tool.toolitem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.Trait;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.network.TraitTickPayload;
import net.stln.magitech.util.MathUtil;
import net.stln.magitech.util.TextUtil;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SpellCasterItem extends PartToolItem {
    ResourceLocation atkId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_damage_modifier");
    ResourceLocation elmatkId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_elemental_attack_damage_modifier");
    ResourceLocation spdId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_speed_modifier");
    ResourceLocation minId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_mining_speed_modifier");
    ResourceLocation defId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_defense_modifier");
    ResourceLocation rngId = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_tool_attack_range_modifier");

    public SpellCasterItem(Properties settings) {
        super(settings);
    }

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
    public ToolStats getSumStats(Player player, Level level, ItemStack stack) {
        return getModifiedStats(player, level, stack);
    }

    @Override
    public ToolStats getModifiedStats(Player player, Level level, ItemStack stack) {
        Map<Trait, Integer> traits = getTraitLevel(getTraits(stack));
        List<ToolStats> statsList = new ArrayList<>();
        statsList.add(getDefaultStats(stack));
        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats1(stack, value, getDefaultStats(stack)));
                statsList.add(trait.modifySpellCasterStatsConditional1(player, level, stack, value, getDefaultStats(stack)));
            }
        });
        ToolStats stats1 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats2(stack, value, stats1));
                statsList.add(trait.modifySpellCasterStatsConditional2(player, level, stack, value, stats1));
            }
        });
        ToolStats stats2 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats3(stack, value, stats2));
                statsList.add(trait.modifySpellCasterStatsConditional3(player, level, stack, value, stats2));
            }
        });
        return ToolStats.add(statsList);
    }

    @Override
    public ToolStats getSumStatsWithoutConditional(ItemStack stack) {
        return getModifiedStatsWithoutConditional(stack);
    }

    @Override
    public ToolStats getModifiedStatsWithoutConditional(ItemStack stack) {
        Map<Trait, Integer> traits = getTraitLevel(getTraits(stack));
        List<ToolStats> statsList = new ArrayList<>();
        statsList.add(getDefaultStats(stack));
        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats1(stack, value, getDefaultStats(stack)));
            }
        });
        ToolStats stats1 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats2(stack, value, stats1));
            }
        });
        ToolStats stats2 = ToolStats.add(statsList);

        traits.forEach((trait, value) -> {
            if (trait != null) {
                statsList.add(trait.modifySpellCasterStats3(stack, value, stats2));
            }
        });
        return ToolStats.add(statsList);
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
            ToolType toolType = ((SpellCasterItem) stack.getItem()).getToolType();

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
                component.append(Component.translatable("item.magitech." + ((SpellCasterItem) stack.getItem()).getToolType().get()));
            }

        } else {
            component.append(Component.translatable("item.magitech." + ((SpellCasterItem) stack.getItem()).getToolType().get()));
        }
        return component;
    }

    @Override
    public void reloadComponent(Player player, Level level, ItemStack stack) {

        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>();


        ToolStats finalStats = getSumStats(player, level, stack);
        Map<String, Float> map = finalStats.getStats();

        Map<String, Float> mod = ToolMaterialRegister.getModStats(this.getToolType()).getStats();
        EquipmentSlotGroup hand = player.getItemInHand(InteractionHand.OFF_HAND).equals(stack) && !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem) ? EquipmentSlotGroup.OFFHAND : EquipmentSlotGroup.MAINHAND;
        entries.add(new ItemAttributeModifiers.Entry(AttributeInit.SPELL_POWER, new AttributeModifier(atkId, map.get(ToolStats.ATK_STAT) - mod.get(ToolStats.ATK_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        if (finalStats.getElement() != Element.NONE) {
            DeferredHolder<Attribute, Attribute> elementAttribute = switch (finalStats.getElement()) {
                case NONE -> null;
                case EMBER -> AttributeInit.EMBER_SPELL_POWER;
                case GLACE -> AttributeInit.GLACE_SPELL_POWER;
                case SURGE -> AttributeInit.SURGE_SPELL_POWER;
                case PHANTOM -> AttributeInit.PHANTOM_SPELL_POWER;
                case TREMOR -> AttributeInit.TREMOR_SPELL_POWER;
                case MAGIC -> AttributeInit.MAGIC_SPELL_POWER;
                case FLOW -> AttributeInit.FLOW_SPELL_POWER;
                case HOLLOW -> AttributeInit.HOLLOW_SPELL_POWER;
            };
            entries.add(new ItemAttributeModifiers.Entry(elementAttribute, new AttributeModifier(elmatkId, map.get(ToolStats.ELM_ATK_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        }
        entries.add(new ItemAttributeModifiers.Entry(AttributeInit.CASTING_SPEED, new AttributeModifier(spdId, map.get(ToolStats.SPD_STAT) - mod.get(ToolStats.SPD_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        entries.add(new ItemAttributeModifiers.Entry(AttributeInit.COOLDOWN_SPEED, new AttributeModifier(minId, map.get(ToolStats.MIN_STAT) - mod.get(ToolStats.MIN_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.ARMOR, new AttributeModifier(defId, map.get(ToolStats.DEF_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        entries.add(new ItemAttributeModifiers.Entry(AttributeInit.PROJECTILE_SPEED, new AttributeModifier(rngId, map.get(ToolStats.RNG_STAT) - mod.get(ToolStats.RNG_STAT), AttributeModifier.Operation.ADD_VALUE), hand));
        entries.add(new ItemAttributeModifiers.Entry(AttributeInit.MANA_EFFICIENCY, new AttributeModifier(rngId, map.get(ToolStats.SWP_STAT) - mod.get(ToolStats.SWP_STAT), AttributeModifier.Operation.ADD_MULTIPLIED_BASE), hand));
        modifyTraitAttribute(player, level, stack, finalStats, entries);
        ItemAttributeModifiers component = new ItemAttributeModifiers(entries, false);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, component);

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

    protected void modifyTraitAttribute(Player player, Level level, ItemStack stack, ToolStats finalStats, List<ItemAttributeModifiers.Entry> entries) {
        getTraitLevel(getTraits(stack)).forEach((trait, value) -> {
            trait.modifySpellCasterAttribute(player, level, stack, value, finalStats, entries);
        });
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        ToolStats stats = getSumStatsWithoutConditional(stack);
        return isCorrectTool(stack, state, (SpellCasterItem) stack.getItem(), stats);
    }

    @Override
    protected void addStatsHoverText(@NotNull ItemStack stack, List<Component> tooltipComponents) {
        ToolStats finalStats = getSumStatsWithoutConditional(stack);
        Map<String, Float> mod = ToolMaterialRegister.getModStats(this.getToolType()).getStats();

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("attribute.magitech.spell_power").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.ATK_STAT) - mod.get(ToolStats.ATK_STAT))
                ).withColor(0xFF4040)));

        tooltipComponents.add(Component.translatable("attribute.magitech.elemental_spell_power").append(": ").withColor(0xa0a0a0)
                .append(Component.translatable("element.magitech." + finalStats.getElement().get())
                        .append(" ")
                        .append(Component.literal(
                                TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.ELM_ATK_STAT))
                        )).withColor(finalStats.getElement().getColor())));

        tooltipComponents.add(Component.translatable("attribute.magitech.casting_speed").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.SPD_STAT) - mod.get(ToolStats.SPD_STAT))
                ).withColor(0x40FFC0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.cooldown_speed").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.MIN_STAT) - mod.get(ToolStats.MIN_STAT))
                ).withColor(0x4080C0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.defense").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(String.valueOf(
                        MathUtil.round(finalStats.getStats().get(ToolStats.DEF_STAT), 2)
                )).withColor(0xA0C0C0)));

        tooltipComponents.add(Component.translatable("attribute.magitech.projectile_speed").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.RNG_STAT) - mod.get(ToolStats.RNG_STAT))
                ).withColor(0x80c0FF)));

        tooltipComponents.add(Component.translatable("attribute.magitech.mana_efficiency").append(": ").withColor(0xa0a0a0)
                .append(Component.literal(
                        TextUtil.toSignedIntPercent(finalStats.getStats().get(ToolStats.SWP_STAT) - mod.get(ToolStats.SWP_STAT))
                ).withColor(0xFFFF80)));

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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        if (!threadbound.isEmpty()) {
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            if (spellComponent.selected() < spellComponent.spells().size()) {
                Spell spell = spellComponent.spells().get(spellComponent.selected());
                if (CooldownData.getPrevCooldown(player, spell) == null && spell.isActiveUse(level, player, usedHand, true)) {
                    boolean flag;
                    if (spell.needsUseCost(level, player, itemStack)) {
                        if (ManaUtil.checkMana(player, spell.getRequiredMana(level, player, itemStack))) {
                            flag = ManaUtil.useManaServerOnly(player, spell.getCost(level, player, itemStack)) || player.isCreative();
                        } else {
                            flag = player.isCreative();
                        }
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        spell.use(level, player, usedHand, true);
                        getTraitLevel(getTraits(itemStack)).forEach((trait, integer) -> {
                            trait.onCastSpell(player, level, itemStack, integer, getModifiedStats(player, level, itemStack));
                        });
                    } else {
                        player.releaseUsingItem();
                        return InteractionResultHolder.consume(itemStack);
                    }
                } else {
                    player.releaseUsingItem();
                }
            } else {
                threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), 0));
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(itemStack);

//        ItemStack stack = player.getItemInHand(usedHand);
//        if (level.isClientSide) {
//            PacketDistributor.sendToServer(new UsePayload(usedHand == InteractionHand.MAIN_HAND, player.getUUID().toString()));
//        } else {
//            PacketDistributor.sendToAllPlayers(new UsePayload(usedHand == InteractionHand.MAIN_HAND, player.getUUID().toString()));
//        }
//        final InteractionResultHolder[] result = {InteractionResultHolder.pass(stack)};
//        getTraitLevel(getTraits(stack)).forEach((trait, integer) -> {
//            if (trait.use(player, level, stack, integer, getSumStats(player, level, stack), usedHand) != InteractionResult.PASS) {
//                result[0] = InteractionResultHolder.success(stack);
//            }
//        });
//        return result[0];
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
                if (CooldownData.getCurrentCooldown(user, spell) == null && spell.isActiveUsingTick(level, livingEntity, stack, getUseDuration(stack, livingEntity) - remainingUseDuration)) {
                    boolean flag;
                    if (spell.needsTickCost(level, user, stack)) {
                        flag = ManaUtil.useManaServerOnly(user, spell.getTickCost(level, user, stack)) || user.isCreative();
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        spell.usingTick(level, livingEntity, stack, getUseDuration(stack, livingEntity) - remainingUseDuration);
                    } else {
                        user.releaseUsingItem();
                    }
                } else {
                    livingEntity.releaseUsingItem();
                }
            } else {
                livingEntity.releaseUsingItem();
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
        return InteractionResult.PASS;
    }
}
