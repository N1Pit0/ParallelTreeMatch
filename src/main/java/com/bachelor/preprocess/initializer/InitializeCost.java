package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class InitializeCost {
    private final ExecutorService executor;
    private final EulerChain eulerChain;
    private SubNode head;
    private CountDownLatch latch;

    public InitializeCost( ExecutorService executor, EulerChain eulerChain){
        this.executor = executor;
        this.eulerChain = eulerChain;
        this.head = eulerChain.getT()[0].tour[0];
        this.latch = new CountDownLatch(eulerChain.getChainSize());
    }

    private void initialize(){
        for(var current : head){
            executor.submit(() -> {
                try{
                    if (current.getTourInfo() != null) {
                        current.setCost(1);
                    } else {
                        current.setCost(0);
                    }
                }finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void doWork() {
        initialize(); // Initial cost setup

        final int eulerChainSize = eulerChain.getChainSize();
        int rounds = (int) Math.ceil(Math.log(eulerChainSize) / Math.log(2));

        CyclicBarrier barrier = new CyclicBarrier(eulerChainSize + 1); // +1 for main thread

        for (int i = 0; i < rounds; i++) {
            for (SubNode current : head) {
                executor.submit(() -> {
                    current.getWriteLock().lock();
                    SubNode next = current.getNextWithoutLock();
                    try {
                        if (next != null) {
                            next.getWriteLock().lock();
                            try {
                                current.setCostWithoutLock(current.getCostWithoutLock() + next.getCostWithoutLock());
                                current.setNextWithoutLock(next.getNextWithoutLock());
                            } finally {
                                next.getWriteLock().unlock();
                            }
                        }
                    } finally {
                        current.getWriteLock().unlock();
                    }
                    // Wait for all tasks in this round
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            // Main thread waits for all tasks to reach the barrier
            try {
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
