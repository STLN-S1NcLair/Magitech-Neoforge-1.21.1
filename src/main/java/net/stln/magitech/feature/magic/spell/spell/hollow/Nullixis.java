package net.stln.magitech.feature.magic.spell.spell.hollow;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.content.entity.magicentity.nullixis.NullixisEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

import java.util.HashMap;
import java.util.Map;

public class Nullixis extends ShotSpell {

    public Nullixis() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.SHOT, 40, 55)
                        .property(SpellPropertyInit.DAMAGE, 6.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.3F)
                        .endSound(SoundInit.NULLIXIS)
                        .endAnim("swing_wand"),
                NullixisEntity::new);
    }
}
