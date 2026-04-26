package net.stln.magitech.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FallbackFluidTank implements IFluidHandler {

    int maxTanks;
    int capacity;
    List<FluidTank> tanks = new ArrayList<>();
    protected Predicate<FluidStack> validator;

    public FallbackFluidTank(int maxTanks, int capacity) {
        this(maxTanks, capacity, stack -> true);
    }

    public FallbackFluidTank(int maxTanks, int capacity, Predicate<FluidStack> validator) {
        this.maxTanks = maxTanks;
        this.capacity = capacity;
        this.validator = validator;
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        if (getTanks() <= tank) {
            return FluidStack.EMPTY;
        }
        return tanks.get(tank).getFluid();
    }

    public FluidStack getLastFluid() {
        if (tanks.isEmpty()) {
            return FluidStack.EMPTY;
        }
        return tanks.get(tanks.size() - 1).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return validator.test(stack);
    }

    public boolean isEmpty() {
        return tanks.isEmpty() || tanks.stream().allMatch(t -> t.getFluid().isEmpty());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || resource.getAmount() <= 0 || !validator.test(resource)) {
            return 0;
        }

        // 既存の同種タンクがあれば、必ずそこを最優先で使う。
        FluidTank sameFluidTank = null;
        for (FluidTank tank : tanks) {
            FluidStack inTank = tank.getFluid();
            if (!inTank.isEmpty() && FluidStack.isSameFluidSameComponents(inTank, resource)) {
                sameFluidTank = tank;
                break;
            }
        }

        if (sameFluidTank != null) {
            // 同種タンクが満タンなら、新規作成や他種タンクへの投入はしない。
            if (sameFluidTank.getFluidAmount() >= capacity) {
                return 0;
            }
            onContentsChanged();
            return sameFluidTank.fill(resource, action);
        }

        // 同種がない場合のみ、空タンクの再利用を試す。
        FluidTank targetTank = null;
        for (FluidTank tank : tanks) {
            if (tank.getFluid().isEmpty()) {
                targetTank = tank;
                break;
            }
        }

        if (targetTank == null) {
            if (tanks.size() >= maxTanks) {
                return 0;
            }
            targetTank = new FluidTank(capacity, validator);
            // SIMULATE では一覧を変更せず、EXECUTE のときだけ実際に追加する。
            if (action.execute()) {
                tanks.add(targetTank);
            }
        }

        onContentsChanged();
        return targetTank.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || resource.getAmount() <= 0) {
            return FluidStack.EMPTY;
        }

        // 指定された流体と一致する最初のタンクからのみ排出する。
        for (FluidTank tank : tanks) {
            FluidStack inTank = tank.getFluid();
            if (!inTank.isEmpty() && FluidStack.isSameFluidSameComponents(inTank, resource)) {
                FluidStack drained = tank.drain(resource.getAmount(), action);
                // EXECUTE のときだけ空タンクを整理する。
                if (action.execute()) {
                    tanks.removeIf(t -> t.getFluid().isEmpty());
                }
                onContentsChanged();
                return drained;
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        // 流体種指定なしの場合は、先頭の非空タンクから排出する。
        for (FluidTank tank : tanks) {
            if (!tank.getFluid().isEmpty()) {
                FluidStack drained = tank.drain(maxDrain, action);
                if (action.execute()) {
                    tanks.removeIf(t -> t.getFluid().isEmpty());
                }
                onContentsChanged();
                return drained;
            }
        }

        return FluidStack.EMPTY;
    }

    public FallbackFluidTank load(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        this.tanks.clear();

        if (nbt.contains("Tanks", Tag.TAG_LIST)) {
            ListTag tankList = nbt.getList("Tanks", Tag.TAG_COMPOUND);
            int count = Math.min(tankList.size(), this.maxTanks);
            for (int i = 0; i < count; i++) {
                FluidTank tank = new FluidTank(this.capacity, this.validator);
                tank.readFromNBT(lookupProvider, tankList.getCompound(i));
                this.tanks.add(tank);
            }
        } else if (nbt.contains("Fluid", Tag.TAG_COMPOUND)) {
            // 旧形式(単一Fluid)の後方互換読み込み。
            FluidStack legacy = FluidStack.parseOptional(lookupProvider, nbt.getCompound("Fluid"));
            if (!legacy.isEmpty() && this.maxTanks > 0) {
                FluidTank tank = new FluidTank(this.capacity, this.validator);
                tank.setFluid(legacy.copyWithAmount(Math.min(legacy.getAmount(), this.capacity)));
                this.tanks.add(tank);
            }
        }

        return this;
    }

    public CompoundTag save(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        ListTag tankList = new ListTag();
        for (FluidTank tank : this.tanks) {
            tankList.add(tank.writeToNBT(lookupProvider, new CompoundTag()));
        }

        if (!tankList.isEmpty()) {
            nbt.put("Tanks", tankList);
        } else {
            nbt.remove("Tanks");
        }
        nbt.remove("Fluid");

        return nbt;
    }

    protected void onContentsChanged() {}
}
