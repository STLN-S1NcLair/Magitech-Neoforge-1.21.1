package net.stln.magitech.util;

// 遅延実行タスクのクラス
public class DelayedTask {
    private final int remainingTicks;
    private final Runnable task;

    public DelayedTask(int ticks, Runnable task) {
        this.remainingTicks = ticks;
        this.task = task;
    }

    public int tick() {
        return remainingTicks - 1;
    }

    public Runnable getTask() {
        return task;
    }
}
