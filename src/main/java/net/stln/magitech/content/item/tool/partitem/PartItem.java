package net.stln.magitech.content.item.tool.partitem;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyHelper;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.feature.tool.trait.TraitHelper;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public abstract class PartItem extends Item {

    private final ToolPartLike partLike;

    public PartItem(Properties settings, ToolPartLike partLike) {
        super(settings);
        this.partLike = partLike;
    }

    public static @NotNull ToolProperties getDefaultStats(@NotNull ItemStack stack) {
        return ComponentHelper.getMaterial(stack).map(ToolMaterial::properties).orElse(new ToolProperties(ToolCategoryInit.NONE));
    }

    public static @NotNull Optional<Trait> getTrait(@NotNull ItemStack stack) {
        return ComponentHelper.getMaterial(stack).map(ToolMaterial::trait);
    }

    public ToolPart getPart() {
        return partLike.asToolPart();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return ComponentHelper.getMaterial(stack)
                .map(ToolMaterial::getId)
                .map(id -> Component.translatable("item." + id.getNamespace() + "." + getPart(), Component.translatable("material.magitech." + id.getPath())))
                .orElseGet(() -> super.getName(stack).copy());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        addPropertiesHoverText(stack, tooltipComponents);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public void addPropertiesHoverText(@NotNull ItemStack stack, List<Component> tooltipComponents) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        ToolProperties properties = ComponentHelper.getMaterial(stack).get().properties();

        tooltipComponents.add(Component.empty());

        for (IToolProperty<?> property : properties.getValues().keySet()) {
            property.addPartTooltip(stack, properties, tooltipComponents);
        }

        tooltipComponents.add(Component.empty());

        TraitHelper.getTrait(stack).forEach(((instance) -> {
            tooltipComponents.add(TraitHelper.getTooltip(instance));
        }
        ));
    }
}
