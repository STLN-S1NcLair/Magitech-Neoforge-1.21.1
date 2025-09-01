package net.stln.magitech.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CriterionInit {

    public static final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGER = DeferredRegister.create(Registries.TRIGGER_TYPE, net.stln.magitech.Magitech.MOD_ID);

    public static final Supplier<ToolUpgradeTrigger> TOOL_UPGRADE = CRITERION_TRIGGER.register("tool_upgrade", ToolUpgradeTrigger::new);

    public static void registerCrtiteria(IEventBus bus) {
        CRITERION_TRIGGER.register(bus);
    }
}
