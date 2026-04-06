package net.stln.magitech.feature.tool.upgrade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class UpgradeUtil {

    public static List<Upgrade> getUpgrades(int size, int seed, ItemStack stack) {
        SynthesisedToolItem item = (SynthesisedToolItem) stack.getItem();
        Set<Upgrade> upgrades = MagitechRegistries.UPGRADE.entrySet().stream().map(Map.Entry::getValue).filter(upgrade -> upgrade.applicable(item.getToolType().asToolType().defaultProperties().get().getCategory())).collect(Collectors.toSet());
        return pickRandomValues(upgrades, seed, size);
    }

    public static List<Upgrade> pickRandomValues(Set<Upgrade> upgrades, long seed, int count) {
        // RandomSourceでseed固定
        RandomSource random = RandomSource.create(seed);

        ArrayList<Upgrade> list = new ArrayList<>(upgrades);

        // ランダムにシャッフル
        Collections.shuffle(list, new Random(random.nextLong()));

        // 指定数だけ取り出す（要素数が足りなければ全部返す）
        return list.subList(0, Math.min(count, list.size()));
    }
}
