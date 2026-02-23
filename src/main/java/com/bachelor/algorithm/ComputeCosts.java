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

    public void testPrefixSum() throws InterruptedException, TimeoutException {
        doStepOne();
        phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
        doStepTwo();
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
        for (var elem : eulerChain.getChain()){
            System.out.println(elem.getCost());
        }
        System.out.println("=====================");
    }

    private void doStepTwo(){
        ParallelPrefixSum.parallelPrefixSum(eulerChain.getChain());
        for (var elem : eulerChain.getChain()){
            System.out.println(elem.getCost());
        }
    }


    private void doStepThree(){
        int[][] splices = this.splices.getSplices();
        phaser.bulkRegister(splices.length);

        for(int i = 1; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            executor.submit(() -> {
                SubNode currentSubNode = eulerChain.getFromIndex(index);
                if(T[currentSubNode.getNodeInfo()].isVariable()){
                    //This array indexing need to take into account that paper uses 1-based indexing
                    try{
                        splices[eulerChain.getFromIndex(index).getCost()][1] = index - 1;
                        splices[eulerChain.getFromIndex(index).getCost() + 1][0] = index + 1;
                    } catch (ArrayIndexOutOfBoundsException e){
                        for (var elem : e.getStackTrace()){
                            LOGGER.error(elem.toString());
                        }
                    }
                }
                phaser.arriveAndDeregister();
            });
        }
        splices[0][0] = 0;
    }


}
