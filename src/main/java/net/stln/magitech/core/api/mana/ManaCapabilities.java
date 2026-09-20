package net.stln.magitech.core.api.mana;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.core.api.mana.handler.IItemManaHandler;

/**
 * マナ関連のNeoForge Capability定義を保持します。
 * Holds the NeoForge capability definitions used by the mana system.
 */
public class ManaCapabilities {

    /**
     * ブロック面ごとに取得できるマナハンドラーの Capability です。
     * Capability for mana handlers accessed per block face.
     */
    public static final BlockCapability<IBlockManaHandler, Direction> MANA_CONTAINER =
            BlockCapability.createSided(
                    Magitech.id("mana_handler"),
                    IBlockManaHandler.class
            );

    /**
     * ItemStack から取得できるマナハンドラーの Capability です。
     * Capability for mana handlers accessed from ItemStacks.
     */
    public static final ItemCapability<IItemManaHandler, Void> MANA_CONTAINER_ITEM =
            ItemCapability.createVoid(
                    Magitech.id("mana_handler"),
                    IItemManaHandler.class
            );

    /**
     * エンティティから取得できるマナハンドラーの Capability です。
     * Capability for mana handlers accessed from entities.
     */
    public static final EntityCapability<EntityManaHandler, Void> MANA_CAPABLE_ENTITY =
            EntityCapability.createVoid(
                    Magitech.id("mana_handler"),
                    EntityManaHandler.class
            );
}
