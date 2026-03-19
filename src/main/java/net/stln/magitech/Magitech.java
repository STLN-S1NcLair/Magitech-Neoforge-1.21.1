package net.stln.magitech;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.stln.magitech.compat.curios.ValidatorInit;
import net.stln.magitech.compat.modonomicon.PageInit;
import net.stln.magitech.content.advancement.CriterionInit;
import net.stln.magitech.content.biome.BiomeInit;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.fluid.FluidInit;
import net.stln.magitech.content.gui.GuiInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemPropertyInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.creative_tab.CreativeTabInit;
import net.stln.magitech.content.item.fluid.FluidContainerMatcherInit;
import net.stln.magitech.content.loot.LootFunctionInit;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.data.DataMapTypeInit;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.feature.tool.part.ToolPartInit;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.register.ToolMaterialRegister;
import net.stln.magitech.feature.tool.tool_group.ToolGroupInit;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;
import net.stln.magitech.feature.tool.upgrade.UpgradeInit;
import net.stln.magitech.worldgen.WorldGenInit;
import net.stln.magitech.worldgen.tree.TreeGrowerInit;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Magitech.MOD_ID)
public class Magitech {
    // Define mod id in a common place for everything child reference
    public static final String MOD_ID = "magitech";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Magitech(IEventBus modEventBus, ModContainer modContainer) {
        MagitechRegistries.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        // Item
        ComponentInit.registerComponents(modEventBus);
        ItemInit.registerItems(modEventBus);
        CreativeTabInit.registerCreativeTabs(modEventBus);
        FluidContainerMatcherInit.registerFluidContainerMatchers(modEventBus);

        // Block
        BlockInit.registerBlocks(modEventBus);
        FluidInit.registerFluids(modEventBus);

        // Entity
        EntityInit.registerModEntities(modEventBus);
        AttributeInit.registerEntityAttributes(modEventBus);
        MobEffectInit.registerMobEffects(modEventBus);
        DataAttachmentInit.registerDataAttachmentTypes(modEventBus);
        DataMapTypeInit.registerDataMapTypes(modEventBus);

        // UI
        GuiInit.registerMenus(modEventBus);

        // Core Feature
        Element.registerElements();
        MaterialInit.registerMaterials(modEventBus);
        SpellInit.registerSpells(modEventBus);
        ToolMaterialRegister.init();
        ToolPartInit.registerToolParts(modEventBus);
        ToolPropertyInit.registerToolProperties(modEventBus);
        ToolGroupInit.registerToolGroups(modEventBus);
        ToolTypeInit.registerToolTypes(modEventBus);
        UpgradeInit.registerUpgrades();

        // Generation
        LootFunctionInit.registerFunctions(modEventBus);
        WorldGenInit.registerFeatures(modEventBus);
        TreeGrowerInit.registerTrunkPlacerTypes(modEventBus);

        // Effect
        ParticleInit.registerParticleType(modEventBus);
        SoundInit.registerSoundEvents(modEventBus);

        // Misc
        CriterionInit.registerCriteria(modEventBus);
        RecipeInit.registerRecipes(modEventBus);
        ValidatorInit.registerValidators();

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) child respond directly child events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
//        WorldGenInit.registerBiomeModifiers();
        BiomeInit.registerBiomeRegions(event);
        PageInit.registerPages();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods child call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber child automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityInit.registerModEntitiesRenderer();
            ItemPropertyInit.registerItemProperties();
            PageInit.registerRenderers();
            FluidInit.registerFluidRenderTypes();
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            ParticleInit.registerParticleFactories(event);
        }
    }
}
