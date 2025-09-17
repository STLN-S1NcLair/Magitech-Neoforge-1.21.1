package net.stln.magitech.magic.spell;

import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpellRegister {

    private static final Map<ResourceLocation, Spell> register = new HashMap<>();

    public static @NotNull Map<ResourceLocation, Spell> getRegister() {
        return Map.copyOf(register);
    }
    
    public static @NotNull Set<ResourceLocation> getSpellIds() {
        return register.keySet();
    }

    public static @NotNull Spell registerSpell(@NotNull ResourceLocation id, @NotNull Spell spell) {
        register.put(id, spell);
        return spell;
    }

    public static @Nullable Spell getSpell(@NotNull ResourceLocation id) {
        return register.get(id);
    }

    public static @NotNull DataResult<Spell> getSpellResult(@NotNull ResourceLocation id) {
        Spell spell = getSpell(id);
        return spell == null ? DataResult.error(() -> "Unknown spell: " + id) : DataResult.success(spell);
    }

    public static @Nullable ResourceLocation getId(@NotNull Spell spell) {
        for (Map.Entry<ResourceLocation, Spell> entry : register.entrySet()) {
            if (entry.getValue().getClass().equals(spell.getClass())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static @NotNull Optional<ResourceLocation> getOptionalId(@NotNull Spell spell) {
        return Optional.ofNullable(getId(spell));
    }

    public static @NotNull DataResult<ResourceLocation> getSpellIdResult(@NotNull Spell spell) {
        ResourceLocation id = getId(spell);
        return id == null ? DataResult.error(() -> "Unregistered spell: " + spell) : DataResult.success(id);
    }
}
