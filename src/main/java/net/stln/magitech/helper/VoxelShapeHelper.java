package net.stln.magitech.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class VoxelShapeHelper {

    public static @NotNull VoxelShape rotateShape(@NotNull VoxelShape shape, @NotNull Direction from, @NotNull Direction to) {
        if (from == to) return shape;

        // parent → NORTH（逆回転）
        VoxelShape toNorth = rotateToNorth(shape, from);

        // NORTH → child（正回転）
        return rotateFromNorth(toNorth, to);
    }

    private static @NotNull VoxelShape rotateToNorth(@NotNull VoxelShape shape, @NotNull Direction from) {
        // parent から NORTH に戻す = parent の逆回転
        return rotateShapeInternal(shape, getInverseRotation(from));
    }

    private static @NotNull VoxelShape rotateFromNorth(@NotNull VoxelShape shape, @NotNull Direction to) {
        // NORTH から child に回す
        return rotateShapeInternal(shape, getModelRotation(to));
    }

    private static @NotNull VoxelShape rotateShapeInternal(@NotNull VoxelShape shape, @NotNull RotationAngles rotation) {
        List<AABB> rotatedBoxes = new ArrayList<>();
        for (AABB box : shape.toAabbs()) {
            List<Vec3> corners = getCorners(box);
            List<Vec3> transformed = new ArrayList<>();
            for (Vec3 point : corners) {
                transformed.add(applyRotation(point, rotation));
            }
            rotatedBoxes.add(buildAABB(transformed));
        }

        return rotatedBoxes.stream()
                .map(b -> Shapes.box(b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ))
                .reduce(Shapes.empty(), Shapes::or);
    }

    private static @NotNull Vec3 applyRotation(@NotNull Vec3 point, @NotNull RotationAngles rot) {
        double x = point.x - 0.5;
        double y = point.y - 0.5;
        double z = point.z - 0.5;

        // X軸回転
        for (int i = 0; i < (rot.x / 90); i++) {
            double ty = y;
            y = z;
            z = -ty;
        }

        // Y軸回転
        for (int i = 0; i < (rot.y / 90); i++) {
            double tx = x;
            x = -z;
            z = tx;
        }

        return new Vec3(x + 0.5, y + 0.5, z + 0.5);
    }

    private static @NotNull @Unmodifiable List<Vec3> getCorners(@NotNull AABB box) {
        return List.of(
                new Vec3(box.minX, box.minY, box.minZ),
                new Vec3(box.minX, box.minY, box.maxZ),
                new Vec3(box.minX, box.maxY, box.minZ),
                new Vec3(box.minX, box.maxY, box.maxZ),
                new Vec3(box.maxX, box.minY, box.minZ),
                new Vec3(box.maxX, box.minY, box.maxZ),
                new Vec3(box.maxX, box.maxY, box.minZ),
                new Vec3(box.maxX, box.maxY, box.maxZ)
        );
    }

    private static @NotNull AABB buildAABB(@NotNull List<Vec3> points) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Vec3 p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            minZ = Math.min(minZ, p.z);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
            maxZ = Math.max(maxZ, p.z);
        }

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static @NotNull RotationAngles getModelRotation(@NotNull Direction direction) {
        // based on your blockstate rotation settings
        return switch (direction) {
            case DOWN -> new RotationAngles(180, 0);
            case UP -> new RotationAngles(0, 0);
            case NORTH -> new RotationAngles(90, 0);
            case SOUTH -> new RotationAngles(90, 180);
            case WEST -> new RotationAngles(90, 270);
            case EAST -> new RotationAngles(90, 90);
        };
    }

    private static @NotNull RotationAngles getInverseRotation(@NotNull Direction direction) {
        RotationAngles rot = getModelRotation(direction);
        return new RotationAngles((360 - rot.x) % 360, (360 - rot.y) % 360);
    }

    private record RotationAngles(int x, int y) {
    }
}
