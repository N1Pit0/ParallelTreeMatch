package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.TreeNode;


public class InitializeEulerChain implements Runnable {
    private final int index;
    private final EulerChain eulerChain;
    private final TreeNode[] T;

    public InitializeEulerChain(int index, EulerChain eulerChain) {
        this.index = index;
        this.eulerChain = eulerChain;
        this.T = eulerChain.getT();
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();

        int edgeLabel = T[index].getEdge_label();
        if (iFather >= 0) {
            eulerChain.setAtIndex(eulerChain.getChainSize() - 1 - T[iFather].tour[edgeLabel + 1].getCost(), T[iFather].tour[edgeLabel + 1]);
        }
        eulerChain.setAtIndex(eulerChain.getChainSize() - 1 - T[index].tour[0].getCost(), T[index].tour[0]);
    }
}
