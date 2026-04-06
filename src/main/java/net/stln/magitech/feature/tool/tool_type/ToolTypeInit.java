package net.stln.magitech.feature.tool.tool_type;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.feature.tool.part.ToolPartInit;
import net.stln.magitech.feature.tool.property.ElementalAttributeToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.registry.DeferredToolType;
import net.stln.magitech.registry.DeferredToolTypeRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class ToolTypeInit {
    public static final DeferredToolTypeRegister REGISTER = new DeferredToolTypeRegister(Magitech.MOD_ID);

    private static float order = 0F;

    private static @NotNull DeferredToolType<ToolType> register(@NotNull String path, @NotNull Function<Float, ToolType> toolType) {
        return REGISTER.register(path, () -> toolType.apply(order++));
    }

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> DAGGER = register("dagger", f -> new ToolType(f, false, ItemInit.DAGGER,
            ToolMineType.create(MineType.SWORD),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 3.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 3.2)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 1.0)
                    .set(ToolPropertyInit.DURABILITY, 288.0),
            List.of(new ToolType.PartData(ToolPartInit.LIGHT_HANDLE, 0.4F),
                    new ToolType.PartData(ToolPartInit.LIGHT_BLADE, 1.5F),
                    new ToolType.PartData(ToolPartInit.HANDGUARD, 1.1F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> LIGHT_SWORD = register("light_sword", f -> new ToolType(f, false, ItemInit.LIGHT_SWORD,
            ToolMineType.create(MineType.SWORD),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 5.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(4.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 1.2)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 2.0)
                    .set(ToolPropertyInit.DURABILITY, 361.0),
            List.of(new ToolType.PartData(ToolPartInit.LIGHT_HANDLE, 0.9F),
                    new ToolType.PartData(ToolPartInit.LIGHT_BLADE, 1.5F),
                    new ToolType.PartData(ToolPartInit.HANDGUARD, 1.3F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.3F)
            )
    ));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> HEAVY_SWORD = register("heavy_sword", f -> new ToolType(f, true, ItemInit.HEAVY_SWORD,
            ToolMineType.create(MineType.SWORD),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 7.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(4.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 6.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 2.0)
                    .set(ToolPropertyInit.DURABILITY, 536.0),
            List.of(new ToolType.PartData(ToolPartInit.LIGHT_HANDLE, 0.9F),
                    new ToolType.PartData(ToolPartInit.HEAVY_BLADE, 1.7F),
                    new ToolType.PartData(ToolPartInit.HANDGUARD, 1.1F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.3F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> PICKAXE = register("pickaxe", f -> new ToolType(f, false, ItemInit.PICKAXE,
            ToolMineType.create(MineType.PICKAXE),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 3.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(1.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 2.4)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 1.0)
                    .set(ToolPropertyInit.REACH, 3.5)
                    .set(ToolPropertyInit.SWEEP, 1.5)
                    .set(ToolPropertyInit.DURABILITY, 319.0),
            List.of(new ToolType.PartData(ToolPartInit.HEAVY_HANDLE, 0.8F),
                    new ToolType.PartData(ToolPartInit.SPIKE_HEAD, 1.7F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.5F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> HAMMER = register("hammer", f -> new ToolType(f, true, ItemInit.HAMMER,
            ToolMineType.create(MineType.PICKAXE), AdditionalMineHelper::addHammerMine,
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 9.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(7.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.6)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 4.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 1.5)
                    .set(ToolPropertyInit.DURABILITY, 1013.0),
            List.of(new ToolType.PartData(ToolPartInit.HEAVY_HANDLE, 0.8F),
                    new ToolType.PartData(ToolPartInit.STRIKE_HEAD, 1.7F),
                    new ToolType.PartData(ToolPartInit.PLATE, 1.2F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.3F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> AXE = register("axe", f -> new ToolType(f, false, ItemInit.AXE,
            ToolMineType.create(MineType.AXE),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 6.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 5.0)
                    .set(ToolPropertyInit.REACH, 3.5)
                    .set(ToolPropertyInit.SWEEP, 2.0)
                    .set(ToolPropertyInit.DURABILITY, 325.0),
            List.of(new ToolType.PartData(ToolPartInit.HEAVY_HANDLE, 0.6F),
                    new ToolType.PartData(ToolPartInit.LIGHT_BLADE, 1.9F),
                    new ToolType.PartData(ToolPartInit.STRIKE_HEAD, 1.3F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.2F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> SHOVEL = register("shovel", f -> new ToolType(f, false, ItemInit.SHOVEL,
            ToolMineType.create(MineType.SHOVEL),
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 4.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 1.6)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 6.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 4.0)
                    .set(ToolPropertyInit.DURABILITY, 401.0),
            List.of(new ToolType.PartData(ToolPartInit.HEAVY_HANDLE, 0.6F),
                    new ToolType.PartData(ToolPartInit.LIGHT_BLADE, 1.7F),
                    new ToolType.PartData(ToolPartInit.PLATE, 1.2F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.5F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> SCYTHE = register("scythe", f -> new ToolType(f, true, ItemInit.SCYTHE,
            ToolMineType.create(MineType.SWORD), AdditionalMineHelper::addScytheMine,
            () -> new ToolProperties(ToolCategoryInit.MELEE)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.DAMAGE, 4.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(6.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.7)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 4.5)
                    .set(ToolPropertyInit.SWEEP, 4.0)
                    .set(ToolPropertyInit.DURABILITY, 594.0),
            List.of(new ToolType.PartData(ToolPartInit.REINFORCED_ROD, 1.1F),
                    new ToolType.PartData(ToolPartInit.HEAVY_HANDLE, 1.9F),
                    new ToolType.PartData(ToolPartInit.HEAVY_BLADE, 0.8F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.2F)
            )));

    @SuppressWarnings("unchecked")
    public static final DeferredToolType<ToolType> WAND = register("wand", f -> new ToolType(f, false, ItemInit.WAND,
            ToolMineType.none(),
            () -> new ToolProperties(ToolCategoryInit.CASTER)
                    .set(ToolPropertyInit.TIER, 5.0)
                    .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
                    .set(ToolPropertyInit.POWER, 1.0)
                    .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.flatValue(1.0))
                    .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
                    .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
                    .set(ToolPropertyInit.DEFENSE, 1.0)
                    .set(ToolPropertyInit.LAUNCH, 1.0)
                    .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
                    .set(ToolPropertyInit.DURABILITY, 378.0),
            List.of(new ToolType.PartData(ToolPartInit.CATALYST, 1.6F),
                    new ToolType.PartData(ToolPartInit.LIGHT_HANDLE, 0.5F),
                    new ToolType.PartData(ToolPartInit.CONDUCTOR, 1.3F),
                    new ToolType.PartData(ToolPartInit.TOOL_BINDING, 0.6F)
            )));

    public static void registerToolTypes(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Types for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
