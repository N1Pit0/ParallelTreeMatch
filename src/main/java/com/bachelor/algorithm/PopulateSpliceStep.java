package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;


@SuppressWarnings("ClassCanBeRecord")
class PopulateSpliceStep {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final Splice splice;

    PopulateSpliceStep(EulerChain eulerChain, Splice splice, ExecutorService executor) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.splice = splice;
    }

    void createSplices() {
        reinitializeCostToMakeSpices();
        ParallelPrefixSumStep.parallelPrefixSum(eulerChain.getChain());
        constructSplices();
    }

    private void reinitializeCostToMakeSpices() {
        List<Runnable> tasks = IntStream.range(0, eulerChain.getChainSize())
            .parallel()
            .mapToObj(index -> (Runnable) new CostReinitializeForSplicesStep(eulerChain, index))
            .toList();

        ExecutorBarrierUtils.invokeAll(executor, tasks);
    }

    private void constructSplices() {
        int[][] splices = this.splice.getSplices();
        Object[] locks = new Object[splices.length];
        Arrays.fill(locks, new Object());

        List<Runnable> tasks = IntStream.range(0, eulerChain.getChainSize())
                .parallel()
                .mapToObj(index ->(Runnable) new ConstructSplicesStep(eulerChain, splices, locks, index))
                .toList();

        ExecutorBarrierUtils.invokeAll(executor, tasks);

        splices[0][0] = 0;
        // Ensure the last splice end is set properly
        if (splices.length > 1 && splices[splices.length - 1][1] == 0) {
            splices[splices.length - 1][1] = eulerChain.getChainSize() - 1;
        }
    }
}
