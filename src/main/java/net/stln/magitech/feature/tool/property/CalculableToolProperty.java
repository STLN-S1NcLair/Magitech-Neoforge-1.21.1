package net.stln.magitech.feature.tool.property;

public interface CalculableToolProperty<T> extends IToolProperty<T> {

    // 加算
    T add(T a, T b);

    // 乗算
    T mul(T a, T b);

    // 加法単位元
    T addIdentity();

    // 乗法単位元
    T mulIdentity();
}