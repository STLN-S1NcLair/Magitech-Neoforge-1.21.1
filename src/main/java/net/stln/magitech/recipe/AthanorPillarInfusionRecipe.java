package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class AthanorPillarInfusionRecipe implements Recipe<GroupedMultiStackRecipeInput> {
    protected final List<List<Ingredient>> ingredients;
    protected final ItemStack result;
    protected final String group;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final ItemStack base;
    protected final int mana;

    public AthanorPillarInfusionRecipe(String group, ItemStack base, List<List<Ingredient>> ingredients, int mana, ItemStack result) {
        this.base = base;
        this.mana = mana;
        this.type = RecipeInit.ATHANOR_PILLAR_INFUSION_TYPE.get();
        this.serializer = RecipeInit.ATHANOR_PILLAR_INFUSION_SERIALIZER.get();
        this.ingredients = ingredients;
        this.group = group;
        this.result = result;
    }

    @Override
    public boolean matches(GroupedMultiStackRecipeInput input, Level level) {
        if (input.outerSize() != this.ingredients.size()) {
            return false;
        } else {
            for (int i = 0; i < input.outerSize(); i++) {
                List<ItemStack> group = input.stacks().get(i);
                if (i >= this.ingredients.size() || (group.size() != this.ingredients.get(i).size() && !this.ingredients.get(i).isEmpty())) {
                    return false;
                }
                var nonEmptyItems = new java.util.ArrayList<ItemStack>(input.ingredientCount());
                for (var item : group) {
                    if (!item.isEmpty()) {
                        nonEmptyItems.add(item);
                    }
                }
                List<Ingredient> nonEmptyIngredients = new ArrayList<>();
                for (var item : this.ingredients.get(i)) {
                    if (!item.isEmpty() && !item.getItems()[0].is(Items.AIR)) {
                        nonEmptyIngredients.add(item);
                    }
                }
                if (RecipeMatcher.findMatches(nonEmptyItems, nonEmptyIngredients) == null && (!nonEmptyItems.isEmpty() || !nonEmptyIngredients.isEmpty())) {
                    return false;
                }
            }
            return true;
        }
    }

    public ItemStack assemble(GroupedMultiStackRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> flatList = NonNullList.create();
        for (List<Ingredient> group : ingredients) {
            flatList.addAll(group);
        }
        return flatList;
    }

    public NonNullList<Ingredient> getInnerIngredients(int index) {
        return NonNullList.copyOf(this.ingredients.get(index));
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.ATHANOR_PILLAR_INFUSION_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ATHANOR_PILLAR_INFUSION_SERIALIZER.get();
    }

    public ItemStack getBase() {
        return base;
    }

    public int getMana() {
        return mana;
    }

    public interface Factory<T extends AthanorPillarInfusionRecipe> {
        T create(String group, ItemStack base, List<List<Ingredient>> ingredients, int mana, ItemStack result);
    }

    public static class Serializer<T extends AthanorPillarInfusionRecipe> implements RecipeSerializer<T> {
        final AthanorPillarInfusionRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(AthanorPillarInfusionRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    ItemStack.STRICT_CODEC.fieldOf("base").forGetter(p_300947_ -> p_300947_.base),
                                    Ingredient.LIST_CODEC.listOf().fieldOf("ingredients").forGetter(p_300947_ -> p_300947_.ingredients),
                                    Codec.INT.optionalFieldOf("mana", 0).forGetter(p_300947_ -> p_300947_.mana),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    ItemStack.STREAM_CODEC,
                    r -> r.base,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()),
                    r -> r.ingredients,
                    ByteBufCodecs.INT,
                    r -> r.mana,
                    ItemStack.STREAM_CODEC,
                    r -> r.result,
                    factory::create
            );
        }

        @Override
        public MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }
}
