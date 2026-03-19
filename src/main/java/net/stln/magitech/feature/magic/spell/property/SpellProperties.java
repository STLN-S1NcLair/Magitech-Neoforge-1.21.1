package net.stln.magitech.feature.magic.spell.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record SpellProperties(Map<SpellProperty<?>, Object> map) {

    public static SpellProperties create() {
        return new SpellProperties(new HashMap<>());
    }

    public <T> void put(SpellProperty<T> key, T value) {
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SpellProperty<T> key) {
        return (T) map.get(key);
    }

    public <T> Optional<T> getOptional(SpellProperty<T> key) {
        if (map.containsKey(key)) {
            return Optional.ofNullable(get(key));
        } else {
            return Optional.empty();
        }
    }

    public void combine(SpellProperties oldP, SpellProperties newP) {
        for (Map.Entry<SpellProperty<?>, Object> entry : newP.map().entrySet()) {
            oldP.map().put(entry.getKey(), entry.getValue());
        }
    }

    public boolean contains(SpellProperty<?> key) {
        return map.containsKey(key);
    }
}
