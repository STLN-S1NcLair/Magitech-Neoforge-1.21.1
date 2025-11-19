package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.stln.magitech.data.DataMapTypeInit;
import net.stln.magitech.data.EntityElementData;
import net.stln.magitech.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("deprecation")
    private static void addElement(Builder<EntityElementData, EntityType<?>> builder, EntityType<?> entityType, Element element) {
        builder.add(entityType.builtInRegistryHolder(), new EntityElementData(element), false);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        gatherEntityElement();
    }

    private void gatherEntityElement() {
        Builder<EntityElementData, EntityType<?>> builder = builder(DataMapTypeInit.ENTITY_ELEMENT);
        addElement(builder, EntityType.BLAZE, Element.EMBER);
        addElement(builder, EntityType.BREEZE, Element.FLOW);
        addElement(builder, EntityType.DROWNED, Element.FLOW);
        addElement(builder, EntityType.ELDER_GUARDIAN, Element.FLOW);
        addElement(builder, EntityType.ENDER_DRAGON, Element.HOLLOW);
        addElement(builder, EntityType.ENDERMAN, Element.HOLLOW);
        addElement(builder, EntityType.ENDERMITE, Element.HOLLOW);
        addElement(builder, EntityType.EVOKER, Element.MAGIC);
        addElement(builder, EntityType.GUARDIAN, Element.FLOW);
        addElement(builder, EntityType.MAGMA_CUBE, Element.EMBER);
        addElement(builder, EntityType.PHANTOM, Element.PHANTOM);
        addElement(builder, EntityType.SHULKER, Element.HOLLOW);
        addElement(builder, EntityType.STRAY, Element.GLACE);
        addElement(builder, EntityType.VEX, Element.PHANTOM);
        addElement(builder, EntityType.WARDEN, Element.TREMOR);
        addElement(builder, EntityType.WITCH, Element.MAGIC);
    }
}
