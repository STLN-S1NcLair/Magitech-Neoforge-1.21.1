package net.stln.magitech.feature.magic.spell;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.input.SpellRecipeInput;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyKey;
import net.stln.magitech.helper.MathHelper;
import org.jetbrains.annotations.Nullable;

public class SpellHelper {

    public static void applyEffectToItem(Level level, ISpell spell, LivingEntity caster, ItemEntity item) {
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

    public static <T extends Number> MutableComponent createDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellPropertyKey<T> key, int roundDigit) {
        if (spell.getConfig().properties().contains(key)) {
            return key.getDisplayName().append(": " + MathHelper.round(MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, key, spell), roundDigit));
        }
        return Component.empty();
    }

    public static <T extends Number> MutableComponent createContinuousDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellPropertyKey<T> key) {
        return createDamagePropertyTooltip(spell, caster, wand, key, 2).append("/tick");
    }

    public static <T extends Number> MutableComponent createDamagePropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellPropertyKey<T> key) {
        return createDamagePropertyTooltip(spell, caster, wand, key, 2);
    }

    public static MutableComponent createPropertyTooltip(ISpell spell, LivingEntity caster, @Nullable ItemStack wand, SpellPropertyKey<Float> key, Holder<Attribute> coefficientAttribute) {
        if (spell.getConfig().properties().contains(key)) {
            return key.getDisplayName().append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveSpellProperty(caster, wand, spell, key, coefficientAttribute), 2));
        }
        return Component.empty();
    }

}
