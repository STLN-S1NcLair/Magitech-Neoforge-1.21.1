package net.stln.magitech.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.util.ComponentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RandomThreadPageFunction extends LootItemConditionalFunction {

    public static final MapCodec<RandomThreadPageFunction> CODEC = RecordCodecBuilder.mapCodec(
            p_340803_ -> commonFields(p_340803_)
                    .and(ResourceLocation.CODEC.listOf().fieldOf("spells").forGetter(RandomThreadPageFunction::getSpells))
                    .apply(p_340803_, RandomThreadPageFunction::new)
    );

    protected final List<ResourceLocation> spells;

    private static @NotNull List<ResourceLocation> getAllSpellIds() {
        return SpellRegister.getSpellIds().stream().toList();
    }
    
    public RandomThreadPageFunction(List<LootItemCondition> lootItemConditions) {
        this(lootItemConditions, getAllSpellIds());
    }

    protected RandomThreadPageFunction(List<LootItemCondition> conditions, List<ResourceLocation> spells) {
        super(conditions);
        if (spells.isEmpty()) {
            this.spells = getAllSpellIds();
        } else {
            this.spells = spells;
        }
    }

    public List<ResourceLocation> getSpells() {
        return spells;
    }

    @Override
    protected @NotNull ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context) {
        Collections.shuffle(spells);
        ResourceLocation spellId = spells.getFirst();
        ComponentHelper.setThreadPage(stack, spellId);
        ;
        return stack;
    }

    public static LootItemFunction.Builder builder() {
        return simpleBuilder(RandomThreadPageFunction::new);
    }

    @Override
    public @NotNull LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return LootFunctionInit.RANDOM_THREAD_PAGE.get();  // 登録済みである必要あり
    }
}
