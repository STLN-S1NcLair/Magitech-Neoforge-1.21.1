package net.stln.magitech.helper;

import net.minecraft.server.TickTask;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// 遅延実行マネージャー（サーバー用）
public class TickScheduler {
    private static final List<TickTask> scheduledTasksServer = new ArrayList<>();
    private static final List<TickTask> scheduledTasksClient = new ArrayList<>();
    private static final List<TickTask> pendingTasksServer = new ArrayList<>();
    private static final List<TickTask> pendingTasksClient = new ArrayList<>();
    private static boolean isTickingServer = false;
    private static boolean isTickingClient = false;

    public static void schedule(int delayTicks, Runnable task, @Nullable Boolean isClient) {
        if (isClient == null) {
            addTask(scheduledTasksServer, pendingTasksServer, isTickingServer, delayTicks, task);
            addTask(scheduledTasksClient, pendingTasksClient, isTickingClient, delayTicks, task);
        } else if (isClient) {
            addTask(scheduledTasksClient, pendingTasksClient, isTickingClient, delayTicks, task);
        } else {
            addTask(scheduledTasksServer, pendingTasksServer, isTickingServer, delayTicks, task);
        }
    }

    private static void addTask(List<TickTask> tasks, List<TickTask> pendingTasks,
                                boolean isTicking, int delayTicks, Runnable taskRunnable) {
        TickTask tickTask = new TickTask(delayTicks, taskRunnable);
        if (isTicking) {
            pendingTasks.add(tickTask);
        } else {
            tasks.add(tickTask);
        }
    }

    public static void tick(boolean isClient) {
        if (isClient) {
            tick(scheduledTasksClient, pendingTasksClient, true);
        } else {
            tick(scheduledTasksServer, pendingTasksServer, false);
        }
    }

    private static void tick(List<TickTask> tasks, List<TickTask> pendingTasks, boolean isClient) {
        if (isClient) {
            isTickingClient = true;
        } else {
            isTickingServer = true;
        }

        try {
            List<TickTask> nextTask = new ArrayList<>();

            // すべてのタスクを処理
            for (int i = 0; i < tasks.size(); i++) {
                TickTask task = tasks.get(i);
                int newDelay = task.getTick() - 1;
                if (newDelay <= 0) {
                    task.run();
                    // task.run() 実行中に追加されたタスクを処理
                    nextTask.addAll(pendingTasks);
                    pendingTasks.clear();
                } else {
                    nextTask.add(new TickTask(newDelay, task));
                }
            }

            tasks.clear();
            tasks.addAll(nextTask);
        } finally {
            if (isClient) {
                isTickingClient = false;
            } else {
                isTickingServer = false;
            }
        }
    }
}
