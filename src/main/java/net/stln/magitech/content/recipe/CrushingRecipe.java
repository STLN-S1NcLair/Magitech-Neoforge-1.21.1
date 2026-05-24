package net.stln.magitech.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.content.recipe.input.BaseAndIngredientsRecipeInput;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.systems.recipe.LodestoneInWorldRecipe;

import java.util.List;

public class CrushingRecipe extends LodestoneInWorldRecipe<SingleRecipeInput> {
    protected final SizedIngredient ingredient;
    protected final String group;

    public CrushingRecipe(String group, SizedIngredient ingredient, ItemStack result) {
        super(RecipeInit.CRUSHING_SERIALIZER.get(), RecipeInit.CRUSHING_TYPE.get(), result);
        this.ingredient = ingredient;
        this.group = group;
    }

    @Override
    public boolean matches(SingleRecipeInput input, @NotNull Level level) {
        return ingredient.test(input.item());
    }

    public SizedIngredient getSizedIngredient() {
        return ingredient;
    }

    public interface Factory<T extends CrushingRecipe> {
        T create(String group, SizedIngredient ingredient, ItemStack result);
    }

    public static class Serializer<T extends CrushingRecipe> implements RecipeSerializer<T> {
        final CrushingRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(CrushingRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(p_300947_ -> p_300947_.ingredient),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.output)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    SizedIngredient.STREAM_CODEC,
                    r -> r.ingredient,
                    ItemStack.STREAM_CODEC,
                    r -> r.output,
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
