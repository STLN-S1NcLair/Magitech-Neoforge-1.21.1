package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.ItemInit;

public class SpellConversionRecipe implements Recipe<SingleRecipeInput> {
    protected final Ingredient ingredient;
    protected final ResourceLocation spell;
    protected final ItemStack result;
    protected final String group;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;

    public SpellConversionRecipe(String group, Ingredient ingredient, ResourceLocation spell, ItemStack result) {
        this.type = RecipeInit.SPELL_CONVERSION_TYPE.get();
        this.serializer = RecipeInit.SPELL_CONVERSION_SERIALIZER.get();
        this.group = group;
        this.ingredient = ingredient;
        this.spell = spell;
        this.result = result;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ResourceLocation getSpell() {
        return spell;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }


    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemInit.FLUORITE.get());
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public interface Factory<T extends SpellConversionRecipe> {
        T create(String group, Ingredient ingredient, ResourceLocation spell, ItemStack result);
    }

    public static class Serializer<T extends SpellConversionRecipe> implements RecipeSerializer<T> {
        final Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_301068_ -> p_301068_.ingredient),
                                    ResourceLocation.CODEC.fieldOf("spell").forGetter(p_301068_ -> p_301068_.spell),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    p_319737_ -> p_319737_.ingredient,
                    ResourceLocation.STREAM_CODEC,
                    p_319737_ -> p_319737_.spell,
                    ItemStack.STREAM_CODEC,
                    p_319736_ -> p_319736_.result,
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
