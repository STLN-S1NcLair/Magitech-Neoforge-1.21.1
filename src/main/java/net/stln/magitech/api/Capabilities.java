package net.stln.magitech.api;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.IBasicManaHandler;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.api.mana.IItemManaHandler;

public class Capabilities {

    // ブロック用: 面(Direction)ごとのアクセスが可能
    public static final BlockCapability<IBlockManaHandler, Direction> MANA_CONTAINER =
            BlockCapability.createSided(
                    Magitech.id("mana_handler"),
                    IBlockManaHandler.class
            );

    // アイテム用: 面の概念がないのでコンテキストはVoid
    public static final ItemCapability<IItemManaHandler, Void> MANA_CONTAINER_ITEM =
            ItemCapability.createVoid(
                    Magitech.id("mana_handler"),
                    IItemManaHandler.class
            );
}
