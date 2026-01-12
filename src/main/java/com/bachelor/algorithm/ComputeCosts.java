package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import java.util.concurrent.ExecutorService;

public class ComputeCosts {
    private final EulerChain eulerChain;
    private final ExecutorService executor;
    private final TreeNode[] T;

    public ComputeCosts(EulerChain eulerChain, ExecutorService executor) {
        this.eulerChain = eulerChain;
        this.executor = executor;
        this.T = eulerChain.getT();
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

    }

}
