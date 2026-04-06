package net.stln.magitech.content.item.tool.partitem;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.registry.RegistryHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class PartItem extends Item {

    private final ToolPartLike partLike;

    public PartItem(Properties settings, ToolPartLike partLike) {
        super(settings);
        this.partLike = partLike;
    }

    public static @NotNull ToolProperties getDefaultStats(@NotNull ItemStack stack) {
        return ComponentHelper.getMaterial(stack).map(ToolMaterial::properties).map(Supplier::get).orElse(new ToolProperties(ToolCategoryInit.NONE));
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
                .map(id -> Component.translatable("item." + id.getNamespace() + "." + MagitechRegistries.TOOL_PART.getKey(getPart()).getPath(), Component.translatable("material.magitech." + id.getPath())))
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
        ToolMaterial material = ComponentHelper.getMaterial(stack).get();
        Trait trait = material.trait();

        if (Screen.hasShiftDown()) {
            ToolProperties properties = material.properties().get();

            for (IToolProperty<?> property : RegistryHelper.registeredToolProperties()) {
                if (properties.getValues().containsKey(property)) {
                    property.addPartTooltip(stack, properties, tooltipComponents);
                }
            }

            tooltipComponents.add(Component.empty());
            tooltipComponents.add(trait.getComponent());

        } else if (Screen.hasControlDown()) {

            tooltipComponents.add(trait.getComponent());
            trait.addDescription(tooltipComponents);

        } else {
            tooltipComponents.add(Component.translatable("tooltip.magitech.part.shift").withColor(0x808080));
            tooltipComponents.add(Component.translatable("tooltip.magitech.tool.ctrl").withColor(0x808080));

            tooltipComponents.add(Component.empty());
            tooltipComponents.add(trait.getComponent());
        }
    }
}
