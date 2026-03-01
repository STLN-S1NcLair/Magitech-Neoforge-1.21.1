package net.stln.magitech.feature.magic.spell.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record SpellProperties(Map<SpellPropertyKey<?>, Object> map) {

    public static SpellProperties create() {
        return new SpellProperties(new HashMap<>());
    }

    public <T> void put(SpellPropertyKey<T> key, T value) {
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SpellPropertyKey<T> key) {
        return (T) map.get(key);
    }

    public <T> Optional<T> getOptional(SpellPropertyKey<T> key) {
        if (map.containsKey(key)) {
            return Optional.ofNullable(get(key));
        } else {
            return Optional.empty();
        }
    }

    public void combine(SpellProperties oldP, SpellProperties newP) {
        for (Map.Entry<SpellPropertyKey<?>, Object> entry : newP.map().entrySet()) {
            oldP.map().put(entry.getKey(), entry.getValue());
        }
    }

    public boolean contains(SpellPropertyKey<?> key) {
        return map.containsKey(key);
    }
}
