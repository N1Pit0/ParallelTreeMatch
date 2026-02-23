package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

public class Splices {
    private final int[][] splices;

    public Splices(EulerChain eulerChain){
        int size = eulerChain.getInputArray().getVariableCount();
        splices = new int[size+1][2];
    }

    public int[][] getSplices() {
        return splices;
    }
}
