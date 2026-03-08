package net.stln.magitech.effect.visual;

// 0~1の範囲を全体とした区間
public record Section(float start, float end) {

    // その区間の占有率
    public float ratio() {
        return end - start;
    }
}
