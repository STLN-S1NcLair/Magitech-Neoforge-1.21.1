package net.stln.magitech.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.magic.spell.SpellRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomThreadPageFunction extends LootItemConditionalFunction {

    public static final MapCodec<RandomThreadPageFunction> CODEC = RecordCodecBuilder.mapCodec(
            p_340803_ -> commonFields(p_340803_)
                    .and(Codec.STRING.listOf().fieldOf("spells").forGetter(RandomThreadPageFunction::getSpells))
                    .apply(p_340803_, RandomThreadPageFunction::new)
    );

    protected final List<String> spells;

    public RandomThreadPageFunction(List<LootItemCondition> lootItemConditions) {
        this(lootItemConditions, getIds());
    }

    private static @NotNull List<String> getIds() {
        List<String> ids = new ArrayList<>();
        SpellRegister.getRegister().keySet().stream().toList().forEach((resourceLocation -> {
            ids.add(resourceLocation.toString());
        }));
        return ids;
    }

    public List<String> getSpells() {
        return spells;
    }

    protected RandomThreadPageFunction(List<LootItemCondition> conditions, List<String> spells) {
        super(conditions);
        if (spells.isEmpty()) {
            this.spells = getIds();
        } else {
            this.spells = spells;
        }
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Collections.shuffle(spells);
        String spellId = spells.getFirst();
        stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(SpellRegister.getSpell(ResourceLocation.parse(spellId))));
        return stack;
    }

    public static LootItemFunction.Builder builder() {
        return simpleBuilder(RandomThreadPageFunction::new);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootFunctionInit.RANDOM_THREAD_PAGE.get();  // 登録済みである必要あり
    }
}