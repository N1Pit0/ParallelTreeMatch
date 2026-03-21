package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


//This class does more than one thing.
// It is subject to decomposition according
// to Single Responsibility principle
class PopulateSplice {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final TreeNode[] T;
    private final Splice splice;

    PopulateSplice(EulerChain eulerChain, Splice splice, ExecutorService executor) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
        this.splice = splice;
    }

    void createSplices() {
        reinitializeCostToMakeSpices();
        ParallelPrefixSum.parallelPrefixSum(eulerChain.getChain());
        constructSplices();
    }

    private void reinitializeCostToMakeSpices() {
        List<Runnable> tasks = new LinkedList<>();

        for (int i = 0; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            tasks.add(() -> {
                SubNode current = eulerChain.getFromIndex(index);
                current.setCost(0);
                if (T[current.getNodeInfo()].isVariable()) {
                    current.setCost(1);
                }
            });
        }
        ExecutorBarrierUtils.invokeAll(executor, tasks);
    }

    private void constructSplices() {
        int[][] splices = this.splice.getSplices();
        Object[] locks = new Object[splices.length];
        Arrays.fill(locks, new Object());
        List<Runnable> tasks = new LinkedList<>();

        for (int i = 0; i < eulerChain.getChainSize(); i++) {

            final int index = i;
            tasks.add(() -> {
                SubNode currentSubNode = eulerChain.getFromIndex(index);

                if (T[currentSubNode.getNodeInfo()].isVariable()) {
                    int previousCost = currentSubNode.getCost()-1, currentCost = currentSubNode.getCost();

                    if (checkRangeExclusive(previousCost,splices.length)) {
                        synchronized (locks[previousCost]) {
                            int currentEnd = splices[eulerChain.getFromIndex(index).getCost() - 1][1];
                            splices[eulerChain.getFromIndex(index).getCost() - 1][1] = Math.max(currentEnd, index - 1);
                        }
                    }
                    if (checkRangeExclusive(currentCost, splices.length)) {
                        synchronized (locks[currentCost]) {
                            int currentStart = splices[eulerChain.getFromIndex(index).getCost()][0];
                            splices[eulerChain.getFromIndex(index).getCost()][0] = Math.max(currentStart, index + 1);
                        }
                    }
                }

            });
        }

        ExecutorBarrierUtils.invokeAll(executor, tasks);

        splices[0][0] = 0;
        // Ensure the last splice end is set properly
        if (splices.length > 1 && splices[splices.length - 1][1] == 0) {
            splices[splices.length - 1][1] = eulerChain.getChainSize() - 1;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private boolean checkRangeExclusive(int index, int from, int to) {
        return from <= index && index < to;
    }

    private boolean checkRangeExclusive(int index, int to) {
        return checkRangeExclusive(index, 0, to);
    }
}
