package net.stln.magitech.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
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
import net.stln.magitech.entity.magicentity.electroide.ElectroideEntity;
import net.stln.magitech.entity.magicentity.electroide.ElectroideRenderer;
import net.stln.magitech.entity.magicentity.frigala.FrigalaEntity;
import net.stln.magitech.entity.magicentity.frigala.FrigalaRenderer;
import net.stln.magitech.entity.magicentity.frosblast.FrosblastEntity;
import net.stln.magitech.entity.magicentity.frosblast.FrosblastRenderer;
import net.stln.magitech.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.entity.magicentity.ignisca.IgniscaRenderer;
import net.stln.magitech.entity.magicentity.illusflare.IllusflareEntity;
import net.stln.magitech.entity.magicentity.illusflare.IllusflareRenderer;
import net.stln.magitech.entity.magicentity.mirazien.MirazienEntity;
import net.stln.magitech.entity.magicentity.mirazien.MirazienRenderer;
import net.stln.magitech.entity.magicentity.nullixis.NullixisEntity;
import net.stln.magitech.entity.magicentity.nullixis.NullixisRenderer;
import net.stln.magitech.entity.magicentity.shockvane.ShockvaneEntity;
import net.stln.magitech.entity.magicentity.shockvane.ShockvaneRenderer;
import net.stln.magitech.entity.magicentity.tremivox.TremivoxEntity;
import net.stln.magitech.entity.magicentity.tremivox.TremivoxRenderer;
import net.stln.magitech.entity.magicentity.volkarin.VolkarinEntity;
import net.stln.magitech.entity.magicentity.volkarin.VolkarinRenderer;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisEntity;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisRenderer;
import net.stln.magitech.entity.mob.WeaverEntity;
import net.stln.magitech.entity.mob.WeaverRenderer;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Magitech.MOD_ID);

    public static final Supplier<EntityType<IgniscaEntity>> IGNISCA_ENTITY = registerMobEntity("ignisca", IgniscaEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final Supplier<EntityType<VolkarinEntity>> VOLKARIN_ENTITY = registerMobEntity("volkarin", VolkarinEntity::new, MobCategory.MISC, builder -> builder.sized(1.0F, 1.0F));
    public static final Supplier<EntityType<FrigalaEntity>> FRIGALA_ENTITY = registerMobEntity("frigala", FrigalaEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final Supplier<EntityType<FrosblastEntity>> FROSBLAST_ENTITY = registerMobEntity("frosblast", FrosblastEntity::new, MobCategory.MISC, builder -> builder.sized(1.0F, 1.0F));
    public static final Supplier<EntityType<VoltarisEntity>> VOLTARIS_ENTITY = registerMobEntity("voltaris", VoltarisEntity::new, MobCategory.MISC, builder -> builder.sized(1.0F, 1.0F));
    public static final Supplier<EntityType<ElectroideEntity>> ELECTROIDE_ENTITY = registerMobEntity("electroide", ElectroideEntity::new, MobCategory.MISC, builder -> builder.sized(1.25F, 1.25F));
    public static final Supplier<EntityType<MirazienEntity>> MIRAZIEN_ENTITY = registerMobEntity("mirazien", MirazienEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final Supplier<EntityType<IllusflareEntity>> ILLUSFLARE_ENTITY = registerMobEntity("illusflare", IllusflareEntity::new, MobCategory.MISC, builder -> builder.sized(1.0F, 1.0F));
    public static final Supplier<EntityType<TremivoxEntity>> TREMIVOX_ENTITY = registerMobEntity("tremivox", TremivoxEntity::new, MobCategory.MISC, builder -> builder.sized(0.75F, 0.75F));
    public static final Supplier<EntityType<ShockvaneEntity>> SHOCKVANE_ENTITY = registerMobEntity("shockvane", ShockvaneEntity::new, MobCategory.MISC, builder -> builder.sized(1.0F, 1.0F));
    public static final Supplier<EntityType<ArcalethEntity>> ARCALETH_ENTITY = registerMobEntity("arcaleth", ArcalethEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final Supplier<EntityType<AeltherinEntity>> AELTHERIN_ENTITY = registerMobEntity("aeltherin", AeltherinEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final Supplier<EntityType<NullixisEntity>> NULLIXIS_ENTITY = registerMobEntity("nullixis", NullixisEntity::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));

    public static final Supplier<EntityType<WeaverEntity>> WEAVER_ENTITY = registerMobEntity("weaver", WeaverEntity::new, MobCategory.MONSTER, (builder) -> builder.sized(0.6F, 2.0F).eyeHeight(1.62F).clientTrackingRange(8));

    public static void registerModEntities(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Entity for " + Magitech.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerModEntitiesRenderer() {
        Magitech.LOGGER.info("Registering Entity Renderer for " + Magitech.MOD_ID);
        EntityRenderers.register(EntityInit.IGNISCA_ENTITY.get(), IgniscaRenderer::new);
        EntityRenderers.register(EntityInit.VOLKARIN_ENTITY.get(), VolkarinRenderer::new);
        EntityRenderers.register(EntityInit.FRIGALA_ENTITY.get(), FrigalaRenderer::new);
        EntityRenderers.register(EntityInit.FROSBLAST_ENTITY.get(), FrosblastRenderer::new);
        EntityRenderers.register(EntityInit.VOLTARIS_ENTITY.get(), VoltarisRenderer::new);
        EntityRenderers.register(EntityInit.ELECTROIDE_ENTITY.get(), ElectroideRenderer::new);
        EntityRenderers.register(EntityInit.MIRAZIEN_ENTITY.get(), MirazienRenderer::new);
        EntityRenderers.register(EntityInit.ILLUSFLARE_ENTITY.get(), IllusflareRenderer::new);
        EntityRenderers.register(EntityInit.TREMIVOX_ENTITY.get(), TremivoxRenderer::new);
        EntityRenderers.register(EntityInit.SHOCKVANE_ENTITY.get(), ShockvaneRenderer::new);
        EntityRenderers.register(EntityInit.ARCALETH_ENTITY.get(), ArcalethRenderer::new);
        EntityRenderers.register(EntityInit.AELTHERIN_ENTITY.get(), AeltherinRenderer::new);
        EntityRenderers.register(EntityInit.NULLIXIS_ENTITY.get(), NullixisRenderer::new);

        EntityRenderers.register(EntityInit.WEAVER_ENTITY.get(), WeaverRenderer::new);
    }

    private static <T extends Entity> Supplier<EntityType<T>> registerMobEntity(String path, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> operator) {
        return ENTITY_TYPES.register(path, id -> operator.apply(EntityType.Builder.of(factory, category)).build(id.getPath()));
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
                Monster::checkMonsterSpawnRules,             // 条件 (ここは独自関数でもOK)
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

}
