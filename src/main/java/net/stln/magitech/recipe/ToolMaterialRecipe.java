package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ToolMaterialRecipe extends SingleItemRecipe {

    protected ResourceLocation resultId;

    public ToolMaterialRecipe(String group, Ingredient ingredient, ResourceLocation resultId) {
        super(RecipeInit.TOOL_MATERIAL_TYPE.get(), RecipeInit.TOOL_MATERIAL_SERIALIZER.get(), group, ingredient, ItemStack.EMPTY);
        this.resultId = resultId;
    }

    public ResourceLocation getResultId() {
        return resultId;
    }

    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.STONECUTTER);
    }

    public interface Factory<T extends SingleItemRecipe> {
        T create(String group, Ingredient ingredient, ResourceLocation resultId);
    }

    public static class Serializer<T extends ToolMaterialRecipe> implements RecipeSerializer<T> {
        final ToolMaterialRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(ToolMaterialRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_301068_ -> p_301068_.ingredient),
                                    ResourceLocation.CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.resultId)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    p_319738_ -> p_319738_.ingredient,
                    ResourceLocation.STREAM_CODEC,
                    p_319736_ -> p_319736_.resultId,
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
