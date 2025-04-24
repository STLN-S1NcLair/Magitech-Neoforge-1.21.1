package net.stln.magitech.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethRenderer;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethEntity;
import net.stln.magitech.entity.magicentity.frigala.FrigalaEntity;
import net.stln.magitech.entity.magicentity.frigala.FrigalaRenderer;
import net.stln.magitech.entity.magicentity.mirazien.MirazienEntity;
import net.stln.magitech.entity.magicentity.mirazien.MirazienRenderer;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisEntity;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisRenderer;

import java.util.function.Supplier;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Magitech.MOD_ID);

    public static final Supplier<EntityType<ArcalethEntity>> ARCALETH_ENTITY = ENTITY_TYPES.register("arcaleth",
            () -> EntityType.Builder.<ArcalethEntity>of(ArcalethEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build("arcaleth"));
    public static final Supplier<EntityType<MirazienEntity>> MIRAZIEN_ENTITY = ENTITY_TYPES.register("mirazien",
            () -> EntityType.Builder.<MirazienEntity>of(MirazienEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build("mirazien"));
    public static final Supplier<EntityType<FrigalaEntity>> FRIGALA_ENTITY = ENTITY_TYPES.register("frigala",
            () -> EntityType.Builder.<FrigalaEntity>of(FrigalaEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build("frigala"));
    public static final Supplier<EntityType<VoltarisEntity>> VOLTARIS_ENTITY = ENTITY_TYPES.register("voltaris",
            () -> EntityType.Builder.<VoltarisEntity>of(VoltarisEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build("voltaris"));

    public static void registerModEntities(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Entity for " + Magitech.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerModEntitiesRenderer() {
        Magitech.LOGGER.info("Registering Entity Renderer for " + Magitech.MOD_ID);
        EntityRenderers.register(EntityInit.ARCALETH_ENTITY.get(), ArcalethRenderer::new);
        EntityRenderers.register(EntityInit.MIRAZIEN_ENTITY.get(), MirazienRenderer::new);
        EntityRenderers.register(EntityInit.FRIGALA_ENTITY.get(), FrigalaRenderer::new);
        EntityRenderers.register(EntityInit.VOLTARIS_ENTITY.get(), VoltarisRenderer::new);
    }

}
