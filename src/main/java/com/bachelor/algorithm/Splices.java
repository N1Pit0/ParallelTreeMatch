package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

public class Splices {
    private int[][] splices;

    public Splices(EulerChain eulerChain){
        int size = eulerChain.getChainSize();
        splices = new int[size][2];
    }

    public int[][] getSplices() {
        return splices;
    }
}
