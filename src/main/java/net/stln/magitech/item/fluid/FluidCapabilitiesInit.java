package net.stln.magitech.item.fluid;

import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.stln.magitech.item.ItemInit;

@EventBusSubscriber
public class FluidCapabilitiesInit {

    @SubscribeEvent
    public static void registerFluidCapabilities(RegisterCapabilitiesEvent event) {
        // Fluid Capabilities登録
        registerAlchemicalFlaskCapability(event,
                ItemInit.ALCHEMICAL_FLASK,
                ItemInit.WATER_FLASK,
                ItemInit.LAVA_FLASK,
                ItemInit.SULFURIC_ACID_FLASK,
                ItemInit.MANA_POTION_FLASK,
                ItemInit.HEALING_POTION_FLASK,
                ItemInit.EMBER_POTION_FLASK
        );
    }

    private static void registerAlchemicalFlaskCapability(RegisterCapabilitiesEvent event, ItemLike... item) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, nbt) -> new AlchemicalFlaskFluidHandler(stack, 250), item);
    }
}
