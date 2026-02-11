package net.stln.magitech.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.flow.network.connectable.IManaNode;
import net.stln.magitech.block.ManaNodeBlock;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ManaNetworkHandler {

    // ブロックが設置されたとき (壁が置かれた、新しいノードが置かれた等)
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof Level level && !level.isClientSide) {
            notifyNearbyNodes(level, event.getPos());
        }
    }

    // ブロックが破壊されたとき (壁が壊れた、ノードが撤去された等)
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof Level level && !level.isClientSide) {
            notifyNearbyNodes(level, event.getPos());
        }
    }

    /**
     * 指定座標の周囲(半径4ブロック)にあるマナノードを探し、再スキャンを要求する
     */
    private static void notifyNearbyNodes(Level level, BlockPos center) {
        // 探索範囲: ノードの接続範囲(3) + 1 = 4ブロック分余裕を見る
        int range = 4;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    mutablePos.setWithOffset(center, x, y, z);

                    // 軽いチェック: ブロックの種類を確認
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.getBlock() instanceof ManaNodeBlock) {
                        // 重いチェック: BEを取得してリクエスト
                        BlockEntity be = level.getBlockEntity(mutablePos);
                        if (be instanceof IManaNode node) {
                            node.requestRescan();
                        }
                    }
                }
            }
        }
    }
}