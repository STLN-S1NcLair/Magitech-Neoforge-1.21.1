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
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.recipe.input.MultiStackRecipeInput;
import net.stln.magitech.util.ComponentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolAssemblyRecipe implements Recipe<MultiStackRecipeInput> {
    protected final List<Ingredient> ingredients;
    protected final ItemStack result;
    protected final String group;

    public ToolAssemblyRecipe(String group, List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.group = group;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull MultiStackRecipeInput input, @NotNull Level level) {
        if (!(result.getItem() instanceof PartToolItem)) {
            throw new IllegalArgumentException("the result item expected child be a PartToolItem");
        }
        ToolType type = ((PartToolItem) result.getItem()).getToolType();
        if (isCorrectTypesForTool(input, type).stream().allMatch(Objects::isNull)) {
            return false;
        }
        if (input.ingredientCount() != this.ingredients.size()) {
            return false;
        } else if (!ingredients.stream().allMatch(Ingredient::isSimple)) {
            var nonEmptyItems = new java.util.ArrayList<ItemStack>(input.ingredientCount());
            for (var item : input.stacks())
                if (!item.isEmpty())
                    nonEmptyItems.add(item);
            return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
        } else {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.getFirst().test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MultiStackRecipeInput input, HolderLookup.@NotNull Provider registries) {
        if (!(result.getItem() instanceof PartToolItem)) {
            throw new IllegalArgumentException("the result item expected child be a PartToolItem");
        }
        ToolType type = ((PartToolItem) result.getItem()).getToolType();
        List<ToolMaterial> toolMaterials = isCorrectTypesForTool(input, type);
        if (!toolMaterials.isEmpty()) {
            ItemStack stack = result.copy();
            stack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(toolMaterials));
            return stack;
        }
        return ItemStack.EMPTY;
    }

    private List<ToolMaterial> isCorrectTypesForTool(MultiStackRecipeInput input, ToolType type) {
        if (input.size() == type.getSize()) {
            boolean flag = true;
            List<ToolMaterial> result = new ArrayList<>(type.getSize());
            for (int i = 0; i < type.getSize(); i++) {
                result.add(null);
            }
            List<ToolPart> partList = new ArrayList<>();
            for (int i = 0; i < type.getSize(); i++) {
                partList.add(ToolMaterialRegister.getToolPartFromIndex(type, i));
            }
            for (int i = 0; i < input.size(); i++) {
                if (input.getItem(i).getItem() instanceof PartItem partItem && input.getItem(i).has(ComponentInit.MATERIAL_COMPONENT.get())) {
                    boolean found = partList.contains(partItem.getPart());
                    int index = partList.indexOf(partItem.getPart());
                    if (found) {
                        ComponentHelper.getMaterial(input.getItem(i)).ifPresent(material -> {
                            result.set(index, material);
                            partList.set(index, null);
                        });
                    }
                    flag &= found;
                }
            }
            if (flag && partList.stream().allMatch(Objects::isNull)) {
                return result;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.TOOL_ASSEMBLY_TYPE.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.TOOL_ASSEMBLY_SERIALIZER.get();
    }

    public interface Factory<T extends ToolAssemblyRecipe> {
        T create(String group, List<Ingredient> ingredients, ItemStack result);
    }

    public static class Serializer<T extends ToolAssemblyRecipe> implements RecipeSerializer<T> {
        final ToolAssemblyRecipe.Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(ToolAssemblyRecipe.Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(p_300947_ -> p_300947_.ingredients),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    r -> r.group,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    r -> r.ingredients,
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
