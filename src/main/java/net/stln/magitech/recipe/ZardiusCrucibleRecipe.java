package net.stln.magitech.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.*;

public class ZardiusCrucibleRecipe implements Recipe<MultiStackRecipeInput> {

    protected final List<Ingredient> ingredients;
    protected final Optional<ItemStack> result;
    protected final FluidStack inputFluid;
    protected final Optional<FluidStack> outputFluid;
    protected final String group;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;

    public ZardiusCrucibleRecipe(String group, List<Ingredient> ingredients, FluidStack inputFluid, Optional<ItemStack> result, Optional<FluidStack> outputFluid) {
        this.type = RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get();
        this.serializer = RecipeInit.ZARDIUS_CRUCIBLE_SERIALIZER.get();
        this.ingredients = ingredients;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.group = group;
        this.result = result;
    }

    @Override
    public boolean matches(MultiStackRecipeInput input, Level level) {
        var nonEmptyItems = new java.util.ArrayList<ItemStack>(input.ingredientCount());
        for (var item : input.stacks())
            if (!item.isEmpty())
                nonEmptyItems.add(item);
        return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(MultiStackRecipeInput input, HolderLookup.Provider registries) {
        return result.orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ZARDIUS_CRUCIBLE_SERIALIZER.get();
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid.orElse(FluidStack.EMPTY);
    }

    public interface Factory<T extends ZardiusCrucibleRecipe> {
        T create(String group, List<Ingredient> ingredients, FluidStack inputFluid, Optional<ItemStack> result, Optional<FluidStack> outputFluid);
    }

    public static class Serializer<T extends ZardiusCrucibleRecipe> implements RecipeSerializer<T> {
        final ZardiusCrucibleRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(ZardiusCrucibleRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(p_300947_ -> p_300947_.ingredients),
                                    FluidStack.CODEC.fieldOf("input_fluid").forGetter(p_302316_ -> p_302316_.inputFluid),
                                    ItemStack.STRICT_CODEC.optionalFieldOf("result").forGetter(p_302316_ -> p_302316_.result),
                                    FluidStack.CODEC.optionalFieldOf("output_fluid").forGetter(p_302316_ -> p_302316_.outputFluid)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    r -> r.ingredients,
                    FluidStack.STREAM_CODEC,
                    r -> r.inputFluid,
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
                    r -> r.result,
                    FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
                    r -> r.outputFluid,
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