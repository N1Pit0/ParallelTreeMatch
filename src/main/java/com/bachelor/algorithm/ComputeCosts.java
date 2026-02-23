package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Splices splices;
    private final Phaser phaser;
    private final Logger LOGGER = LoggerFactory.getLogger(ComputeCosts.class);

    public ComputeCosts(EulerChain eulerChain, Splices splices,ExecutorService executor, Phaser phaser) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
        this.splices = splices;
        this.phaser = phaser;
    }

    public void createSplices() throws InterruptedException, TimeoutException {
        doStepOne();
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
        doStepTwo();
        doStepThree();
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
    }

    private void doStepOne(){
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

    private void doStepTwo(){
        ParallelPrefixSum.parallelPrefixSum(eulerChain.getChain());
    }


    private void doStepThree(){
        int[][] splices = this.splices.getSplices();
        phaser.bulkRegister(splices.length);

        for(int i = 1; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            executor.submit(() -> {
                SubNode currentSubNode = eulerChain.getFromIndex(index);
                if(T[currentSubNode.getNodeInfo()].isVariable()){
                    if (checkRangeExclusive(currentSubNode.getCost() - 1, 0, splices.length)) {
                        splices[currentSubNode.getCost()-1][1] = index - 1;
                    }
                    if (checkRangeExclusive(currentSubNode.getCost(), 0, splices.length)) {
                        splices[currentSubNode.getCost()][0] = index + 1;
                    }
                }
                phaser.arriveAndDeregister();
            });
        }
//        for (int i = 0; i < eulerChain.getChainSize(); i++) {
//            final int index = i;
//
//            SubNode currentSubNode = eulerChain.getFromIndex(index);
//            if (T[currentSubNode.getNodeInfo()].isVariable()) {
//                //This array indexing need to take into account that paper uses 1-based indexing
//                if (checkRangeExclusive(eulerChain.getFromIndex(index).getCost()-1, 0, splices.length)) {
//                        splices[eulerChain.getFromIndex(index).getCost()-1][1] = index - 1;
//                }
//                if (checkRangeExclusive(eulerChain.getFromIndex(index).getCost(), 0, splices.length)) {
//                        splices[eulerChain.getFromIndex(index).getCost()][0] = index + 1;
//                }
//            }
//        }
        splices[0][0] = 0;
        splices[splices.length-1][1] = eulerChain.getChainSize() - 1;// This here is not probably correct
    }

    private boolean checkRangeExclusive(int index, int from, int to) {
        return from <= index && index < to;
    }
}
