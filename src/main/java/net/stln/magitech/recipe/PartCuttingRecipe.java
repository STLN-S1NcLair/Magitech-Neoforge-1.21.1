package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

public class PartCuttingRecipe implements Recipe<SingleRecipeInput> {
    protected final int count;
    protected final ItemStack result;
    protected final String group;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;

    public PartCuttingRecipe(String group, int count, ItemStack result) {
        this.type = RecipeInit.PART_CUTTING_TYPE.get();
        this.serializer = RecipeInit.PART_CUTTING_SERIALIZER.get();
        this.group = group;
        this.count = count;
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

    public int getCount() {
        return this.count;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }


    public boolean matches(SingleRecipeInput input, Level level) {
        return !level.getRecipeManager().getRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get(), input, level).isEmpty() && input.item().getCount() >= count;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.ENGINEERING_WORKBENCH.get());
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        ItemStack itemStack = this.result.copy();
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Level level;
        if (server != null) {
            level = server.getLevel(Level.OVERWORLD);
        } else {
            level = getClientLevel();
        }
        ToolMaterial material = level != null ? ToolMaterialRegister.getMaterial(level.getRecipeManager().getRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get(), input, level).getFirst().value().getResultId()) : null;
        if (material != null) {
            itemStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
        }
        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    private Level getClientLevel() {
        return Minecraft.getInstance().level;
    }

    public interface Factory<T extends PartCuttingRecipe> {
        T create(String group, int count, ItemStack result);
    }

    public static class Serializer<T extends PartCuttingRecipe> implements RecipeSerializer<T> {
        final Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300947_ -> p_300947_.group),
                                    Codec.INT.optionalFieldOf("input_count", 1).forGetter(p_300947_ -> p_300947_.count),
                                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_302316_ -> p_302316_.result)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    p_319737_ -> p_319737_.group,
                    ByteBufCodecs.INT,
                    p_319737_ -> p_319737_.count,
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
