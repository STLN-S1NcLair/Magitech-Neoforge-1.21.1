package net.stln.magitech.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.content.recipe.input.BaseAndIngredientsRecipeInput;
import net.stln.magitech.content.recipe.input.MultiStackRecipeInput;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.systems.recipe.LodestoneInWorldRecipe;

import java.util.List;

public class InfusionRecipe extends LodestoneInWorldRecipe<BaseAndIngredientsRecipeInput> {
    protected final SizedIngredient base;
    protected final List<SizedIngredient> ingredients;
    protected final long mana;
    protected final String group;

    public InfusionRecipe(String group, SizedIngredient base, List<SizedIngredient> ingredients, long mana, ItemStack result) {
        super(RecipeInit.INFUSION_SERIALIZER.get(), RecipeInit.INFUSION_TYPE.get(), result);
        this.ingredients = ingredients;
        this.mana = mana;
        this.base = base;
        this.group = group;
    }

    @Override
    public boolean matches(BaseAndIngredientsRecipeInput input, @NotNull Level level) {
        if (input.size() != ingredients.size()) return false;
        if (input.base() == null || !base.test(input.base())) return false;
        return input.test(this.base, this.ingredients);
    }

    public SizedIngredient getBase() {
        return base;
    }

    public List<SizedIngredient> getSizedIngredients() {
        return ingredients;
    }

    public long getMana() {
        return mana;
    }

    public interface Factory<T extends InfusionRecipe> {
        T create(String group, SizedIngredient base, List<SizedIngredient> ingredients, long mana, ItemStack result);
    }

    public static class Serializer<T extends InfusionRecipe> implements RecipeSerializer<T> {
        final InfusionRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(InfusionRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    SizedIngredient.NESTED_CODEC.fieldOf("base").forGetter(p_300947_ -> p_300947_.base),
                                    SizedIngredient.NESTED_CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(p_300947_ -> p_300947_.ingredients),
                                    Codec.LONG.optionalFieldOf("mana", 0L).forGetter(p_300947_ -> p_300947_.mana),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.output)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    SizedIngredient.STREAM_CODEC,
                    r -> r.base,
                    SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    r -> r.ingredients,
                    ByteBufCodecs.VAR_LONG,
                    r -> r.mana,
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
