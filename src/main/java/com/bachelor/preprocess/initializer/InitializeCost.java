package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


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

    public void doWork() {

        final int size = eulerChain.getChainSize();
        int rounds = (int) Math.ceil(Math.log(size) / Math.log(2));

        for (int r = 0; r < rounds; r++) {

            List<Future<?>> futures = new ArrayList<>();

            // Phase 1: compute new values (read only old next + cost)
            for (SubNode current : head) {
                futures.add(executor.submit(() -> {

                    SubNode next = current.getNext();

                    if (next != null) {
                        current.setCostTmp(
                                current.getCost() + next.getCost()
                        );

                        current.setNextTmp(
                                next.getNext()
                        );
                    } else {
                        current.setCostTmp(current.getCost());
                        current.setNextTmp(null);
                    }
                }));
            }

            // Barrier
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            List<Future<?>> commitFutures = new ArrayList<>();

            for (SubNode current : head) {
                commitFutures.add(executor.submit(() -> {
                    current.setCost(current.getCostTmp());
                    current.setNext(current.getNextTmp());
                }));
            }

            for (Future<?> f : commitFutures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
