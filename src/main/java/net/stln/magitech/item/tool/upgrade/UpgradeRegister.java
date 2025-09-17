package net.stln.magitech.item.tool.upgrade;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UpgradeRegister {
    private static final Map<ResourceLocation, Upgrade> dictId = new LinkedHashMap<>();

    public static void registerId(ResourceLocation id, Upgrade upgrade) {
        dictId.put(id, upgrade);
    }

    private static final Map<ResourceLocation, Upgrade> dictSpellCasterId = new LinkedHashMap<>();

    public static void registerSpellCasterId(ResourceLocation id, Upgrade upgrade) {
        dictSpellCasterId.put(id, upgrade);
    }

    public static Upgrade getUpgrade(ResourceLocation id) {
        return dictId.get(id);
    }

    public static Map<ResourceLocation, Upgrade> getDictId() {
        return dictId;
    }

    public static Upgrade getSpellCasterUpgrade(ResourceLocation id) {
        return dictSpellCasterId.get(id);
    }

    public static Upgrade getUpgradeFromAll(ResourceLocation id) {
        return dictId.containsKey(id) ? dictId.get(id) : dictSpellCasterId.get(id);
    }

    public static Map<ResourceLocation, Upgrade> getSpellCasterDictId() {
        return dictSpellCasterId;
    }

    public static int hasUpgrade(@Nullable Upgrade upgrade) {
        for (Map.Entry<ResourceLocation, Upgrade> entry : dictId.entrySet()) {
            if (Objects.equals(entry.getValue(), upgrade)) {
                return 0;
            }
        }
        for (Map.Entry<ResourceLocation, Upgrade> entry : dictSpellCasterId.entrySet()) {
            if (Objects.equals(entry.getValue(), upgrade)) {
                return 1;
            }
        }
        return -1;
    }

    public static @Nullable ResourceLocation getId(@Nullable Upgrade upgrade) {
        for (Map.Entry<ResourceLocation, Upgrade> entry : dictId.entrySet()) {
            if (Objects.equals(entry.getValue(), upgrade)) {
                return entry.getKey();
            }
        }
        for (Map.Entry<ResourceLocation, Upgrade> entry : dictSpellCasterId.entrySet()) {
            if (Objects.equals(entry.getValue(), upgrade)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static @NotNull Optional<ResourceLocation> getOptionalId(@Nullable Upgrade upgrade) {
        return Optional.ofNullable(getId(upgrade));
    }
}
