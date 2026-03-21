package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;


//God knows what's going on here. Just want to
//see the results, and then I'll decompose and make this class 'prettier'
public class InitializeCost {
    private final ExecutorService executor;
    private final EulerChain eulerChain;
    private final SubNode head;

    public InitializeCost(ExecutorService executor, EulerChain eulerChain) {
        this.executor = executor;
        this.eulerChain = eulerChain;
        this.head = eulerChain.getT()[0].tour[0];
    }

    public void doWork(int timeoutInSeconds) {

        final int size = eulerChain.getChainSize();
        int rounds = (int) Math.ceil(Math.log(size) / Math.log(2));

        for (int r = 0; r < rounds; r++) {

            List<Runnable> computeTasks = new LinkedList<>();
            // Phase 1: compute new values (read only old next + cost)
            for (SubNode current : head) {
                computeTasks.add(() -> {
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
                });
            }

            ExecutorBarrierUtils.invokeAll(executor, computeTasks, timeoutInSeconds);

            List<Runnable> commitFutures = new LinkedList<>();
            for (SubNode current : head) {
                commitFutures.add(() -> {
                    current.setCost(current.getCostTmp());
                    current.setNext(current.getNextTmp());
                });
            }

            ExecutorBarrierUtils.invokeAll(executor, commitFutures, 5);
        }
    }
}
