package com.bachelor.utils;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ExecutorBarrierUtils {

    public static void invokeAll(ExecutorService executor, List<Runnable> tasks, int timeoutSeconds){
        List<Future<?>> futures  = tasks
                .stream()
                .map(executor::submit)
                .collect(Collectors.toList());

        futures.forEach(future -> {
            try {
                future.get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void invokeAll(ExecutorService executor, List<Runnable> tasks){
        invokeAll(executor, tasks, 10000);
    }
}
