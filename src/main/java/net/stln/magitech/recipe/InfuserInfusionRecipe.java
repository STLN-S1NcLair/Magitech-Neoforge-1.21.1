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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;

public class InfuserInfusionRecipe implements Recipe<SingleRecipeInput> {
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final long mana;
    protected final String group;

    public InfuserInfusionRecipe(String group, Ingredient ingredient, long mana, ItemStack result) {
        this.mana = mana;
        this.ingredient = ingredient;
        this.group = group;
        this.result = result;
    }

    @Override
    public boolean matches(SingleRecipeInput input, @NotNull Level level) {
        return ingredient.test(input.getItem(0));
    }

    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.INFUSER_INFUSION_TYPE.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.INFUSER_INFUSION_SERIALIZER.get();
    }

    public long getMana() {
        return mana;
    }

    public interface Factory<T extends InfuserInfusionRecipe> {
        T create(String group, Ingredient ingredient, long mana, ItemStack result);
    }

    public static class Serializer<T extends InfuserInfusionRecipe> implements RecipeSerializer<T> {
        final InfuserInfusionRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(InfuserInfusionRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.CODEC.fieldOf("ingredient").forGetter(p_300947_ -> p_300947_.ingredient),
                                    Codec.LONG.optionalFieldOf("mana", 0L).forGetter(p_300947_ -> p_300947_.mana),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    r -> r.ingredient,
                    ByteBufCodecs.VAR_LONG,
                    r -> r.mana,
                    ItemStack.STREAM_CODEC,
                    r -> r.result,
                    factory::create
            );
        }

        @Override
        public @NotNull MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }
}
