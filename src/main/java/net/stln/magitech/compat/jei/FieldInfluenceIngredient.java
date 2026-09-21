package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FieldInfluenceIngredient {
    public static final IIngredientType<FieldInfluence> TYPE = () -> FieldInfluence.class;
    public static final Codec<FieldInfluence> CODEC = FieldInfluence.CODEC;

    public static final IIngredientHelper<FieldInfluence> HELPER = new IIngredientHelper<>() {
        @Override
        public IIngredientType<FieldInfluence> getIngredientType() {
            return TYPE;
        }

        @Override
        public String getDisplayName(FieldInfluence ingredient) {
            return getTypeName(ingredient).getString();
        }

        @Override
        public String getUniqueId(FieldInfluence ingredient, UidContext context) {
            ResourceLocation id = getIngredientId(ingredient);
            return id == null ? "unknown" : id.toString();
        }

        @Override
        public ResourceLocation getResourceLocation(FieldInfluence ingredient) {
            return getIngredientId(ingredient);
        }

        @Override
        public FieldInfluence copyIngredient(FieldInfluence ingredient) {
            return new FieldInfluence(ingredient.type(), ingredient.intensity());
        }

        @Override
        public boolean isValidIngredient(FieldInfluence ingredient) {
            return ingredient != null && ingredient.type() != null && ingredient.intensity() > 0 && getTypeId(ingredient) != null;
        }

        @Override
        public String getErrorInfo(FieldInfluence ingredient) {
            return "Unknown field influence: " + ingredient;
        }
    };

    public static final IIngredientRenderer<FieldInfluence> RENDERER = new IIngredientRenderer<>() {
        @Override
        public void render(GuiGraphics guiGraphics, FieldInfluence ingredient) {
            FieldInfluenceType type = ingredient == null ? null : ingredient.type();
            ResourceLocation texture = type == null ? null : type.getIconTexture();
            if (texture != null && type != null) {
                FieldEffectIconRenderer.renderTexture(
                        guiGraphics,
                        texture,
                        0,
                        0,
                        16,
                        1.0F,
                        1.0F,
                        type.getPrimary(),
                        type.getSecondary()
                );
            } else {
                ResourceLocation influenceId = getTypeId(ingredient);
                int color = 0xFF000000 | (influenceId == null ? 0x777777 : influenceId.hashCode() & 0x7F7F7F);
                guiGraphics.fill(0, 0, 16, 16, color);
            }

            if (ingredient.intensity() > 1) {
                String count = Integer.toString(ingredient.intensity());
                guiGraphics.drawString(
                        Minecraft.getInstance().font,
                        count,
                        16 - Minecraft.getInstance().font.width(count),
                        8,
                        0xFFFFFF,
                        true
                );
            }
        }

        @Override
        public List<Component> getTooltip(FieldInfluence ingredient, TooltipFlag tooltipFlag) {
            return List.of(
                    getTypeName(ingredient),
                    Component.translatable("gui.magitech.field_influence").withColor(0x808080)
            );
        }
    };

    private FieldInfluenceIngredient() {
    }

    public static void register(IModIngredientRegistration registration) {
        Set<FieldInfluence> influences = new HashSet<>();
        for (var type : MagitechRegistries.FIELD_INFLUENCE_TYPE) {
            influences.add(new FieldInfluence(type, 1));
        }
        registration.register(TYPE, influences.stream().toList(), HELPER, RENDERER, CODEC);
    }

    public static FieldInfluence of(FieldInfluence influence) {
        return new FieldInfluence(influence.type(), influence.intensity());
    }

    private static ResourceLocation getTypeId(FieldInfluence influence) {
        return influence == null || influence.type() == null
                ? null
                : MagitechRegistries.FIELD_INFLUENCE_TYPE.getKey(influence.type());
    }

    private static ResourceLocation getIngredientId(FieldInfluence influence) {
        ResourceLocation typeId = getTypeId(influence);
        return typeId == null
                ? null
                : ResourceLocation.fromNamespaceAndPath(typeId.getNamespace(), "field_influence/" + typeId.getPath());
    }

    private static Component getTypeName(FieldInfluence influence) {
        ResourceLocation id = getTypeId(influence);
        return id == null
                ? Component.empty()
                : Component.translatable("field_influence." + id.getNamespace() + "." + id.getPath());
    }

}
