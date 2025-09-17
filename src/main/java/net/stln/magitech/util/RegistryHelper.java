package net.stln.magitech.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RegistryHelper {
    public static <T> @NotNull Optional<ResourceLocation> getId(@NotNull Holder<T> holder) {
        if (holder instanceof DeferredHolder<?, ?> deferredHolder) {
            return Optional.of(deferredHolder.getId());
        } else {
            return holder.unwrapKey().map(ResourceKey::location);
        }
    }

    public static <T> @Nullable ResourceLocation getIdOrNull(@NotNull Holder<T> holder) {
        return getId(holder).orElse(null);
    }
}
