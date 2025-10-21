package net.stln.magitech.item.fluid;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.component.ComponentInit;

public class AlchemicalFlaskFluidHandler extends FluidHandlerItemStackSimple {

    public AlchemicalFlaskFluidHandler(ItemStack container, int capacity) {
        super(ComponentInit.FLUID_CONTENT_COMPONENT, container, capacity);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (container.getCount() < 1 || resource.isEmpty() || !canFillFluidType(resource)) {
            return 0;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty()) {
            int fillAmount = Math.min(capacity, resource.getAmount());
            if (fillAmount == capacity) {
                if (action.execute()) {
                    MagitechRegistries.FLUID_CONTAINER_MATCHER.stream()
                            .filter(matcher -> matcher.fillingMatches(container, resource))
                            .findFirst()
                            .ifPresent(matcher -> {
                                container = matcher.filledContainer().asItem().getDefaultInstance();
                            });
                }

                return fillAmount;
            }
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, getFluid())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    protected void setContainerToEmpty() {
        MagitechRegistries.FLUID_CONTAINER_MATCHER.stream()
                .filter(matcher -> matcher.emptyingMatches(container))
                .findFirst()
                .ifPresent(matcher -> {
                    container = matcher.emptyContainer().asItem().getDefaultInstance();
                });
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return MagitechRegistries.FLUID_CONTAINER_MATCHER.stream()
                .anyMatch(matcher -> matcher.fillingMatches(container, stack));
    }

    @Override
    public int getTankCapacity(int tank) {
        return super.getTankCapacity(tank);
    }
}
