package com.bachelor.utils;

import com.bachelor.config.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.IntConsumer;

public class ExecutorBarrierUtils {

    /** Blocks smaller than this are not worth splitting further. */
    // this needs benchmarking for tuning the number well
    private static final int MIN_BLOCK_SIZE = 8;

    /** More blocks than threads, so a delayed thread doesn't hold everyone up. */
    private static final int BLOCKS_PER_THREAD = 4;

    /**
     * Runs action(i) for every i in [0, count), split into a few large blocks.
     * Returns only after all indices have been processed, so it also acts as a barrier.
     * If everything fits into one block, it runs directly on the calling thread.
     */
    //I'll probably add overloaded variant to supply MIN_BLOCK_SIZE for different calls
    //because different work needs different parameter tuning
    public static void forEachIndex(ExecutorService executor, int count, IntConsumer ... actions) {
        if (count <= 0) {
            return;
        }

        int maxBlocks = Parallelism.of(executor) * BLOCKS_PER_THREAD;
        int blockCount = Math.max(1, Math.min(maxBlocks, count / MIN_BLOCK_SIZE));
        List<Runnable> tasks = getRunnables(count, actions, blockCount);

        if (tasks.size() == 1) {
            tasks.getFirst().run();
            return;
        }
        invokeAll(executor, tasks);
    }

    private static List<Runnable> getRunnables(int count, IntConsumer[] actions, int blockCount) {
        int blockSize = Math.ceilDiv(count, blockCount);

        List<Runnable> tasks = new ArrayList<>(blockCount);
        for (int from = 0; from < count; from += blockSize) {
            int start = from;
            int end = Math.min(count, from + blockSize);
            tasks.add(() -> {
                for (int i = start; i < end; i++) {
                    for (IntConsumer action : actions){
                        action.accept(i);
                    }
                }
            });
        }
        return tasks;
    }

    public static void invokeAll(ExecutorService executor, List<Runnable> tasks, int timeoutSeconds) {
        List<Future<?>> futures = tasks.stream()
                .<Future<?>>map(executor::submit)
                .toList();

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeoutSeconds);
        try {
            for (Future<?> future : futures) {
                long remaining = deadline - System.nanoTime();
                future.get(Math.max(remaining, 0), TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            cancelAll(futures);
            throw new RuntimeException("Interrupted while waiting for tasks", e);
        } catch (ExecutionException e) {
            cancelAll(futures);
            throw new RuntimeException("Task failed: " + e.getCause().getMessage(), e.getCause());
        } catch (TimeoutException e) {
            cancelAll(futures);
            throw new RuntimeException("Tasks did not finish within " + timeoutSeconds + " seconds", e);
        }
    }

    private static void cancelAll(List<Future<?>> futures) {
        futures.forEach(f -> f.cancel(true));
    }

    public static void invokeAll(ExecutorService executor, List<Runnable> tasks){
        invokeAll(executor, tasks, Config.getThreadTimeout());
    }
}
