package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import java.util.concurrent.ExecutorService;

public class ComputeCosts {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final TreeNode[] T;
    private final Splices splices;

    public ComputeCosts(EulerChain eulerChain, Splices splices,ExecutorService executor) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
        this.splices = splices;
    }

    public void createSplices(){
        doStepOne();
        doStepTwo();
        doStepThree();
    }

    private void doStepOne(){
        for (int i = 0; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            executor.submit(() -> {
                SubNode current = eulerChain.getFromIndex(index);
                current.setCost(0);
                if(T[current.getNodeInfo()].isVariable()){
                    current.setCost(1);
                }
            });
        }
    }

    private void doStepTwo(){
        for (int i = 0; i < eulerChain.getChainSize(); i++) {
            int sum = 0;
            for (int j = 0; j < i; j++) {
                sum += eulerChain.getFromIndex(j).getCost();
            }
            eulerChain.getFromIndex(i).setCost(sum);
        }
    }

    private void doStepThree(){
        for(int i = 1; i < eulerChain.getChainSize(); i++) {
            final int index = i;
            int [][] spliceArray = splices.getSplices();
            executor.submit(() -> {
                synchronized (spliceArray){
                    //This array indexing need to take into account that paper uses 1-based indexing
                    spliceArray[eulerChain.getFromIndex(index).getCost()][2] = index - 1;
                    spliceArray[eulerChain.getFromIndex(index).getCost() + 1][1] = index + 1;
                }
            });
            spliceArray[0][0] = 0;
        }
    }

}
