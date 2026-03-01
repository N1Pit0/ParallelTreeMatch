package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


//This class does more than one thing.
// It is subject to decomposition according
// to Single Responsibility principle
public class ComputeCosts {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final TreeNode[] T;
    private final Splice splice;
    private final Phaser phaser;

    public ComputeCosts(EulerChain eulerChain, Splice splice, ExecutorService executor, Phaser phaser) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
        this.splice = splice;
        this.phaser = phaser;
    }

    public void createSplices() throws InterruptedException, TimeoutException {
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

        //TODO: Parallelize setting splice
        for (int i = 0; i < eulerChain.getChainSize(); i++) {

            SubNode currentSubNode = eulerChain.getFromIndex(i);
            if (T[currentSubNode.getNodeInfo()].isVariable()) {

                if (checkRangeExclusive(eulerChain.getFromIndex(i).getCost()-1, 0, splices.length)) {
                        splices[eulerChain.getFromIndex(i).getCost()-1][1] = i - 1;
                }
                if (checkRangeExclusive(eulerChain.getFromIndex(i).getCost(), 0, splices.length)) {
                        splices[eulerChain.getFromIndex(i).getCost()][0] = i + 1;
                }
            }
        }
        splices[0][0] = 0;
        // Ensure the last splice end is set properly
        if (splices.length > 1 && splices[splices.length-1][1] == 0) {
            splices[splices.length-1][1] = eulerChain.getChainSize() - 1;
        }
    }

    private boolean checkRangeExclusive(int index, int from, int to) {
        return from <= index && index < to;
    }
}
