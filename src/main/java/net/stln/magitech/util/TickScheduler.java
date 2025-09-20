package net.stln.magitech.util;

import net.minecraft.server.TickTask;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 遅延実行マネージャー（サーバー用）
public class TickScheduler {
    private static final List<TickTask> scheduledTasksServer = new ArrayList<>();
    private static final List<TickTask> scheduledTasksClient = new ArrayList<>();

    public static void schedule(int delayTicks, Runnable task, @Nullable Boolean isClient) {
        if (isClient == null) {
            scheduledTasksServer.add(new TickTask(delayTicks, task));
            scheduledTasksClient.add(new TickTask(delayTicks, task));
        } else if (isClient) {
            scheduledTasksClient.add(new TickTask(delayTicks, task));
        } else {
            scheduledTasksServer.add(new TickTask(delayTicks, task));
        }
    }

    public static void tick(boolean isClient) {
        tick(!isClient ? scheduledTasksServer : scheduledTasksClient);
    }
    
    private static void tick(List<TickTask> tasks) {
        Iterator<TickTask> iterator = tasks.iterator();
        List<TickTask> nextTask = new ArrayList<>();

        while (iterator.hasNext()) {
            TickTask task = iterator.next();
            int newDelay = task.getTick() - 1;
            if (newDelay <= 0) {
                task.run();
            } else {
                nextTask.add(new TickTask(newDelay, task));
            }
            iterator.remove();
        }

        tasks.addAll(nextTask);
    }
}
