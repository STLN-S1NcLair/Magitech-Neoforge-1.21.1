package net.stln.magitech.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ToolUpgradeTrigger extends SimpleCriterionTrigger<ToolUpgradeTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<ToolUpgradeTrigger.TriggerInstance> codec() {
        return ToolUpgradeTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack item, int tier) {
        this.trigger(player, p_27675_ -> p_27675_.matches(item, tier));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints tier)
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ToolUpgradeTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                p_337356_ -> p_337356_.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ToolUpgradeTrigger.TriggerInstance::player),
                                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ToolUpgradeTrigger.TriggerInstance::item),
                                MinMaxBounds.Ints.CODEC.optionalFieldOf("tier", MinMaxBounds.Ints.ANY).forGetter(ToolUpgradeTrigger.TriggerInstance::tier)
                        )
                        .apply(p_337356_, ToolUpgradeTrigger.TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> toolUpgrade() {
            return CriterionInit.TOOL_UPGRADE.get().createCriterion(new ToolUpgradeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), MinMaxBounds.Ints.ANY));
        }

        public boolean matches(ItemStack stack, int tier) {
            return (item.isEmpty() || item.get().test(stack)) && this.tier.matches(tier);
        }
    }
}
