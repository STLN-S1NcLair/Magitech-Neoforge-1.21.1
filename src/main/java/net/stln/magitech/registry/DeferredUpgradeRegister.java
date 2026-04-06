package net.stln.magitech.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.upgrade.Upgrade;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredUpgradeRegister extends DeferredRegister<Upgrade> {
    public DeferredUpgradeRegister(String namespace) {
        super(MagitechRegistries.Keys.UPGRADE, namespace);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Upgrade> @NotNull DeferredUpgrade<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (DeferredUpgrade<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Upgrade> @NotNull DeferredUpgrade<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (DeferredUpgrade<I>) super.register(name, func);
    }

    @Override
    protected <I extends Upgrade> @NotNull DeferredUpgrade<I> createHolder(@NotNull ResourceKey<? extends Registry<Upgrade>> registryKey, @NotNull ResourceLocation key) {
        return new DeferredUpgrade<>(key);
    }
}
