package net.stln.magitech.feature.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.property.SpellProperties;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyKey;

import java.util.Optional;

public record SpellConfig(Element element, SpellShape shape, int cooldown, int cost, boolean continuous, Optional<Long> costPerTick, boolean hasCharge, Optional<Integer> chargeTime, SpellProperties properties,
                          Optional<ResourceLocation> castAnim, Optional<ResourceLocation> tickAnim, Optional<ResourceLocation> endAnim) {

    public class Builder {
        private Element element;
        private SpellShape shape;
        private int cooldown;
        private int cost;
        private boolean continuous;
        private Long costPerTick;
        private boolean hasCharge;
        private Integer chargeTime;
        private final SpellProperties properties = SpellProperties.create();
        private ResourceLocation castAnim;
        private ResourceLocation tickAnim;
        private ResourceLocation endAnim;

        Builder(Element element, SpellShape shape, int cooldown, int cost) {
            this.element = element;
            this.shape = shape;
            this.cooldown = cooldown;
            this.cost = cost;
        }

        public Builder continuous(long costPerTick) {
            this.continuous = true;
            this.costPerTick = costPerTick;
            return this;
        }

        public Builder charge(int chargeTime) {
            this.hasCharge = true;
            this.chargeTime = chargeTime;
            return this;
        }

        public <T> Builder property(SpellPropertyKey<T> key, T value) {
            this.properties.put(key, value);
            return this;
        }

        public Builder castAnim(ResourceLocation castAnim) {
            this.castAnim = castAnim;
            return this;
        }

        public Builder tickAnim(ResourceLocation tickAnim) {
            this.tickAnim = tickAnim;
            return this;
        }

        public Builder endAnim(ResourceLocation endAnim) {
            this.endAnim = endAnim;
            return this;
        }

        public SpellConfig build() {
            return new SpellConfig(element, shape, cooldown, cost, continuous, Optional.ofNullable(costPerTick), hasCharge, Optional.ofNullable(chargeTime), properties,
                    Optional.ofNullable(castAnim), Optional.ofNullable(tickAnim), Optional.ofNullable(endAnim));
        }
    }
}
