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

        CountDownLatch barrier = new CountDownLatch(eulerChainSize); // +1 for the main thread
        for (int i = 0; i < rounds; i++) {

            // Submit a task for each node
            for (SubNode current : head) {
                executor.submit(() -> {
                    try {
                        if (current.getNext() != null) {
                            current.setCost(current.getCost() + current.getNext().getCost());
                            current.setNext(current.getNext().getNext());
                        }
                    } finally {
                        try {
                            barrier.countDown(); // Wait for all tasks in this round
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            // Main thread waits for all tasks to reach the barrier
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
