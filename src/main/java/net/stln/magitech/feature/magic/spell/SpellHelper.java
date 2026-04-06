package net.stln.magitech.feature.magic.spell;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.input.SpellRecipeInput;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.property.SpellProperty;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class SpellHelper {

    public static void applyEffectToItem(Level level, ISpell spell, ItemEntity item) {
        var recipeInput = new SpellRecipeInput(item.getItem(), spell);
        level.getRecipeManager().getRecipeFor(RecipeInit.SPELL_CONVERSION_TYPE.get(), recipeInput, level).map(RecipeHolder::value).ifPresent(recipe -> {
            ItemStack stack = recipe.assemble(recipeInput, level.registryAccess());
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
        });
    }

    public static <T extends Number> MutableComponent createDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key, int roundDigit) {
        if (spell.getConfig().properties().contains(key)) {
            return createTooltip(MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, key, spell), key, roundDigit);
        }
        return Component.empty();
    }

    public static <T extends Number> MutableComponent createContinuousDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key) {
        return createDamagePropertyTooltip(spell, caster, wand, key, 2).append("/tick");
    }

    public static <T extends Number> MutableComponent createDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key) {
        return createDamagePropertyTooltip(spell, caster, wand, key, 2);
    }

    public static <T extends Number> MutableComponent createProjectileSpeedPropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key) {
        return createPropertyTooltip(spell, caster, wand, key, AttributeInit.LAUNCH);
    }

    public static <T extends Number> MutableComponent createRangePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key) {
        return createPropertyTooltip(spell, caster, wand, key, AttributeInit.LAUNCH).append("m");
    }

    public static <T extends Number> MutableComponent createDurationPropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key) {
        if (spell.getConfig().properties().contains(key)) {
            return createTooltip(MagicPerformanceHelper.getEffectiveSpellProperty(caster, wand, spell, key, AttributeInit.LAUNCH) / 20, key, 2).append("s");
        }
        return Component.empty();
    }

    public static <T extends Number> MutableComponent createPropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key, Holder<Attribute> coefficientAttribute) {
        if (spell.getConfig().properties().contains(key)) {
            return createTooltip(MagicPerformanceHelper.getEffectiveSpellProperty(caster, wand, spell, key, coefficientAttribute), key, 2);
        }
        return Component.empty();
    }

    public static <T extends Number> MutableComponent createBasicPropertyValueTooltip(ISpell spell, SpellProperty<T> key, int roundDigit) {
        if (spell.getConfig().properties().contains(key)) {
            return createTooltip(spell.getConfig().properties().get(key).floatValue(), key, roundDigit);
        }
        return Component.empty();
    }

    public static <T extends Number> MutableComponent createBasicPropertyValueTooltip(ISpell spell, SpellProperty<T> key) {
        return createBasicPropertyValueTooltip(spell, key, 2);
    }

    public static <T extends Number> MutableComponent createTooltip(float value, SpellProperty<T> key, int roundDigit) {
        return key.getDisplayName().append(": " + MathHelper.round(value, roundDigit));
    }

    // 処理系

    public static @NotNull Set<Entity> getTargets(Level level, LivingEntity caster) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 center = caster.getEyePosition().add(forward);
        Vec3 center2 = center.add(forward.scale(3));
        Set<Entity> attackList = new HashSet<>();
        attackList.addAll(CombatHelper.getEntitiesInBox(level, caster, center, new Vec3(3.0, 3.0, 3.0)));
        attackList.addAll(CombatHelper.getEntitiesInBox(level, caster, center2, new Vec3(4.0, 4.0, 4.0)));
        return attackList;
    }

    public static @NotNull Set<Entity> getChainTargets(Level level, Entity entity, Vec3 center, float radius) {
        return new HashSet<>(CombatHelper.getEntitiesInBox(level, entity, center, new Vec3(radius, radius, radius)));
    }

    public static @NotNull Set<Entity> getChainTargets(Level level, Entity entity, float radius) {
        Vec3 center = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
        return new HashSet<>(CombatHelper.getEntitiesInBox(level, entity, center, new Vec3(radius, radius, radius)));
    }

    public static boolean canSee(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        return level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster)).getType() != HitResult.Type.BLOCK;
    }

}
