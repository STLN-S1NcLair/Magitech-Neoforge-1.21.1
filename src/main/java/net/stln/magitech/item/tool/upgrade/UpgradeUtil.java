package net.stln.magitech.item.tool.upgrade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;

import java.util.*;

public class UpgradeUtil {

    public static List<Upgrade> getUpgrades(int size, int seed, ItemStack stack) {
        Map<ResourceLocation, Upgrade> upgradeMap = UpgradeRegister.getDictId();
        if (stack.getItem() instanceof SpellCasterItem) {
            upgradeMap = UpgradeRegister.getSpellCasterDictId();
        }
        return pickRandomValues(upgradeMap, seed, size);
    }

    public static <K, V> List<V> pickRandomValues(Map<K, V> map, long seed, int count) {
        // RandomSourceでseed固定
        RandomSource random = RandomSource.create(seed);

        // valuesをリスト化
        List<V> values = new ArrayList<>(map.values());

        // ランダムにシャッフル
        Collections.shuffle(values, new Random(random.nextLong()));

        // 指定数だけ取り出す（要素数が足りなければ全部返す）
        return values.subList(0, Math.min(count, values.size()));
    }
}
