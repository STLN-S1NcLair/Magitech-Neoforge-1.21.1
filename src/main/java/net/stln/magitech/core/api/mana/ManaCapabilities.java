package net.stln.magitech.core.api.mana;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.core.api.mana.handler.IItemManaHandler;

public class ManaCapabilities {

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

    // エンティティ用: 面の概念がないのでコンテキストはVoid
    public static final EntityCapability<EntityManaHandler, Void> MANA_CAPABLE_ENTITY =
            EntityCapability.createVoid(
                    Magitech.id("mana_handler"),
                    EntityManaHandler.class
            );
}
