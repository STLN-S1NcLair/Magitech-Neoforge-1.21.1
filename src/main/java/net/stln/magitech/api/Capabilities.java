package net.stln.magitech.api;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.IManaHandler;

public class Capabilities {

    // ブロック用: 面(Direction)ごとのアクセスが可能
    public static final BlockCapability<IManaHandler, Direction> MANA_CONTAINER =
            BlockCapability.createSided(
                    Magitech.id("mana_handler"),
                    IManaHandler.class
            );

    // アイテム用: 面の概念がないのでコンテキストはVoid
    public static final ItemCapability<IManaHandler, Void> MANA_CONTAINER_ITEM =
            ItemCapability.createVoid(
                    Magitech.id("mana_handler"),
                    IManaHandler.class
            );
}
