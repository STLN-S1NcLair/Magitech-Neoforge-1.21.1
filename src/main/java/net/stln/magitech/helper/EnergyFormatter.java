package net.stln.magitech.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class EnergyFormatter {

    // 小数点以下2桁まで表示するフォーマッタ
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    private static final BigDecimal KILO = BigDecimal.valueOf(1_000L);
    private static final BigDecimal MEGA = BigDecimal.valueOf(1_000_000L);
    private static final BigDecimal GIGA = BigDecimal.valueOf(1_000_000_000L);
    private static final BigDecimal TERA = BigDecimal.valueOf(1_000_000_000_000L);
    private static final BigDecimal PETA = BigDecimal.valueOf(1_000_000_000_000_000L);
    private static final BigDecimal EXA = BigDecimal.valueOf(1_000_000_000_000_000_000L);

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
        return Component.literal((flow > 0 ? "+" : "") + formatAverage(flow).getString() + " / " + formatAverage(maxFlow).getString());
    }

    // 平均表示のフォーマット
    public static MutableComponent formatAverage(long value) {
        return Component.literal(formatValue(value) + "/t");
    }

    // 単体の値をフォーマットするメソッド
    public static String formatValue(long val) {
        if (val == Long.MAX_VALUE || val == Long.MIN_VALUE) {
            return "∞ J";
        }
        BigDecimal valDecimal = BigDecimal.valueOf(val);
        BigDecimal abs = valDecimal.abs();
        if (abs.compareTo(EXA) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(EXA, MathContext.DECIMAL64)) + " EJ";
        }
        if (abs.compareTo(PETA) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(PETA, MathContext.DECIMAL64)) + " PJ";
        }
        if (abs.compareTo(TERA) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(TERA, MathContext.DECIMAL64)) + " TJ";
        }
        if (abs.compareTo(GIGA) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(GIGA, MathContext.DECIMAL64)) + " GJ";
        }
        if (abs.compareTo(MEGA) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(MEGA, MathContext.DECIMAL64)) + " MJ";
        }
        if (abs.compareTo(KILO) >= 0) {
            return DECIMAL_FORMAT.format(valDecimal.divide(KILO, MathContext.DECIMAL64)) + " kJ";
        }

        // それ未満はそのまま J
        return val + " J";
    }
}
