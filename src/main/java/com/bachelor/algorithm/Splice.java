package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

class Splice {
    private final int[][] splices;
    private final EulerChain eulerChain;

     Splice(EulerChain eulerChain){
        this.eulerChain = eulerChain;
        int size = eulerChain.getInputArray().getVariableCount();
        splices = new int[size+1][2];
        initializeSplices();
    }

    private void initializeSplices() {
        // Initialize last splice to extend to the end of the chain
        int lastIndex = eulerChain.getChainSize() - 1;
        splices[splices.length - 1][1] = lastIndex;
    }

    int[][] getSplices() {
        return splices;
    }
}
