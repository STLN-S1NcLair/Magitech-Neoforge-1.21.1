package net.stln.magitech.item.tool;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.StringRepresentable;
import net.stln.magitech.item.tool.material.MiningLevel;
import net.stln.magitech.item.tool.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ToolStatsNew {
    public static final ToolStatsNew DEFAULT = new ToolStatsNew(Map.of(), Element.NONE, MiningLevel.NONE, 0);

    public static Builder builder() {
        return new Builder();
    }

    public static @NotNull ToolStatsNew combine(@NotNull List<ToolStatsNew> others) {
        return combineBuilder(others, true).build();
    }

    public static @NotNull ToolStatsNew combineWithoutElement(@NotNull List<ToolStatsNew> others) {
        return combineBuilder(others, false).build();
    }

    private static @NotNull ToolStatsNew.Builder combineBuilder(@NotNull List<ToolStatsNew> others, boolean includeElement) {
        var builder = builder();
        HashMap<Element, Float> elementMap = new HashMap<>();

        for (ToolStatsNew other : others) {
            // othersの値を合体
            other.statsMap.forEach((builder::addStats));
            // Elementによる補正
            Element element = other.getElement();
            float elementAttack = builder.getStats(StatsType.ELEMENT_ATTACK);
            elementMap.compute(element, ((element1, aFloat) -> aFloat == null ? elementAttack : aFloat + elementAttack));
            // 採掘レベルの上書き
            MiningLevel miningLevel = other.getMiningLevel();
            if (miningLevel.getTier() > builder.miningLevel.getTier()) {
                builder.setMiningLevel(miningLevel);
            }
            builder.addTier(other.getTier());
        }
        if (!includeElement) return builder;

        var elementAttack = builder.getStats(StatsType.ELEMENT_ATTACK);
        List<Element> elements = List.of(Element.EMBER, Element.GLACE, Element.SURGE, Element.PHANTOM, Element.TREMOR, Element.MAGIC, Element.FLOW, Element.HOLLOW);
        for (Element element : elements) {

            if (elementMap.getOrDefault(element, 0f) > elementAttack) {
                builder.setElement(element);
                elementAttack = elementMap.getOrDefault(element, 0f);
            } else if (elementMap.getOrDefault(element, 0f) == elementAttack) {
                builder.setElement(Element.NONE);
                elementAttack = 0f;
            }
        }

        var currentElement = builder.getElement();
        for (Element element : elements) {
            if (element != currentElement) {
                elementAttack += elementMap.getOrDefault(element, 0f) * 0.75f;
            }
        }

        List<Element> elements1 = ImmutableList.<Element>builder()
                .addAll(elements)
                .add(Element.NONE)
                .build();
        for (Element element : elements1) {
            if (element != currentElement) {
                elementAttack += elementMap.getOrDefault(element, 0f) * (element == Element.NONE ? 1f : 0.5f);
            }
        }

        builder.setStats(StatsType.ELEMENT_ATTACK, elementAttack);
        return builder;
    }

    private final Map<StatsType, Float> statsMap;
    private final Element element;
    private final MiningLevel miningLevel;
    private final int tier;

    private ToolStatsNew(@NotNull Map<StatsType, Float> statsMap, @NotNull Element element, @NotNull MiningLevel miningLevel, int tier) {
        this.statsMap = statsMap;
        this.element = element;
        this.miningLevel = miningLevel;
        this.tier = tier;
    }

    public float getStat(@NotNull StatsType statsType) {
        return statsMap.getOrDefault(statsType, 0f);
    }

    public @NotNull Element getElement() {
        return element;
    }

    public @NotNull MiningLevel getMiningLevel() {
        return miningLevel;
    }

    public int getTier() {
        return tier;
    }

    public static class Builder {
        private final Map<StatsType, Float> builder = new HashMap<>();
        private Element element = Element.NONE;
        private MiningLevel miningLevel = MiningLevel.NONE;
        private int tier = 0;

        Builder() {
        }

        public float getStats(StatsType statsType) {
            return builder.getOrDefault(statsType, 0f);
        }

        public Builder addStats(StatsType statsType, float value) {
            builder.compute(statsType, (statsType1, old) -> old != null ? old + value : value);
            return this;
        }

        public Builder setStats(StatsType statsType, float value) {
            builder.put(statsType, value);
            return this;
        }

        public Element getElement() {
            return this.element;
        }

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setMiningLevel(MiningLevel miningLevel) {
            this.miningLevel = miningLevel;
            return this;
        }

        public Builder addTier(int tier) {
            this.tier += tier;
            return this;
        }

        public Builder setTier(int tier) {
            this.tier = tier;
            return this;
        }

        public ToolStatsNew build() {
            return new ToolStatsNew(Map.copyOf(builder), element, miningLevel, tier);
        }
    }

    public enum StatsType implements StringRepresentable {
        ATTACK,
        ELEMENT_ATTACK,
        ATTACK_SPEED,
        MINING_SPEED,
        DEFENCE,
        ATTACK_RANGE,
        SWEEP_RANGE,
        DURABILITY,
        ;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
