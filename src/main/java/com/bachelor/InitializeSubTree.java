package com.bachelor;

import static com.bachelor.InputArray.T;
import static com.bachelor.NodeType.LEAF;

public class InitializeSubTree implements Runnable{
    private final int index;

    public InitializeSubTree(int index){
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();

        if(iFather == -1) return;

        int edgeLabel = T[index].getEdge_label();
        int subTree = T[iFather].tour[T[iFather].arity() + 1].getCost();
        T[iFather].tour[edgeLabel].setSubtree(subTree);

        SubNode firstSubNode = T[index].tour[0];
        if(firstSubNode.getType().equals(LEAF)){
            firstSubNode.setSubtree(firstSubNode.getCost());
        }
    }
}
