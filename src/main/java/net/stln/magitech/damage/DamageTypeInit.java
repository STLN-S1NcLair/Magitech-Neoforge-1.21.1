package net.stln.magitech.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

public class DamageTypeInit {

    public static final ResourceKey<DamageType> MANA_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana"));
    public static final ResourceKey<DamageType> EMBER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ember"));
    public static final ResourceKey<DamageType> GLACE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "glace"));
    public static final ResourceKey<DamageType> SURGE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "surge"));
    public static final ResourceKey<DamageType> PHANTOM_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "phantom"));
    public static final ResourceKey<DamageType> TREMOR_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tremor"));
    public static final ResourceKey<DamageType> MAGIC_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "magic"));
    public static final ResourceKey<DamageType> FLOW_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "flow"));
    public static final ResourceKey<DamageType> HOLLOW_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "hollow"));

    public static float getElementDamage(Player player, Entity target, ItemStack stack) {
        ToolStats stats = ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack);
        return stats.getStats().get(ToolStats.ELM_ATK_STAT) * EntityElementRegister.getElementAffinity(target, stats.getElement()).getMultiplier();
    }

}
