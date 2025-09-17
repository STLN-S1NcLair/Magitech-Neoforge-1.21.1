package net.stln.magitech;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.stln.magitech.item.tool.material.ToolMaterialNew;
import net.stln.magitech.magic.spell.Spell;

public final class MagitechRegistries {
    private MagitechRegistries() {}
    
    public static final Registry<Spell> SPELL = new RegistryBuilder<>(Keys.SPELL).sync(true).create();
    public static final Registry<ToolMaterialNew> TOOL_MATERIAL = new RegistryBuilder<>(Keys.TOOL_MATERIAL).sync(true).create();
    
    public static void register(IEventBus eventBus) {
        eventBus.addListener((NewRegistryEvent event) -> {
            event.register(SPELL);
            event.register(TOOL_MATERIAL);
        });
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
    }
    
    public static final class Keys {
        private Keys() {}
        
        public static final ResourceKey<? extends Registry<Spell>> SPELL = ResourceKey.createRegistryKey(Magitech.id("spell"));
        public static final ResourceKey<? extends Registry<ToolMaterialNew>> TOOL_MATERIAL = ResourceKey.createRegistryKey(Magitech.id("tool_material"));
    }
}
