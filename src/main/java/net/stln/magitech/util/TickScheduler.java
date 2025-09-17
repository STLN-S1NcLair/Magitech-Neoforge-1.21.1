package net.stln.magitech.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 遅延実行マネージャー（サーバー用）
public class TickScheduler {
    private static final List<DelayedTask> scheduledTasksServer = new ArrayList<>();
    private static final List<DelayedTask> scheduledTasksClient = new ArrayList<>();

    public static void schedule(int delayTicks, Runnable task, @Nullable Boolean isClient) {
        if (isClient == null) {
            scheduledTasksServer.add(new DelayedTask(delayTicks, task));
            scheduledTasksClient.add(new DelayedTask(delayTicks, task));
        } else if (isClient) {
            scheduledTasksClient.add(new DelayedTask(delayTicks, task));
        } else {
            scheduledTasksServer.add(new DelayedTask(delayTicks, task));
        }
    }

    public static void tick(boolean isClient) {
        tick(!isClient ? scheduledTasksServer : scheduledTasksClient);
    }
    
    private static void tick(List<DelayedTask> tasks) {
        Iterator<DelayedTask> iterator = tasks.iterator();
        List<DelayedTask> nextTask = new ArrayList<>();

        while (iterator.hasNext()) {
            DelayedTask task = iterator.next();
            int newDelay = task.tick();
            if (newDelay < 0) {
                task.getTask().run();
            } else {
                nextTask.add(new DelayedTask(newDelay, task.getTask()));
            }
            iterator.remove();
        }

        tasks.addAll(nextTask);
    }
}
