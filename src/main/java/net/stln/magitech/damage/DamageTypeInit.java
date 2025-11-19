package net.stln.magitech.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.util.DataMapHelper;

public class DamageTypeInit {

    public static final ResourceKey<DamageType> MANA_DAMAGE = create("mana");
    public static final ResourceKey<DamageType> EMBER_DAMAGE = create("ember");
    public static final ResourceKey<DamageType> GLACE_DAMAGE = create("glace");
    public static final ResourceKey<DamageType> SURGE_DAMAGE = create("surge");
    public static final ResourceKey<DamageType> PHANTOM_DAMAGE = create("phantom");
    public static final ResourceKey<DamageType> TREMOR_DAMAGE = create("tremor");
    public static final ResourceKey<DamageType> MAGIC_DAMAGE = create("magic");
    public static final ResourceKey<DamageType> FLOW_DAMAGE = create("flow");
    public static final ResourceKey<DamageType> HOLLOW_DAMAGE = create("hollow");

    public static final ResourceKey<DamageType> MANA_BERRY_BUSH = create("mana_berry_bush");

    public static float getElementDamage(Player player, Entity target, ItemStack stack) {
        ToolStats stats = ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack);
        float multiplier = DataMapHelper.getElementMultiplier(target, stats.getElement());
        return stats.getStats().get(ToolStats.ELM_ATK_STAT) * multiplier;
    }

    private static ResourceKey<DamageType> create(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Magitech.id(path));
    }
}
