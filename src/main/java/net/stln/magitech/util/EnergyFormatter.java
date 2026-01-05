package net.stln.magitech.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.text.DecimalFormat;

public class EnergyFormatter {

    // 小数点以下2桁まで表示するフォーマッタ
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    /**
     * kJ単位のエネルギー量を、SI接頭辞（M, G, T, P）を使った短い形式に変換します。
     * 例: 2000 kJ -> "2 MJ"
     * 1500 kJ -> "1.5 MJ"
     */
    public static MutableComponent formatEnergy(long energy, long maxEnergy) {
        // 現在値と最大値をそれぞれフォーマットして "現在の値 / 最大値 単位" の形式にする場合
        // ここではシンプルに「現在の値 単位」を返すロジックを中心に記述します
        return Component.literal(formatValue(energy) + " / " + formatValue(maxEnergy));
    }

    // 流量のフォーマット
    public static MutableComponent formatFlow(long flow, long maxFlow) {
        return Component.literal((flow > 0 ? "+" : "") + formatValue(flow) + "/t / " + formatValue(maxFlow) + "/t");
    }

    // 単体の値をフォーマットするメソッド
    public static String formatValue(long val) {
        if (Math.abs(val) >= 1_000_000_000_000_000_000L) {
            return DECIMAL_FORMAT.format(val / 1_000_000_000_000.0) + " EJ";
        }
        if (Math.abs(val) >= 1_000_000_000_000_000L) {
            return DECIMAL_FORMAT.format(val / 1_000_000_000_000.0) + " PJ";
        }
        if (Math.abs(val) >= 1_000_000_000_000L) {
            return DECIMAL_FORMAT.format(val / 1_000_000_000_000.0) + " TJ";
        }
        // 1,000,000,000 kJ = 1 TJ (Tera)
        if (Math.abs(val) >= 1_000_000_000L) {
            return DECIMAL_FORMAT.format(val / 1_000_000_000.0) + " GJ";
        }
        // 1,000,000 kJ = 1 GJ (Giga)
        if (Math.abs(val) >= 1_000_000L) {
            return DECIMAL_FORMAT.format(val / 1_000_000.0) + " MJ";
        }
        // 1,000 kJ = 1 MJ (Mega)
        if (Math.abs(val) >= 1_000L) {
            return DECIMAL_FORMAT.format(val / 1_000.0) + " kJ";
        }

        // それ未満はそのまま kJ
        return val + " J";
    }
}
