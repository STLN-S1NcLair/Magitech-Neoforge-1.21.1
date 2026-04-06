package net.stln.magitech.feature.tool.tool_type;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.property.ToolProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public record ToolType(float order, boolean heavyTool, ItemLike defaultItem, ToolMineType mineType, Function4<Player, ItemStack, BlockPos, Direction, Set<BlockPos>> additionalMine, Supplier<ToolProperties> defaultProperties, List<PartData> parts) implements ToolTypeLike {

    public ToolType(float order, boolean heavyTool, ItemLike defaultItem, ToolMineType mineType, Supplier<ToolProperties> defaultProperties, List<PartData> parts) {
        this(order, heavyTool, defaultItem, mineType, (player, stack, pos, face) -> Set.of(pos), defaultProperties, parts);
    }

    @Override
    public @NotNull ToolType asToolType() {
        return this;
    }

    public Item getDefaultItem() {
        return defaultItem.asItem();
    }

    public record PartData(ToolPartLike part, float weight) {

    }
}
