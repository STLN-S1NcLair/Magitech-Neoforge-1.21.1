package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.tool.material.ToolMaterial;
import org.jetbrains.annotations.NotNull;

public class ToolMaterialRecipe extends SingleItemRecipe {

    protected final ToolMaterial toolMaterial;

    public ToolMaterialRecipe(String group, Ingredient ingredient, ToolMaterial toolMaterial) {
        super(RecipeInit.TOOL_MATERIAL_TYPE.get(), RecipeInit.TOOL_MATERIAL_SERIALIZER.get(), group, ingredient, ItemStack.EMPTY);
        this.toolMaterial = toolMaterial;
    }

    public ToolMaterial getToolMaterial() {
        return toolMaterial;
    }

    public boolean matches(SingleRecipeInput input, @NotNull Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return BlockInit.ENGINEERING_WORKBENCH.toStack();
    }

    public interface Factory<T extends SingleItemRecipe> {
        T create(String group, Ingredient ingredient, ToolMaterial toolMaterial);
    }

    public static class Serializer<T extends ToolMaterialRecipe> implements RecipeSerializer<T> {
        final ToolMaterialRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_301068_ -> p_301068_.ingredient),
                                    ToolMaterial.CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.toolMaterial)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    p_319738_ -> p_319738_.ingredient,
                    ToolMaterial.STREAM_CODEC,
                    p_319736_ -> p_319736_.toolMaterial,
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
