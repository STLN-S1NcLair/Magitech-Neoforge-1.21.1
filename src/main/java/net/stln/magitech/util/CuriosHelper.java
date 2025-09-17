package net.stln.magitech.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

public class CuriosHelper {
    private static @NotNull Optional<ICuriosItemHandler> getOptionalCuriosInventory(@Nullable LivingEntity entity) {
        return entity == null ? Optional.empty() : CuriosApi.getCuriosInventory(entity);
    }

    private static @NotNull Optional<ICurioStacksHandler> getThreadBoundSlot(@Nullable LivingEntity entity) {
        return getOptionalCuriosInventory(entity).map(ICuriosItemHandler::getCurios).flatMap(map -> Optional.ofNullable(map.get("threadbound")));
    }

    public static @NotNull Optional<ItemStack> getThreadBoundStack(@Nullable LivingEntity entity) {
        if (!ModList.get().isLoaded("curios")) return Optional.empty();
        return getThreadBoundSlot(entity)
                .map(ICurioStacksHandler::getStacks)
                .map(iDynamicStackHandler -> iDynamicStackHandler.getStackInSlot(0))
                .filter(stack -> !stack.isEmpty());
    }
}
