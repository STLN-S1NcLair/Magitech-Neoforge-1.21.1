package net.stln.magitech.effect.visual;

// 0~1の範囲を全体とした区間
public record Section(float start, float end) {

    public static Section cover() {
        return new Section(0, 1);
    }

    public static Section firstHalf() {
        return new Section(0, 0.5F);
    }

    public static Section lastHalf() {
        return new Section(0.5F, 1);
    }

    // その区間の占有率
    public float ratio() {
        return end - start;
    }
}
