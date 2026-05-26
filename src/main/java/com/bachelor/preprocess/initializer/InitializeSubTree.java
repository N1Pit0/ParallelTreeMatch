package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.*;

import static com.bachelor.preprocess.NodeType.LEAF;

public class InitializeSubTree implements Runnable {
    private final int index;
    private final TreeNode[] T;
    private final EulerChain eulerChain;

    public InitializeSubTree(int index, EulerChain eulerChain) {
        this.index = index;
        this.T = eulerChain.getT();
        this.eulerChain = eulerChain;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();

        if (iFather >= 0) {
            int edgeLabel = T[index].getEdgeLabel();
            int subTree = eulerChain.getChainSize() - 1 - T[iFather].tour[T[iFather].arity()].getCost();
            T[iFather].tour[edgeLabel].setSubtree(subTree);
            T[iFather].tour[T[iFather].arity()].setSubtree(subTree);
        }

        SubNode firstSubNode = T[index].tour[0];
        if (firstSubNode.getType().equals(LEAF)) {
            firstSubNode.setSubtree(eulerChain.getChainSize() - 1 - firstSubNode.getCost());
        }
    }
}
