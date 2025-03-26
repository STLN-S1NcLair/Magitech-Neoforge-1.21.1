package net.stln.magitech.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.client.MagicBulletRenderer;

import java.util.function.Supplier;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Magitech.MOD_ID);

    public static final Supplier<EntityType<MagicBulletEntity>> MAGIC_BULLET = ENTITY_TYPES.register("magic_bullet",
            () -> EntityType.Builder.<MagicBulletEntity>of(MagicBulletEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build("magic_bullet"));

    public static void registerModEntities(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Entity for " + Magitech.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerModEntitiesRenderer() {
        Magitech.LOGGER.info("Registering Entity Renderer for " + Magitech.MOD_ID);
        EntityRenderers.register(EntityInit.MAGIC_BULLET.get(), MagicBulletRenderer::new);
    }

}
