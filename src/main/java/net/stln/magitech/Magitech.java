package net.stln.magitech;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
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
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.stln.magitech.advancement.CriterionInit;
import net.stln.magitech.biome.BiomeInit;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.compat.curios.ValidatorInit;
import net.stln.magitech.compat.modonomicon.PageInit;
import net.stln.magitech.data.DataMapTypeInit;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.mobeffect.MobEffectInit;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.event.EventInit;
import net.stln.magitech.fluid.FluidInit;
import net.stln.magitech.gui.GuiInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.ItemPropertyInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.creative_tab.CreativeTabInit;
import net.stln.magitech.element.Element;
import net.stln.magitech.item.fluid.FluidContainerMatcherInit;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.upgrade.UpgradeInit;
import net.stln.magitech.loot.LootFunctionInit;
import net.stln.magitech.magic.spell.SpellInit;
import net.stln.magitech.particle.ParticleInit;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.worldgen.WorldGenInit;
import net.stln.magitech.worldgen.tree.TreeGrowerInit;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Magitech.MOD_ID)
public class Magitech {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "magitech";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Magitech(IEventBus modEventBus, ModContainer modContainer) {
        MagitechRegistries.register(modEventBus);
        
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ComponentInit.registerComponents(modEventBus);
        
        BlockInit.registerBlocks(modEventBus);
        ItemInit.registerItems(modEventBus);
        FluidInit.registerFluids(modEventBus);

        AttributeInit.registerEntityAttributes(modEventBus);
        CreativeTabInit.registerCreativeTabs(modEventBus);
        CriterionInit.registerCriteria(modEventBus);
        Element.registerElements();
        DataMapTypeInit.registerDataMapTypes(modEventBus);
        EntityInit.registerModEntities(modEventBus);
        GuiInit.registerMenus(modEventBus);
        LootFunctionInit.registerFunctions(modEventBus);
        MaterialInit.registerMaterials(modEventBus);
        MobEffectInit.registerMobEffects(modEventBus);
        ParticleInit.registerParticleClient(modEventBus);
        RecipeInit.registerRecipes(modEventBus);
        SoundInit.registerSoundEvents(modEventBus);
        SpellInit.registerSpells(modEventBus);
        FluidContainerMatcherInit.registerFluidContainerMatchers(modEventBus);
        ToolMaterialRegister.init();
        TreeGrowerInit.registerTrunkPlacerTypes(modEventBus);
        UpgradeInit.registerUpgrades();
        ValidatorInit.registerValidators();
        WorldGenInit.registerFeatures(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
//        WorldGenInit.registerBiomeModifiers();
        BlockInit.registerStrippableBlocks();
        BiomeInit.registerBiomeRegions(event);
        PageInit.registerPages();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {

        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityInit.registerModEntitiesRenderer();
            EventInit.registerClientEvent();
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
