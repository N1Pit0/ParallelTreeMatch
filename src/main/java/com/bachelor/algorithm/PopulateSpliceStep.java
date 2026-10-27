package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.utils.ExecutorBarrierUtils;
import java.util.concurrent.ExecutorService;

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
        ExecutorBarrierUtils.forEachIndex(executor, eulerChain.getChainSize(),
                index -> new CostReinitializeForSplicesStep(eulerChain, index).run());
    }

    private void constructSplices() {
        int[][] splices = this.splice.getSplices();

        ExecutorBarrierUtils.forEachIndex(executor, eulerChain.getChainSize(),
                index -> new ConstructSplicesStep(eulerChain, splices, index).run());
    }
}
