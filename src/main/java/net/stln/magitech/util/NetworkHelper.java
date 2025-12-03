package net.stln.magitech.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class NetworkHelper {
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC =
            StreamCodec.of(
                    (buf, vec) -> {  // encode
                        buf.writeDouble(vec.x);
                        buf.writeDouble(vec.y);
                        buf.writeDouble(vec.z);
                    },
                    (buf) -> new Vec3(
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    )
            );
}
