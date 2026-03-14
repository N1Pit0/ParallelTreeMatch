package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


//This class does more than one thing.
// It is subject to decomposition according
// to Single Responsibility principle
class PopulateSplice {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final TreeNode[] T;
    private final Splice splice;
    private final Phaser phaser;

    PopulateSplice(EulerChain eulerChain, Splice splice, ExecutorService executor, Phaser phaser) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
        this.splice = splice;
        this.phaser = phaser;
    }

    void createSplices() throws InterruptedException, TimeoutException {
        reinitializeCostToMakeSpices();
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
        ParallelPrefixSum.parallelPrefixSum(eulerChain.getChain());
        constructSplices();
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
    }

    private void reinitializeCostToMakeSpices(){
        phaser.bulkRegister(eulerChain.getChainSize());
        for (int i = 0; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            executor.submit(() -> {
                SubNode current = eulerChain.getFromIndex(index);
                current.setCost(0);
                if(T[current.getNodeInfo()].isVariable()){
                    current.setCost(1);
                }
                phaser.arriveAndDeregister();
            });
        }
    }

    private void constructSplices(){
        int[][] splices = this.splice.getSplices();
        Object[] locks = new Object[splices.length];

        phaser.bulkRegister(eulerChain.getChainSize());
        for (int i = 0; i < eulerChain.getChainSize(); i++) {

            final int index = i;
            executor.submit(() -> {
                SubNode currentSubNode = eulerChain.getFromIndex(index);

                if (T[currentSubNode.getNodeInfo()].isVariable()) {
                    int previousCost = currentSubNode.getCost()-1, currentCost = currentSubNode.getCost();
                    synchronized (locks) {
                        if(locks[previousCost] == null){
                            locks[previousCost] = new Object();
                        }
                        if (locks[currentCost] == null){
                            locks[currentCost] = new Object();
                        }
                    }
                    if (checkRangeExclusive(previousCost,splices.length)) {
                        synchronized (locks[previousCost]) {
                            int currentEnd = splices[eulerChain.getFromIndex(index).getCost()-1][1];
                            splices[eulerChain.getFromIndex(index).getCost()-1][1] = Math.max(currentEnd, index-1);
                        }
                    }
                    if (checkRangeExclusive(currentCost, splices.length)) {
                        synchronized (locks[currentCost]) {
                            int currentStart = splices[eulerChain.getFromIndex(index).getCost()][0];
                            splices[eulerChain.getFromIndex(index).getCost()][0] = Math.max(currentStart, index+1);
                        }
                    }
                }
                
                phaser.arriveAndDeregister();
            });

        }
        splices[0][0] = 0;
        // Ensure the last splice end is set properly
        if (splices.length > 1 && splices[splices.length-1][1] == 0) {
            splices[splices.length-1][1] = eulerChain.getChainSize() - 1;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private boolean checkRangeExclusive(int index, int from, int to) {
        return from <= index && index < to;
    }

    private boolean checkRangeExclusive(int index,int to){
        return checkRangeExclusive(index, 0, to);
    }
}
