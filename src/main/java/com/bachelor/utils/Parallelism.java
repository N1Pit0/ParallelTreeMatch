package com.bachelor.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public final class Parallelism {

    private Parallelism() {}

    /**
     * Estimates how many tasks the executor can run at the same time.
     *   newFixedThreadPool(n)  -> n
     *   newCachedThreadPool()  -> number of CPU cores (it has no fixed size)
     *   ForkJoinPool           -> its parallelism
     */
    public static int of(ExecutorService executor) {
        if (executor instanceof ThreadPoolExecutor pool && pool.getCorePoolSize() > 0) {
            return pool.getCorePoolSize();
        }
        if (executor instanceof ForkJoinPool pool) {
            return pool.getParallelism();
        }
        return Runtime.getRuntime().availableProcessors();
    }
}