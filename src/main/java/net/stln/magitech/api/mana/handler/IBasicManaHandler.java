package net.stln.magitech.api.mana.handler;

import net.stln.magitech.api.mana.flow.ManaFlowRule;

public interface IBasicManaHandler extends IManaHandler {

    /**
     * 充填率バイアス、入出力可否を取得する。
     * 計算上の充填率(FillRatio)にこの値を加算して判定を行う。
     * * @return
     * - 負の値 (例: -1.0): 常に不足しているように振る舞う (優先充填 / 集める)
     * - 正の値 (例: +1.0): 常に余っているように振る舞う (優先排出 / 配る)
     * - 0.0 : 通常の挙動
     */
    ManaFlowRule getManaFlowRule();

    /**
     * バイアス込みの「実効充填率」を計算するヘルパー
     */
    default double getEffectiveFillRatio() {
        return fillRatio() + getManaFlowRule().flowBias();
    }

    /**
     * バイアス込みの「実効マナ量」
     * ネットワーク計算用。Entanglerなら満タンでも 0 になる。
     */
    default long getEffectiveMana() {
        // 実効マナ = 現在量 + (最大容量 * バイアス)
        long effective = getMana() + (long)(getMaxMana() * getManaFlowRule().flowBias());

        // 負の値になると総量計算がおかしくなるため、0以上にする
        // (逆に上限突破は許容する)
        return Math.max(0, effective);
    }
}
