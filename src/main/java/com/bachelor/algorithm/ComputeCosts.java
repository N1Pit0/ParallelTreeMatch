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
    private final Splices splices;
    private final Phaser phaser;

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
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
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
        phaser.register();
        for (int i = 0; i < eulerChain.getChainSize(); i++) {
            int sum = 0;
            for (int j = 0; j < i; j++) {
                sum += eulerChain.getFromIndex(j).getCost();
            }
            eulerChain.getFromIndex(i).setCost(sum);
        }
        phaser.arriveAndDeregister();
    }

    private void doStepThree(){
        phaser.bulkRegister(eulerChain.getChainSize());

        //Should we start from i = 0 or i = 1???
        for(int i = 0; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            executor.submit(() -> {
                SubNode currentSubNode = eulerChain.getFromIndex(index);
                if(T[currentSubNode.getNodeInfo()].isVariable()){
                    //This array indexing need to take into account that paper uses 1-based indexing
                    splices.writeAtIndex(currentSubNode.getCost(),1, index - 1);
                    splices.writeAtIndex(currentSubNode.getCost() + 1, 0 ,index + 1);
                }
                phaser.arriveAndDeregister();
            });
        }
        splices.writeAtIndex(0,0,0);
    }

}
