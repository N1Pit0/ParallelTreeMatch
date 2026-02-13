package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


//God knows what's going on here. Just want to
//see the results, and then I'll decompose and make this class 'prettier'
public class InitializeCost {
    private final ExecutorService executor;
    private final EulerChain eulerChain;
    private final SubNode head;

    public InitializeCost( ExecutorService executor, EulerChain eulerChain){
        this.executor = executor;
        this.eulerChain = eulerChain;
        this.head = eulerChain.getT()[0].tour[0];
    }

    private void initialize() {
        int expectedCount = eulerChain.getChainSize();
        System.out.println("Expected chain size: " + expectedCount);

        // Count actual nodes
        int actualCount = 0;
        for(var current : head) {
            actualCount++;
        }
        System.out.println("Actual nodes in iteration: " + actualCount);

//        if (expectedCount != actualCount) {
//            throw new IllegalStateException(
//                    String.format("Mismatch! Expected %d nodes but found %d", expectedCount, actualCount)
//            );
//        }

        // Now do the actual initialization
        CountDownLatch latch = new CountDownLatch(actualCount);  // Use actual count!

        for(var current : head) {
            executor.submit(() -> {
                try {
                    if (current.getTourInfo() != null) {
                        current.setCost(1);
                    } else {
                        current.setCost(0);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            if (!latch.await(2, TimeUnit.SECONDS)) {
                System.err.println("Timeout! Remaining count: " + latch.getCount());
                System.err.println("This means " + latch.getCount() + " tasks didn't complete");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting!");
            Thread.currentThread().interrupt();  // Restore interrupt flag
            throw new RuntimeException("Initialize interrupted", e);
        }
    }

    public void doWork() {
        initialize(); // Initial cost setup

        final int eulerChainSize = eulerChain.getChainSize();
        int rounds = (int) Math.ceil(Math.log(eulerChainSize) / Math.log(2));


        for (int i = 0; i < rounds; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(eulerChainSize);
            for (SubNode current : head) {
                executor.submit(() -> {
                    SubNode next = current.getNextWithoutLock();
                    SubNode first = current;
                    SubNode second = next;
                    if (next != null && System.identityHashCode(current) > System.identityHashCode(next)) {
                        first = next;
                        second = current;
                    }
                    first.lock();
                    try {
                        if (second != null) second.lock();
                        try {
                            if (next != null) {
                                current.setCost(current.getCostWithoutLock() + next.getCostWithoutLock());
                                current.setNextWithoutLock(next.getNextWithoutLock());
                            }
                        } finally {
                            if (second != null) second.unlock();
                        }
                    } finally {
                        first.unlock();
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
