package net.stln.magitech.feature.tool.property;

public interface CalculableToolProperty<T> extends IToolProperty<T> {

    // 加算
    T add(T a, T b);

    // 乗算
    T mul(T a, T b);

    // スカラー加算
    T scalarAdd(T a, float b);

    // スカラー乗算
    T scalarMul(T a, float b);

    float scalarValue(T a);

    // 単位元
    T identity();
}