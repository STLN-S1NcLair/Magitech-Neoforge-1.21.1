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
        if (!isClient) {
            Iterator<DelayedTask> iteratorServer = scheduledTasksServer.iterator();
            List<DelayedTask> nextServer = new ArrayList<>();

            while (iteratorServer.hasNext()) {
                DelayedTask task = iteratorServer.next();
                int newDelay = task.tick();
                if (newDelay < 0) {
                    task.getTask().run();
                    iteratorServer.remove();
                } else {
                    nextServer.add(new DelayedTask(newDelay, task.getTask()));
                    iteratorServer.remove();
                }
            }

            scheduledTasksServer.addAll(nextServer);
        } else {
            Iterator<DelayedTask> iteratorClient = scheduledTasksClient.iterator();
            List<DelayedTask> nextClient = new ArrayList<>();

            while (iteratorClient.hasNext()) {
                DelayedTask task = iteratorClient.next();
                int newDelay = task.tick();
                if (newDelay < 0) {
                    task.getTask().run();
                    iteratorClient.remove();
                } else {
                    nextClient.add(new DelayedTask(newDelay, task.getTask()));
                    iteratorClient.remove();
                }
            }

            scheduledTasksClient.addAll(nextClient);
        }
    }
}
