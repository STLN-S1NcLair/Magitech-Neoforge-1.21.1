package net.stln.magitech.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.stln.magitech.item.tool.ToolBeltItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

public class CuriosHelper {
    private static @NotNull Optional<ICuriosItemHandler> getOptionalCuriosInventory(@Nullable LivingEntity entity) {
        return entity == null ? Optional.empty() : CuriosApi.getCuriosInventory(entity);
    }

    private static @NotNull Optional<ICurioStacksHandler> getThreadBoundSlot(@Nullable LivingEntity entity) {
        return getOptionalCuriosInventory(entity).map(ICuriosItemHandler::getCurios).flatMap(map -> Optional.ofNullable(map.get("threadbound")));
    }

    private static @NotNull Optional<ICurioStacksHandler> getBeltSlot(@Nullable LivingEntity entity) {
        return getOptionalCuriosInventory(entity).map(ICuriosItemHandler::getCurios).flatMap(map -> Optional.ofNullable(map.get("belt")));
    }

    public static @NotNull Optional<ItemStack> getThreadBoundStack(@Nullable LivingEntity entity) {
        if (!ModList.get().isLoaded("curios")) return Optional.empty();
        return getThreadBoundSlot(entity)
                .map(ICurioStacksHandler::getStacks)
                .map(iDynamicStackHandler -> iDynamicStackHandler.getStackInSlot(0))
                .filter(stack -> !stack.isEmpty());
    }

    public static @NotNull Optional<ItemStack> getToolBeltStack(@Nullable LivingEntity entity) {
        if (!ModList.get().isLoaded("curios")) return Optional.empty();
        return getBeltSlot(entity)
                .map(ICurioStacksHandler::getStacks)
                .map(stacks -> {
                    for (int i = 0; i < stacks.getSlots(); i++) {
                        if (stacks.getStackInSlot(i).getItem() instanceof ToolBeltItem) {
                            return stacks.getStackInSlot(i);
                        }
                    }
                    return ItemStack.EMPTY;
                })
                .filter(stack -> !stack.isEmpty());
    }
}
