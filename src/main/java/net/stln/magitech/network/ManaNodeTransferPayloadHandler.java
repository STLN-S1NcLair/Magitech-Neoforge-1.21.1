package net.stln.magitech.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.particle.particle_option.ManaZapParticleEffect;
import org.joml.Vector3f;

public class ManaNodeTransferPayloadHandler {
    // クライアント側での処理
    public static void handleDataOnMainS2C(ManaNodeTransferPayload packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // クライアントのワールドを取得
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            spawnManaParticle(level, packet.from(), packet.to());
        }
    }

    private static void spawnManaParticle(Level level, BlockPos fromPos, BlockPos toPos) {
        Vec3 from = fromPos.getCenter();
        Vec3 to = toPos.getCenter();
        RandomSource random = level.random;

        // パーティクル生成 (以前のロジックをここに移動)
        // パケットが来た＝確実に転送があったので、確率判定なしで出すか、
        // あるいは視覚的なうるささを防ぐために少し間引く
        if (random.nextFloat() < 0.5f) {
            level.addParticle(new ManaZapParticleEffect(
                            new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                            new Vector3f((float) to.x, (float) to.y, (float) to.z),
                            1.0F, random.nextInt(2, 5), 0, random.nextInt(2, 5), 1F),
                    from.x, from.y, from.z, 0, 0, 0);
        }
    }
}
