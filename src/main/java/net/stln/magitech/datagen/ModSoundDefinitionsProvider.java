package net.stln.magitech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.sound.SoundInit;

import java.util.function.Supplier;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Magitech.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        Supplier<SoundEvent>[] simpleSounds = new Supplier[]{
                SoundInit.PHANTOM_BUFF, SoundInit.GEOMENDING_BREAK, SoundInit.PHANTOM_SLAYER_DASH, SoundInit.FROST_BREAK,
                SoundInit.ZAP, SoundInit.FLAME, SoundInit.SONICBOOM, SoundInit.MYSTICAL, SoundInit.GLACE_LAUNCH,
                SoundInit.SPARK, SoundInit.BLOW, SoundInit.ARCALETH, SoundInit.FIREBALL, SoundInit.TREMIVOX,
                SoundInit.AELTHERIN, SoundInit.NULLIXIS, SoundInit.NIVALUNE, SoundInit.FULGENZA, SoundInit.FULGENZA_CHARGE,
                SoundInit.VOIDLANCE, SoundInit.MYSTAVEN, SoundInit.PHANTASTRA, SoundInit.PYROLUX, SoundInit.FLUVINAE,
                SoundInit.VEILMIST, SoundInit.SONISTORM, SoundInit.GLYMORA, SoundInit.TENEBRISOL, SoundInit.ECHOLOCATION,
                SoundInit.ENERCRUX, SoundInit.BLAZEWEND, SoundInit.GLISTELDA, SoundInit.GLISTELDA_BREAK, SoundInit.FADANCEA,
                SoundInit.QUAVERIS, SoundInit.ENVISTRA, SoundInit.DISPARUNDRA, SoundInit.SYLLAEZE, SoundInit.ARCLUME,
                SoundInit.NYMPHORA, SoundInit.TENEBPORT, SoundInit.TENEBPORT_CHARGE, SoundInit.VOLKARIN,
                SoundInit.VOLKARIN_SHOOT, SoundInit.FROSBLAST, SoundInit.FROSBLAST_SHOOT, SoundInit.ELECTROIDE,
                SoundInit.ELECTROIDE_SHOOT, SoundInit.ILLUSFLARE, SoundInit.ILLUSFLARE_SHOOT, SoundInit.SHOCKVANE,
                SoundInit.SHOCKVANE_SHOOT, SoundInit.HEXFLARE, SoundInit.HEXFLARE_SHOOT, SoundInit.HYDRELUX,
                SoundInit.HYDRELUX_SHOOT, SoundInit.HYDRELUX_BOUNCE, SoundInit.NIHILFLARE, SoundInit.NIHILFLARE_SHOOT,
                SoundInit.ARDOVITAE, SoundInit.MYSTPHEL, SoundInit.HYDRAERUN, SoundInit.LUXGRAIL, SoundInit.LUXGRAIL_CHARGE,
                SoundInit.AETHER_LIFTER_JUMP, SoundInit.FLAMGLIDE_STRIDER_JUMP, SoundInit.ATHANOR_PILLAR_INFUSION,
                SoundInit.ATHANOR_PILLAR_ZAP, SoundInit.MANA_NODE, SoundInit.INFUSION_ALTAR, SoundInit.INFUSION_ALTAR_CRAFT,
                SoundInit.MANA_PARCEL, SoundInit.BURNER, SoundInit.CHILLER, SoundInit.AETHERIX, SoundInit.THAUMIRIS, SoundInit.ESFOUNTIA, SoundInit.QUINTEX,
                SoundInit.WEAVER_DEATH
        };
        for (Supplier<SoundEvent> soundEvent : simpleSounds) {
            add(SoundInit.holder(soundEvent), SoundDefinition.definition().with(sound(SoundInit.id(soundEvent))));
        }

        add(SoundInit.holder(SoundInit.CRYSTAL_BREAK), definitionWithPitch("subtitles.block.generic.break", SoundInit.CRYSTAL_BREAK, 3));
        add(SoundInit.holder(SoundInit.CRYSTAL_PLACE), definitionWithPitch("subtitles.block.generic.place", SoundInit.CRYSTAL_PLACE, 3));
        add(SoundInit.holder(SoundInit.CRYSTAL_HIT), definitionWithPitch("subtitles.block.generic.hit", SoundInit.CRYSTAL_STEP, 3));
        add(SoundInit.holder(SoundInit.CRYSTAL_STEP), definitionWithPitch("subtitles.block.generic.footsteps", SoundInit.CRYSTAL_STEP, 3));
        add(SoundInit.holder(SoundInit.CRYSTAL_FALL), definitionWithPitch("subtitles.block.generic.hit", SoundInit.CRYSTAL_STEP, 3));
        add(SoundInit.holder(SoundInit.ALCHECRYSITE_BREAK), definition("subtitles.block.generic.break", SoundInit.ALCHECRYSITE_BREAK, 4));
        add(SoundInit.holder(SoundInit.ALCHECRYSITE_PLACE), definition("subtitles.block.generic.place", SoundInit.ALCHECRYSITE_PLACE, 6));
        add(SoundInit.holder(SoundInit.ALCHECRYSITE_HIT), definition("subtitles.block.generic.hit", SoundInit.ALCHECRYSITE_STEP, 6));
        add(SoundInit.holder(SoundInit.ALCHECRYSITE_STEP), definition("subtitles.block.generic.footsteps", SoundInit.ALCHECRYSITE_STEP, 6));
        add(SoundInit.holder(SoundInit.ALCHECRYSITE_FALL), definition("subtitles.block.generic.hit", SoundInit.ALCHECRYSITE_STEP, 6));
        add(SoundInit.holder(SoundInit.WEAVER_HURT), definition(SoundInit.WEAVER_HURT, 4));
    }

    private SoundDefinition definition(String subtitle, Supplier<SoundEvent> prefix, int count) {
        SoundDefinition definition = SoundDefinition.definition().subtitle(subtitle);
        addSounds(definition, prefix, count);
        return definition;
    }

    private SoundDefinition definition(Supplier<SoundEvent> prefix, int count) {
        SoundDefinition definition = SoundDefinition.definition();
        addSounds(definition, prefix, count);
        return definition;
    }

    private SoundDefinition definitionWithPitch(String subtitle, Supplier<SoundEvent> prefix, int count) {
        SoundDefinition definition = SoundDefinition.definition().subtitle(subtitle);
        for (float pitch : new float[]{0.8F, 1.0F, 1.2F}) {
            for (int index = 1; index <= count; index++) {
                definition.with(sound(soundId(prefix, index)).pitch(pitch));
            }
        }
        return definition;
    }

    private void addSounds(SoundDefinition definition, Supplier<SoundEvent> prefix, int count) {
        for (int index = 1; index <= count; index++) {
            definition.with(sound(soundId(prefix, index)));
        }
    }

    private ResourceLocation soundId(Supplier<SoundEvent> prefix, int index) {
        return SoundInit.id(prefix).withSuffix("_" + index);
    }
}
