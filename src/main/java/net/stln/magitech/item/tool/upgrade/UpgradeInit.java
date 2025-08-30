package net.stln.magitech.item.tool.upgrade;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.MiningLevel;
import net.stln.magitech.util.Element;

public class UpgradeInit {

    public static final Upgrade ATTACK_UPGRADE = new SimpleUpgrade(new ToolStats(0.1F, 0, 0, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade ELEMENTAL_ATTACK_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0.1F, 0, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade SPEED_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0.1F, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade MINING_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0.1F, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade DEFENCE_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0.1F, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade RANGE_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0.1F, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade SWEEP_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0, 0.1F, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade DURABILITY_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0, 0, 0.1F, Element.NONE, MiningLevel.NONE, 0));

    public static final Upgrade CASTER_POWER_UPGRADE = new SimpleUpgrade(new ToolStats(0.1F, 0, 0, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_ELEMENTAL_POWER_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0.1F, 0, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_CHARGE_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0.1F, 0, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_COOLDOWN_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0.1F, 0, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_DEFENCE_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0.1F, 0, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_PROJECTILE_SPEED_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0.1F, 0, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_EFFICIENCY_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0, 0.1F, 0, Element.NONE, MiningLevel.NONE, 0));
    public static final Upgrade CASTER_DURABILITY_UPGRADE = new SimpleUpgrade(new ToolStats(0, 0, 0, 0, 0, 0, 0, 0.1F, Element.NONE, MiningLevel.NONE, 0));

    public static void registerUpgrades() {
        Magitech.LOGGER.info("Registering Upgrades for " + Magitech.MOD_ID);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "damage_upgrade"), ATTACK_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "elemental_damage_upgrade"), ELEMENTAL_ATTACK_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "speed_upgrade"), SPEED_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mining_upgrade"), MINING_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "defence_upgrade"), DEFENCE_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "range_upgrade"), RANGE_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sweep_upgrade"), SWEEP_UPGRADE);
        UpgradeRegister.registerId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "durability_upgrade"), DURABILITY_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_power_upgrade"), CASTER_POWER_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_elemental_power_upgrade"), CASTER_ELEMENTAL_POWER_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_charge_upgrade"), CASTER_CHARGE_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_cooldown_upgrade"), CASTER_COOLDOWN_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_defence_upgrade"), CASTER_DEFENCE_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_projectile_speed_upgrade"), CASTER_PROJECTILE_SPEED_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_efficiency_upgrade"), CASTER_EFFICIENCY_UPGRADE);
        UpgradeRegister.registerSpellCasterId(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "caster_durability_upgrade"), CASTER_DURABILITY_UPGRADE);
    }
}
