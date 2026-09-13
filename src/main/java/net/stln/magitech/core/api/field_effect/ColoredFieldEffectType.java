package net.stln.magitech.core.api.field_effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * フィールド効果の処理と表示を定義する基底クラスです。
 * Base class defining field-effect processing and visualization.
 */
public abstract class ColoredFieldEffectType extends FieldEffectType {

    public abstract Color getPrimary();

    public abstract Color getSecondary();
}
