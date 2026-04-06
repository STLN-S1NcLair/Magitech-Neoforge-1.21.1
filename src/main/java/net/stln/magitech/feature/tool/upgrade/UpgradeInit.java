package net.stln.magitech.feature.tool.upgrade;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.registry.DeferredUpgrade;
import net.stln.magitech.registry.DeferredUpgradeRegister;
import org.jetbrains.annotations.NotNull;

public class UpgradeInit {

    public static final DeferredUpgradeRegister REGISTER = new DeferredUpgradeRegister(Magitech.MOD_ID);

    private static <T extends Upgrade> @NotNull DeferredUpgrade<T> register(@NotNull String path, @NotNull T upgrade) {
        return REGISTER.register(path, () -> upgrade);
    }

    public static final DeferredUpgrade<Upgrade> ATTACK_UPGRADE = register("attack_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.ATTACK, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> ELEMENTAL_UPGRADE = register("element_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.ELEMENT, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> HANDLING_UPGRADE = register("handling_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.HANDLING, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> CONTINUITY_UPGRADE = register("continuity_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.CONTINUITY, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> RANGE_UPGRADE = register("range_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.DEFENCE, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> SWEEP_UPGRADE = register("unique_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.RANGE, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> DEFENCE_UPGRADE = register("defence_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.UNIQUE, 0.05F), ToolCategoryInit.ALL));
    public static final DeferredUpgrade<Upgrade> DURABILITY_UPGRADE = register("durability_upgrade", new SingleUpgrade(new RationalToolPropertyModifier(ToolPropertyCategory.DURABILITY, 0.05F), ToolCategoryInit.ALL));

    public static void registerUpgrades(IEventBus bus) {
        Magitech.LOGGER.info("Registering Upgrades for " + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
