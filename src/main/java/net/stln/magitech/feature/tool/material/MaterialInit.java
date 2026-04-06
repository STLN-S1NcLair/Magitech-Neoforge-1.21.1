package net.stln.magitech.feature.tool.material;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.property.ElementalAttributeToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.trait.*;
import net.stln.magitech.registry.DeferredToolMaterial;
import net.stln.magitech.registry.DeferredToolMaterialRegister;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class MaterialInit {

    public static final DeferredToolMaterialRegister REGISTER = new DeferredToolMaterialRegister(Magitech.MOD_ID);

    public static final DeferredToolMaterial<ToolMaterial> SAMPLE = register("sample", new SampleTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.NONE, 1.0))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 1.0)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 1.0)
            .set(ToolPropertyInit.DURABILITY, 1.0)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NONE)
            .set(ToolPropertyInit.POWER, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
    );

    public static final DeferredToolMaterial<ToolMaterial> WOOD = register("wood", new AdaptationTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.4)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.2))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.1)
            .set(ToolPropertyInit.MINING_SPEED, 0.4)
            .set(ToolPropertyInit.DEFENSE, 0.5)
            .set(ToolPropertyInit.REACH, 0.9)
            .set(ToolPropertyInit.SWEEP, 0.7)
            .set(ToolPropertyInit.DURABILITY, 0.3)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NONE)
            .set(ToolPropertyInit.POWER, 0.4)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.4))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.1)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.2)
            .set(ToolPropertyInit.LAUNCH, 0.9)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.7)
    );
    public static final DeferredToolMaterial<ToolMaterial> STONE = register("stone", new GeomendingTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.ATTACK_SPEED, 0.85)
            .set(ToolPropertyInit.MINING_SPEED, 0.8)
            .set(ToolPropertyInit.DEFENSE, 1.5)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 0.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 0.85)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.8)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.8)
    );
    public static final DeferredToolMaterial<ToolMaterial> DEEPSLATE = register("deepslate", new HardmineTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.TREMOR, 0.35))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.7)
            .set(ToolPropertyInit.MINING_SPEED, 0.9)
            .set(ToolPropertyInit.DEFENSE, 2.0)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 0.6)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.TREMOR, 0.35))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.7)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.9)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.8)
    );
    public static final DeferredToolMaterial<ToolMaterial> COPPER = register("copper", new ConductanceTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.3))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 0.8)
            .set(ToolPropertyInit.REACH, 0.8)
            .set(ToolPropertyInit.SWEEP, 0.9)
            .set(ToolPropertyInit.DURABILITY, 0.8)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.3))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.8)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 0.8)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.9)
    );
    public static final DeferredToolMaterial<ToolMaterial> ZINC = register("zinc", new ElectrostaticChargeTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.1)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.5))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.05)
            .set(ToolPropertyInit.MINING_SPEED, 0.6)
            .set(ToolPropertyInit.DEFENSE, 1.2)
            .set(ToolPropertyInit.REACH, 0.7)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 0.7)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.5))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 0.8)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.9)
    );
    public static final DeferredToolMaterial<ToolMaterial> BONE = register("bone", new FossilizationTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.1)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.ATTACK_SPEED, 1.05)
            .set(ToolPropertyInit.MINING_SPEED, 0.6)
            .set(ToolPropertyInit.DEFENSE, 1.2)
            .set(ToolPropertyInit.REACH, 0.7)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 0.7)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 0.85)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.8)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.8)
    );
    public static final DeferredToolMaterial<ToolMaterial> GLASS = register("glass", new FragileTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 1.0)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 1.0)
            .set(ToolPropertyInit.DURABILITY, 0.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NONE)
            .set(ToolPropertyInit.POWER, 1.0)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
    );
    public static final DeferredToolMaterial<ToolMaterial> SANDSTONE = register("sandstone", new TemperatureDifferenceTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.3))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.75)
            .set(ToolPropertyInit.MINING_SPEED, 0.6)
            .set(ToolPropertyInit.DEFENSE, 0.8)
            .set(ToolPropertyInit.REACH, 1.2)
            .set(ToolPropertyInit.SWEEP, 0.9)
            .set(ToolPropertyInit.DURABILITY, 0.4)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.4))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.75)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.6)
            .set(ToolPropertyInit.LAUNCH, 1.2)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.9)
    );
    public static final DeferredToolMaterial<ToolMaterial> MOSS = register("moss", new GrowthTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 0.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.0)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.FLOW, 1.0))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.2)
            .set(ToolPropertyInit.MINING_SPEED, 0.5)
            .set(ToolPropertyInit.DEFENSE, 0.2)
            .set(ToolPropertyInit.REACH, 0.5)
            .set(ToolPropertyInit.SWEEP, 0.5)
            .set(ToolPropertyInit.DURABILITY, 0.2)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NONE)
            .set(ToolPropertyInit.POWER, 0.3)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.FLOW, 1.0))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.2)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.5)
            .set(ToolPropertyInit.LAUNCH, 0.5)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.5)
    );
    public static final DeferredToolMaterial<ToolMaterial> IRON = register("iron", new DuranceTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.2)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.2)
            .set(ToolPropertyInit.DEFENSE, 1.0)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 1.0)
            .set(ToolPropertyInit.DURABILITY, 1.0)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.IRON)
            .set(ToolPropertyInit.POWER, 1.2)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.2)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
    );
    public static final DeferredToolMaterial<ToolMaterial> GOLD = register("gold", new CatalysisTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.1)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.MAGIC, 1.2))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.75)
            .set(ToolPropertyInit.MINING_SPEED, 2.4)
            .set(ToolPropertyInit.DEFENSE, 0.6)
            .set(ToolPropertyInit.REACH, 0.9)
            .set(ToolPropertyInit.SWEEP, 0.7)
            .set(ToolPropertyInit.DURABILITY, 0.2)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.1)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.MAGIC, 1.2))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.75)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 2.4)
            .set(ToolPropertyInit.LAUNCH, 0.9)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.7)
    );
    public static final DeferredToolMaterial<ToolMaterial> AMETHYST = register("amethyst", new ShatterTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.MAGIC, 0.25))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.9)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 0.75)
            .set(ToolPropertyInit.REACH, 1.1)
            .set(ToolPropertyInit.SWEEP, 1.1)
            .set(ToolPropertyInit.DURABILITY, 0.8)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.MAGIC, 0.35))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.1)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.1)
    );
    public static final DeferredToolMaterial<ToolMaterial> CITRINE = register("citrine", new HeatTreatmentTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.25))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.9)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 0.75)
            .set(ToolPropertyInit.REACH, 1.1)
            .set(ToolPropertyInit.SWEEP, 1.1)
            .set(ToolPropertyInit.DURABILITY, 0.8)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.35))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.1)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.1)
    );
    public static final DeferredToolMaterial<ToolMaterial> REDSTONE = register("redstone", new SignalRushTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.5)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.8))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.65)
            .set(ToolPropertyInit.MINING_SPEED, 1.1)
            .set(ToolPropertyInit.DEFENSE, 0.3)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 1.0)
            .set(ToolPropertyInit.DURABILITY, 1.2)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.IRON)
            .set(ToolPropertyInit.POWER, 0.5)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.9))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.65)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.1)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
    );
    public static final DeferredToolMaterial<ToolMaterial> LAPIS = register("lapis", new InclusionTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.TREMOR, 0.5))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 1.9)
            .set(ToolPropertyInit.REACH, 0.8)
            .set(ToolPropertyInit.SWEEP, 0.7)
            .set(ToolPropertyInit.DURABILITY, 0.1)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.TREMOR, 0.6))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 0.8)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.7)
    );
    public static final DeferredToolMaterial<ToolMaterial> CALCITE = register("calcite", new BirefringenceTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.GLACE, 0.4))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
            .set(ToolPropertyInit.MINING_SPEED, 0.9)
            .set(ToolPropertyInit.DEFENSE, 0.4)
            .set(ToolPropertyInit.REACH, 0.9)
            .set(ToolPropertyInit.SWEEP, 0.9)
            .set(ToolPropertyInit.DURABILITY, 0.6)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.GLACE, 0.6))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.8)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.9)
            .set(ToolPropertyInit.LAUNCH, 0.9)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.9)
    );
    public static final DeferredToolMaterial<ToolMaterial> DRIPSTONE = register("dripstone", new PrecipitationTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.5)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.6))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.7)
            .set(ToolPropertyInit.MINING_SPEED, 0.75)
            .set(ToolPropertyInit.DEFENSE, 0.8)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 0.75)
            .set(ToolPropertyInit.DURABILITY, 0.4)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.6))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.7)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.75)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.75)
    );
    public static final DeferredToolMaterial<ToolMaterial> FLUORITE = register("fluorite", new OverchargedTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.0)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.MANA, 1.0))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.1)
            .set(ToolPropertyInit.MINING_SPEED, 0.9)
            .set(ToolPropertyInit.DEFENSE, 0.1)
            .set(ToolPropertyInit.REACH, 0.7)
            .set(ToolPropertyInit.SWEEP, 1.2)
            .set(ToolPropertyInit.DURABILITY, 0.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.3)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.MANA, 1.2))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.1)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.9)
            .set(ToolPropertyInit.LAUNCH, 0.7)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.5)
    );
    public static final DeferredToolMaterial<ToolMaterial> TOURMALINE = register("tourmaline", new ElectricalBoostTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 1.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.2)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.SURGE, 1.2))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 0.2)
            .set(ToolPropertyInit.REACH, 0.4)
            .set(ToolPropertyInit.SWEEP, 1.2)
            .set(ToolPropertyInit.DURABILITY, 0.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 0.5)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.SURGE, 1.0))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 0.4)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.2)
    );
    public static final DeferredToolMaterial<ToolMaterial> DIAMOND = register("diamond", new LightweightTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.5)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.ATTACK_SPEED, 1.1)
            .set(ToolPropertyInit.MINING_SPEED, 1.6)
            .set(ToolPropertyInit.DEFENSE, 0.8)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 4.0)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.DIAMOND)
            .set(ToolPropertyInit.POWER, 1.5)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.none())
            .set(ToolPropertyInit.CHARGE_SPEED, 1.1)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.8)
    );
    public static final DeferredToolMaterial<ToolMaterial> EMERALD = register("emerald", new ConcentrationTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.6))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.9)
            .set(ToolPropertyInit.MINING_SPEED, 1.3)
            .set(ToolPropertyInit.DEFENSE, 1.2)
            .set(ToolPropertyInit.REACH, 1.1)
            .set(ToolPropertyInit.SWEEP, 0.7)
            .set(ToolPropertyInit.DURABILITY, 3.6)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.DIAMOND)
            .set(ToolPropertyInit.POWER, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.FLOW, 0.8))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.3)
            .set(ToolPropertyInit.LAUNCH, 1.1)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.7)
    );
    public static final DeferredToolMaterial<ToolMaterial> ENDER_METAL = register("ender_metal", new EnderDrawTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.HOLLOW, 0.9))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.5)
            .set(ToolPropertyInit.MINING_SPEED, 1.0)
            .set(ToolPropertyInit.DEFENSE, 0.7)
            .set(ToolPropertyInit.REACH, 1.4)
            .set(ToolPropertyInit.SWEEP, 0.6)
            .set(ToolPropertyInit.DURABILITY, 3.2)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.IRON)
            .set(ToolPropertyInit.POWER, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.HOLLOW, 0.9))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.5)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
            .set(ToolPropertyInit.LAUNCH, 1.4)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.6)
    );
    public static final DeferredToolMaterial<ToolMaterial> QUARTZ = register("quartz", new SmoothTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.1)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.GLACE, 0.2))
            .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
            .set(ToolPropertyInit.MINING_SPEED, 1.2)
            .set(ToolPropertyInit.DEFENSE, 0.9)
            .set(ToolPropertyInit.REACH, 1.0)
            .set(ToolPropertyInit.SWEEP, 1.2)
            .set(ToolPropertyInit.DURABILITY, 2.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 1.1)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.GLACE, 0.2))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.2)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.2)
    );
    public static final DeferredToolMaterial<ToolMaterial> GLOWSTONE = register("glowstone", new IlluminationTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.8)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.6))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
            .set(ToolPropertyInit.MINING_SPEED, 1.1)
            .set(ToolPropertyInit.DEFENSE, 1.1)
            .set(ToolPropertyInit.REACH, 0.8)
            .set(ToolPropertyInit.SWEEP, 1.0)
            .set(ToolPropertyInit.DURABILITY, 2.6)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.STONE)
            .set(ToolPropertyInit.POWER, 1.1)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.2))
            .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.2)
            .set(ToolPropertyInit.LAUNCH, 1.0)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.2)
    );
    public static final DeferredToolMaterial<ToolMaterial> SULFURIC_ACID_BATTERY = register("sulfuric_acid_battery", new SparkTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 2.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.9)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.SURGE, 0.6))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.9)
            .set(ToolPropertyInit.MINING_SPEED, 1.2)
            .set(ToolPropertyInit.DEFENSE, 0.6)
            .set(ToolPropertyInit.REACH, 0.7)
            .set(ToolPropertyInit.SWEEP, 0.8)
            .set(ToolPropertyInit.DURABILITY, 3.0)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.IRON)
            .set(ToolPropertyInit.POWER, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.SURGE, 1.0))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 1.2)
            .set(ToolPropertyInit.LAUNCH, 0.7)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.8)
    );
    public static final DeferredToolMaterial<ToolMaterial> NETHERITE = register("netherite", new LavaforgedTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 3.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 1.25)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.5))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.9)
            .set(ToolPropertyInit.MINING_SPEED, 1.8)
            .set(ToolPropertyInit.DEFENSE, 1.1)
            .set(ToolPropertyInit.REACH, 1.1)
            .set(ToolPropertyInit.SWEEP, 1.1)
            .set(ToolPropertyInit.DURABILITY, 5.0)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NETHERITE)
            .set(ToolPropertyInit.POWER, 1.25)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.EMBER, 0.5))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.9)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 0.8)
            .set(ToolPropertyInit.LAUNCH, 1.1)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 1.1)
    );
    public static final DeferredToolMaterial<ToolMaterial> RADIANT_STEEL = register("radiant_steel", new BrillianceTrait(), () -> new ToolProperties(ToolCategoryInit.ALL)
            .set(ToolPropertyInit.TIER, 3.0)
            .set(ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT, 1.0)
            .set(ToolPropertyInit.DAMAGE, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.singleElement(Element.PHANTOM, 1.2))
            .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
            .set(ToolPropertyInit.MINING_SPEED, 2.0)
            .set(ToolPropertyInit.DEFENSE, 0.0)
            .set(ToolPropertyInit.REACH, 1.2)
            .set(ToolPropertyInit.SWEEP, 0.9)
            .set(ToolPropertyInit.DURABILITY, 5.5)
            .set(ToolPropertyInit.MINING_LEVEL, MiningLevel.NETHERITE)
            .set(ToolPropertyInit.POWER, 0.7)
            .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.singleElement(Element.PHANTOM, 1.5))
            .set(ToolPropertyInit.CHARGE_SPEED, 0.8)
            .set(ToolPropertyInit.COOLDOWN_SPEED, 2.0)
            .set(ToolPropertyInit.LAUNCH, 1.2)
            .set(ToolPropertyInit.MANA_EFFICIENCY, 0.9)
    );

    private static float order = 0F;

    private static DeferredToolMaterial<ToolMaterial> register(String name, Trait trait, Supplier<ToolProperties> properties) {
        return REGISTER.register(name, () -> new ToolMaterial(order++, properties, trait));
    }

    public static void registerMaterials(IEventBus bus) {
        Magitech.LOGGER.info("Registering Materials for" + Magitech.MOD_ID);
        REGISTER.register(bus);

        /*WOOD.addStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE, 0));
        WOOD.addSpellCasterStats(new ToolStats(0.4F, 0.4F, 1.1F, 1.2F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE, 0));

        STONE.addStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE, 0));
        STONE.addSpellCasterStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE, 0));

        DEEPSLATE.addStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE, 0));
        DEEPSLATE.addSpellCasterStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE, 0 ));

        COPPER.addStats(new ToolStats(0.8F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE, 0));
        COPPER.addSpellCasterStats(new ToolStats(0.9F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE, 0));

        BONE.addStats(new ToolStats(1.1F, 0F, 1.05F, 0.6F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE, 0));
        BONE.addSpellCasterStats(new ToolStats(1.1F, 0F, 1.05F, 1.9F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE, 0));

        MOSS.addStats(new ToolStats(0F, 1F, 1.2F, 0.5F, 0.2F, 0.5F, 0.5F, 0.2F, Element.FLOW, MiningLevel.NONE, 0));
        MOSS.addSpellCasterStats(new ToolStats(0.3F, 1F, 1.2F, 0.5F, 0.2F, 0.5F, 0.5F, 0.2F, Element.FLOW, MiningLevel.NONE, 0));

        IRON.addStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON, 1));
        IRON.addSpellCasterStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON, 1));

        GOLD.addStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE, 1));
        GOLD.addSpellCasterStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE, 1));

        AMETHYST.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE, 1));
        AMETHYST.addSpellCasterStats(new ToolStats(0.9F, 0.35F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE, 1));

        CITRINE.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.EMBER, MiningLevel.STONE, 1));
        CITRINE.addSpellCasterStats(new ToolStats(0.9F, 0.35F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.EMBER, MiningLevel.STONE, 1));

        REDSTONE.addStats(new ToolStats(0.5F, 0.8F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON, 1));
        REDSTONE.addSpellCasterStats(new ToolStats(0.5F, 0.9F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON, 1));

        LAPIS.addStats(new ToolStats(0.8F, 0.5F, 1F, 1F, 1.9F, 0.8F, 0.7F, 0.1F, Element.TREMOR, MiningLevel.STONE, 1));
        LAPIS.addSpellCasterStats(new ToolStats(0.8F, 0.6F, 1F, 1F, 1.9F, 0.8F, 0.7F, 0.1F, Element.TREMOR, MiningLevel.STONE, 1));

        CALCITE.addStats(new ToolStats(0.7F, 0.4F, 0.8F, 0.9F, 0.4F, 0.9F, 0.9F, 0.6F, Element.GLACE, MiningLevel.STONE, 1));
        CALCITE.addSpellCasterStats(new ToolStats(0.7F, 0.6F, 0.8F, 0.9F, 0.4F, 0.9F, 0.9F, 0.6F, Element.GLACE, MiningLevel.STONE, 1));

        DRIPSTONE.addStats(new ToolStats(0.5F, 0.6F, 0.7F, 0.75F, 0.8F, 1F, 0.75F, 0.4F, Element.FLOW, MiningLevel.STONE, 1));
        DRIPSTONE.addSpellCasterStats(new ToolStats(0.8F, 0.6F, 0.7F, 0.75F, 0.8F, 1F, 0.75F, 0.4F, Element.FLOW, MiningLevel.STONE, 1));

        FLUORITE.addStats(new ToolStats(0F, 1.0F, 1.1F, 0.9F, 0.1F, 0.7F, 1.2F, 0.5F, Element.PHANTOM, MiningLevel.STONE, 1));
        FLUORITE.addSpellCasterStats(new ToolStats(0.3F, 1.2F, 1.1F, 0.9F, 0.1F, 0.7F, 2.0F, 0.5F, Element.PHANTOM, MiningLevel.STONE, 1));

        DIAMOND.addStats(new ToolStats(1.5F, 0F, 1.1F, 1.6F, 0.8F, 1F, 0.8F, 4.0F, Element.NONE, MiningLevel.DIAMOND, 2));
        DIAMOND.addSpellCasterStats(new ToolStats(1.5F, 0F, 1.1F, 1.0F, 0.8F, 1F, 0.8F, 4.0F, Element.NONE, MiningLevel.DIAMOND, 2));

        EMERALD.addStats(new ToolStats(0.9F, 0.6F, 0.9F, 1.3F, 1.2F, 1.1F, 0.7F, 3.6F, Element.FLOW, MiningLevel.DIAMOND, 2));
        EMERALD.addSpellCasterStats(new ToolStats(0.9F, 0.8F, 0.9F, 1.3F, 1.2F, 1.1F, 0.7F, 3.6F, Element.FLOW, MiningLevel.DIAMOND, 2));

        ENDER_METAL.addStats(new ToolStats(0.8F, 0.9F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 3.2F, Element.HOLLOW, MiningLevel.IRON, 2));
        ENDER_METAL.addSpellCasterStats(new ToolStats(0.9F, 0.9F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 3.2F, Element.HOLLOW, MiningLevel.IRON, 2));

        QUARTZ.addStats(new ToolStats(1.1F, 0.2F, 1F, 1.2F, 0.9F, 1F, 1.2F, 2.5F, Element.GLACE, MiningLevel.STONE, 2));
        QUARTZ.addSpellCasterStats(new ToolStats(1.1F, 0.2F, 1F, 1.2F, 0.9F, 1F, 1.2F, 2.5F, Element.GLACE, MiningLevel.STONE, 2));

        GLOWSTONE.addStats(new ToolStats(0.8F, 0.6F, 0.8F, 1.1F, 1.1F, 0.8F, 1F, 2.6F, Element.PHANTOM, MiningLevel.STONE, 2));
        GLOWSTONE.addSpellCasterStats(new ToolStats(0.8F, 0.8F, 0.8F, 1.1F, 1.1F, 0.8F, 1F, 2.6F, Element.PHANTOM, MiningLevel.STONE, 2));

        NETHERITE.addStats(new ToolStats(1.25F, 0.5F, 0.9F, 1.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE, 3));
        NETHERITE.addSpellCasterStats(new ToolStats(1.25F, 0.5F, 0.9F, 0.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE, 3));

        RADIANT_STEEL.addStats(new ToolStats(0.7F, 1.2F, 0.8F, 2F, 0F, 1.2F, 0.9F, 5.5F, Element.GLACE, MiningLevel.NETHERITE, 3));
        RADIANT_STEEL.addSpellCasterStats(new ToolStats(0.7F, 1.5F, 0.8F, 2F, 0F, 1.2F, 0.9F, 5.5F, Element.GLACE, MiningLevel.NETHERITE, 3));

        FRIGIDITE.addStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE, 4));
        FRIGIDITE.addSpellCasterStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE, 4));

        TRANSLUCIUM.addStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.7F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE, 4));
        TRANSLUCIUM.addSpellCasterStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.5F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE, 4));

        RESONITE.addStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.7F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE, 4));
        RESONITE.addSpellCasterStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.0F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE, 4));

        ABYSSITE.addStats(new ToolStats(0.5F, 1.7F, 1F, 1.7F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE, 4));
        ABYSSITE.addSpellCasterStats(new ToolStats(0.5F, 1.7F, 1F, 1.1F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE, 4));*/
    }
}
