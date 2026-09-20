package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;

import java.util.List;
import java.util.Objects;

/**
 * JEIでフィールド効果をアイテム風の材料として扱うingredientです。
 * A JEI ingredient that represents a field effect as an item-like material.
 */
public record FieldEffectIngredient(ResourceLocation id) {
    public static final IIngredientType<FieldEffectIngredient> TYPE =
            () -> FieldEffectIngredient.class;
    public static final Codec<FieldEffectIngredient> CODEC = ResourceLocation.CODEC.xmap(
            FieldEffectIngredient::new,
            FieldEffectIngredient::id
    );

    public static final IIngredientHelper<FieldEffectIngredient> HELPER = new IIngredientHelper<>() {
        @Override
        public IIngredientType<FieldEffectIngredient> getIngredientType() {
            return TYPE;
        }

        @Override
        public String getDisplayName(FieldEffectIngredient ingredient) {
            return FieldEffectIconRenderer.getDisplayName(ingredient.id()).getString();
        }

        @Override
        public String getUniqueId(FieldEffectIngredient ingredient, UidContext context) {
            return ingredient.id().toString();
        }

        @Override
        public ResourceLocation getResourceLocation(FieldEffectIngredient ingredient) {
            return ingredient.id();
        }

        @Override
        public FieldEffectIngredient copyIngredient(FieldEffectIngredient ingredient) {
            return new FieldEffectIngredient(ingredient.id());
        }

        @Override
        public boolean isValidIngredient(FieldEffectIngredient ingredient) {
            return MagitechRegistries.FIELD_EFFECT_TYPE.get(ingredient.id()) != null;
        }

        @Override
        public String getErrorInfo(FieldEffectIngredient ingredient) {
            return "Unknown field effect: " + ingredient.id();
        }
    };

    public static final IIngredientRenderer<FieldEffectIngredient> RENDERER = new IIngredientRenderer<>() {
        @Override
        public void render(GuiGraphics guiGraphics, FieldEffectIngredient ingredient) {
            FieldEffectIconRenderer.render(guiGraphics, ingredient.id(), 0, 0, 16, 1.0F, 1.0F);
        }

        @Override
        public List<Component> getTooltip(FieldEffectIngredient ingredient, TooltipFlag tooltipFlag) {
            return List.of(
                    FieldEffectIconRenderer.getDisplayName(ingredient.id()),
                    Component.translatable("gui.magitech.field_effect").withColor(0x808080)
            );
        }
    };

    public FieldEffectIngredient {
        Objects.requireNonNull(id, "Field effect id");
    }

    /**
     * 登録済みの全フィールド効果をJEIへ登録します。
     * Registers all registered field effects with JEI.
     */
    public static void register(IModIngredientRegistration registration) {
        List<FieldEffectIngredient> ingredients = MagitechRegistries.FIELD_EFFECT_TYPE.keySet().stream()
                .map(FieldEffectIngredient::new)
                .toList();
        registration.register(TYPE, ingredients, HELPER, RENDERER, CODEC);
    }

    /**
     * IDからレシピスロット用のフィールド効果ingredientを作成します。
     * Creates a field-effect ingredient for use in a recipe slot from its ID.
     */
    public static FieldEffectIngredient of(ResourceLocation id) {
        return new FieldEffectIngredient(id);
    }
}
