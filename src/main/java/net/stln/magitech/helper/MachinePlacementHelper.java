package net.stln.magitech.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.jetbrains.annotations.NotNull;

public final class MachinePlacementHelper {
    private MachinePlacementHelper() {
    }

    public static @NotNull Direction getFacing(@NotNull BlockPlaceContext context, @NotNull Direction defaultFacing) {
        Player player = context.getPlayer();
        return player != null && player.isShiftKeyDown() ? defaultFacing : defaultFacing.getOpposite();
    }
}
