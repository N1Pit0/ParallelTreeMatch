package com.bachelor;

import static com.bachelor.NodeType.LEAF;

public class InitializeSubTree implements Runnable{
    private final int index;
    private final TreeNode[] T;

    public InitializeSubTree(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();

        if(iFather <= 0) return;

        int edgeLabel = T[index].getEdge_label();
        int subTree = T[iFather].tour[T[iFather].arity()].getCost();
        T[iFather].tour[edgeLabel].setSubtree(subTree);

        SubNode firstSubNode = T[index].tour[0];
        if(firstSubNode.getType().equals(LEAF)){
            firstSubNode.setSubtree(firstSubNode.getCost());
        }
    }
}
