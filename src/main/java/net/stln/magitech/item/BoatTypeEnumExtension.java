package net.stln.magitech.item;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;

public class BoatTypeEnumExtension {

    public static final EnumProxy<Boat.Type> CELIFERN_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class, BlockInit.CELIFERN_PLANKS, Magitech.id("celifern").toString(), ItemInit.CELIFERN_BOAT, ItemInit.CELIFERN_CHEST_BOAT, Items.STICK, false);


    public static final EnumProxy<Boat.Type> CHARCOAL_BIRCH_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class, BlockInit.CHARCOAL_BIRCH_PLANKS, Magitech.id("charcoal_birch").toString(), ItemInit.CHARCOAL_BIRCH_BOAT, ItemInit.CHARCOAL_BIRCH_CHEST_BOAT, Items.STICK, false);


    public static final EnumProxy<Boat.Type> MYSTWOOD_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class, BlockInit.MYSTWOOD_PLANKS, Magitech.id("mystwood").toString(), ItemInit.MYSTWOOD_BOAT, ItemInit.MYSTWOOD_CHEST_BOAT, Items.STICK, false);
}
