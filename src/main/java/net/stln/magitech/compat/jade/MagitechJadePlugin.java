package net.stln.magitech.compat.jade;

import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.block.ManaContainerBlock;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MagitechJadePlugin implements IWailaPlugin {

    // クライアント側の登録 (表示ロジック)
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(
                ManaContainerJadeProvider.INSTANCE,
                ManaContainerBlock.class
        );
    }

    // サーバー側の登録 (データ同期ロジック)
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(
                ManaContainerServerProvider.INSTANCE,
                ManaContainerBlockEntity.class
        );
    }
}
