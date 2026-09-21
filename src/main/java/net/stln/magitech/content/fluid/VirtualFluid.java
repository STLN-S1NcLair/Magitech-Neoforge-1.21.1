package net.stln.magitech.content.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class VirtualFluid extends Fluid {
    private final @NotNull Supplier<? extends FluidType> typeGetter;
    private final @Nullable Supplier<? extends Item> bucketGetter;

    public VirtualFluid(@NotNull Supplier<? extends FluidType> typeGetter, @Nullable Supplier<? extends Item> bucketGetter) {
        this.typeGetter = typeGetter;
        this.bucketGetter = bucketGetter;
    }

    @Override
    public @NotNull Item getBucket() {
        return bucketGetter == null ? Items.AIR : bucketGetter.get();
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return true;
    }

    @Override
    protected @NotNull Vec3 getFlow(@NotNull BlockGetter blockReader, @NotNull BlockPos pos, @NotNull FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    public float getHeight(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 0;
    }

    @Override
    public float getOwnHeight(@NotNull FluidState state) {
        return 0;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return true;
    }

    @Override
    public int getAmount(@NotNull FluidState state) {
        return 0;
    }

    @Override
    public boolean isSame(@NotNull Fluid fluid) {
        return Objects.equals(this, fluid);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return typeGetter.get();
    }

    @Override
    public @NotNull Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(getFluidType().getSound(SoundActions.BUCKET_FILL));
    }
}
