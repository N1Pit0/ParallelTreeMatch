package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.*;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class InitializeCost {
    private final ExecutorService executor;
    private final EulerChain eulerChain;
    private final SubNode head;
    private final int timeoutInSeconds;

    public static void createAndRunNewInstance(ExecutorService executor,
                                                         EulerChain eulerChain,
                                                         int timeoutInSeconds){
        new InitializeCost(executor, eulerChain, timeoutInSeconds).initializeCosts();
    }

    private InitializeCost(ExecutorService executor, EulerChain eulerChain, int timeoutInSeconds) {
        this.executor = executor;
        this.eulerChain = eulerChain;
        this.head = eulerChain.getT()[0].tour[0];
        this.timeoutInSeconds = timeoutInSeconds;
    }

    private void initializeCosts() {

        final int size = eulerChain.getChainSize();
        int rounds = (int) Math.ceil(Math.log(size) / Math.log(2));

        for (int r = 0; r < rounds; r++) {

            List<Runnable> computeTemporaryCosts = new LinkedList<>();
            for (SubNode current : head) {
                computeTemporaryCosts.add(new ComputeTemporaryCostsStep(current));
            }
            ExecutorBarrierUtils.invokeAll(executor, computeTemporaryCosts, timeoutInSeconds);

            List<Runnable> commitFutures = new LinkedList<>();
            for (SubNode current : head) {
                commitFutures.add(new CommitTemporaryCostsStep(current));
            }
            ExecutorBarrierUtils.invokeAll(executor, commitFutures, 5);
        }
    }
}
