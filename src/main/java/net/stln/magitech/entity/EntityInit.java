package net.stln.magitech.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.aeltherin.AeltherinEntity;
import net.stln.magitech.entity.magicentity.aeltherin.AeltherinRenderer;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethEntity;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethRenderer;
import net.stln.magitech.entity.magicentity.frigala.FrigalaEntity;
import net.stln.magitech.entity.magicentity.frigala.FrigalaRenderer;
import net.stln.magitech.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.entity.magicentity.ignisca.IgniscaRenderer;
import net.stln.magitech.entity.magicentity.mirazien.MirazienEntity;
import net.stln.magitech.entity.magicentity.mirazien.MirazienRenderer;
import net.stln.magitech.entity.magicentity.nullixis.NullixisEntity;
import net.stln.magitech.entity.magicentity.nullixis.NullixisRenderer;
import net.stln.magitech.entity.magicentity.tremivox.TremivoxEntity;
import net.stln.magitech.entity.magicentity.tremivox.TremivoxRenderer;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisEntity;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisRenderer;
import net.stln.magitech.entity.mob.WeaverEntity;
import net.stln.magitech.entity.mob.WeaverRenderer;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Magitech.MOD_ID);

    public static final Supplier<EntityType<IgniscaEntity>> IGNISCA_ENTITY = ENTITY_TYPES.register("ignisca",
            () -> EntityType.Builder.<IgniscaEntity>of(IgniscaEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("ignisca"));
    public static final Supplier<EntityType<FrigalaEntity>> FRIGALA_ENTITY = ENTITY_TYPES.register("frigala",
            () -> EntityType.Builder.<FrigalaEntity>of(FrigalaEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("frigala"));
    public static final Supplier<EntityType<VoltarisEntity>> VOLTARIS_ENTITY = ENTITY_TYPES.register("voltaris",
            () -> EntityType.Builder.<VoltarisEntity>of(VoltarisEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).build("voltaris"));
    public static final Supplier<EntityType<MirazienEntity>> MIRAZIEN_ENTITY = ENTITY_TYPES.register("mirazien",
            () -> EntityType.Builder.<MirazienEntity>of(MirazienEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("mirazien"));
    public static final Supplier<EntityType<TremivoxEntity>> TREMIVOX_ENTITY = ENTITY_TYPES.register("tremivox",
            () -> EntityType.Builder.<TremivoxEntity>of(TremivoxEntity::new, MobCategory.MISC).sized(0.75F, 0.75F).build("tremivox"));
    public static final Supplier<EntityType<ArcalethEntity>> ARCALETH_ENTITY = ENTITY_TYPES.register("arcaleth",
            () -> EntityType.Builder.<ArcalethEntity>of(ArcalethEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("arcaleth"));
    public static final Supplier<EntityType<AeltherinEntity>> AELTHERIN_ENTITY = ENTITY_TYPES.register("aeltherin",
            () -> EntityType.Builder.<AeltherinEntity>of(AeltherinEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("aeltherin"));
    public static final Supplier<EntityType<NullixisEntity>> NULLIXIS_ENTITY = ENTITY_TYPES.register("nullixis",
            () -> EntityType.Builder.<NullixisEntity>of(NullixisEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("nullixis"));

    public static final Supplier<EntityType<WeaverEntity>> WEAVER_ENTITY = ENTITY_TYPES.register("weaver",
            () -> EntityType.Builder.<WeaverEntity>of(WeaverEntity::new, MobCategory.MONSTER).sized(0.6F, 2.0F).eyeHeight(1.62F).clientTrackingRange(8).build("weaver"));

    public static void registerModEntities(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Entity for " + Magitech.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerModEntitiesRenderer() {
        Magitech.LOGGER.info("Registering Entity Renderer for " + Magitech.MOD_ID);
        EntityRenderers.register(EntityInit.IGNISCA_ENTITY.get(), IgniscaRenderer::new);
        EntityRenderers.register(EntityInit.FRIGALA_ENTITY.get(), FrigalaRenderer::new);
        EntityRenderers.register(EntityInit.VOLTARIS_ENTITY.get(), VoltarisRenderer::new);
        EntityRenderers.register(EntityInit.MIRAZIEN_ENTITY.get(), MirazienRenderer::new);
        EntityRenderers.register(EntityInit.TREMIVOX_ENTITY.get(), TremivoxRenderer::new);
        EntityRenderers.register(EntityInit.ARCALETH_ENTITY.get(), ArcalethRenderer::new);
        EntityRenderers.register(EntityInit.AELTHERIN_ENTITY.get(), AeltherinRenderer::new);
        EntityRenderers.register(EntityInit.NULLIXIS_ENTITY.get(), NullixisRenderer::new);

        EntityRenderers.register(EntityInit.WEAVER_ENTITY.get(), WeaverRenderer::new);
    }


    @SubscribeEvent
    public static void registerDefaultAttributes(EntityAttributeCreationEvent event) {
        Magitech.LOGGER.info("Registering Entity Attribute for " + Magitech.MOD_ID);
        event.put(WEAVER_ENTITY.get(), WeaverEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                WEAVER_ENTITY.get(),
                SpawnPlacementTypes.ON_GROUND,              // 湧く場所のタイプ
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,   // 高さ判定
                ((entityType, serverLevel, spawnType, pos, random) -> true),             // 条件 (ここは独自関数でもOK)
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

}
