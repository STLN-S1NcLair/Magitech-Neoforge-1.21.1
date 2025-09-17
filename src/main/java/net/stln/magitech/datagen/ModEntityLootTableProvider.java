package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.item.ItemInit;

public class ModEntityLootTableProvider extends EntityLootSubProvider {
    protected ModEntityLootTableProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), FeatureFlagSet.of(), registries);
    }

    @Override
    public void generate() {
        this.add(EntityInit.WEAVER_ENTITY.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(TimeCheck.time(IntRange.range(2000, 10000)).setPeriod(24000))
                                .add(LootItem.lootTableItem(ItemInit.AGGREGATED_LUMINIS.get()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(TimeCheck.time(IntRange.range(10000, 14000)).setPeriod(24000)
                                        .or(TimeCheck.time(IntRange.range(22000, 24000)).setPeriod(24000))
                                        .or(TimeCheck.time(IntRange.range(0, 2000)).setPeriod(24000)))
                                .add(LootItem.lootTableItem(ItemInit.AGGREGATED_FLUXIA.get()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(TimeCheck.time(IntRange.range(14000, 22000)).setPeriod(24000))
                                .add(LootItem.lootTableItem(ItemInit.AGGREGATED_NOCTIS.get()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
        );
    }
}
