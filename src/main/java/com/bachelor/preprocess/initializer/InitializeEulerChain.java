package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.TreeNode;

public class InitializeEulerChain implements Initializer {
    private final int index;
    private final EulerChain eulerChain;
    private final TreeNode[] T;

    public InitializeEulerChain(int index, EulerChain eulerChain, InputArray inputArray){
        this.index = index;
        this.eulerChain = eulerChain;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
        int iFather = T[index].getFather();
        if(iFather <= 0) return;

        int edgeLabel = T[index].getEdge_label();
        eulerChain.setAtIndex(T[iFather].tour[edgeLabel + 1].getCost(), T[iFather].tour[edgeLabel + 1]);
        eulerChain.setAtIndex(T[index].tour[0].getCost(), T[index].tour[0]);
    }
}
