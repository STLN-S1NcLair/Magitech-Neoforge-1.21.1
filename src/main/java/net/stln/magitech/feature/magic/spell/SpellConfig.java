package net.stln.magitech.feature.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.property.SpellProperties;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyKey;

import java.util.Optional;
import java.util.function.Supplier;

public record SpellConfig(Element element, SpellShape shape, int cooldown, float cost, boolean continuous,
                          Optional<Float> costPerTick, boolean hasCharge, Optional<Integer> chargeTime,
                          SpellProperties properties,
                          Optional<ResourceLocation> castAnim, Optional<ResourceLocation> tickAnim,
                          Optional<ResourceLocation> endAnim,
                          Optional<Supplier<SoundEvent>> castSound, Optional<Supplier<SoundEvent>> tickSound,
                          Optional<Supplier<SoundEvent>> chargeSound, Optional<Supplier<SoundEvent>> endSound,
                          int tickSoundDelay) {

    public static class Builder {
        private final Element element;
        private final SpellShape shape;
        private final int cooldown;
        private final float cost;
        private boolean continuous;
        private Float costPerTick;
        private boolean hasCharge;
        private Integer chargeTime;
        private final SpellProperties properties = SpellProperties.create();
        private ResourceLocation castAnim;
        private ResourceLocation tickAnim;
        private ResourceLocation endAnim;
        private Supplier<SoundEvent> castSound;
        private Supplier<SoundEvent> tickSound;
        private Supplier<SoundEvent> chargeSound;
        private Supplier<SoundEvent> endSound;
        private int tickSoundDelay = 0;

        public Builder(Element element, SpellShape shape, int cooldown, float cost) {
            this.element = element;
            this.shape = shape;
            this.cooldown = cooldown;
            this.cost = cost;
        }

        public Builder continuous(float costPerTick) {
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

        public Builder castAnim(String path) {
            this.castAnim = Magitech.id(path);
            return this;
        }

        public Builder tickAnim(String path) {
            this.tickAnim = Magitech.id(path);
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

        public Builder endAnim(String path) {
            this.endAnim = Magitech.id(path);
            return this;
        }

        public Builder castSound(Supplier<SoundEvent> castSound) {
            this.castSound = castSound;
            return this;
        }

        public Builder tickSound(Supplier<SoundEvent> tickSound, int tickSoundDelay) {
            this.tickSound = tickSound;
            this.tickSoundDelay = tickSoundDelay;
            return this;
        }

        public Builder chargeSound(Supplier<SoundEvent> chargeSound) {
            this.chargeSound = chargeSound;
            return this;
        }

        public Builder endSound(Supplier<SoundEvent> endSound) {
            this.endSound = endSound;
            return this;
        }

        public SpellConfig build() {
            return new SpellConfig(element, shape, cooldown, cost, continuous, Optional.ofNullable(costPerTick), hasCharge, Optional.ofNullable(chargeTime), properties,
                    Optional.ofNullable(castAnim), Optional.ofNullable(tickAnim), Optional.ofNullable(endAnim), Optional.ofNullable(castSound), Optional.ofNullable(tickSound), Optional.ofNullable(chargeSound), Optional.ofNullable(endSound), tickSoundDelay);
        }
    }
}
