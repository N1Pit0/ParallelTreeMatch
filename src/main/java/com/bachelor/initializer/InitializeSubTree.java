package com.bachelor.initializer;

import com.bachelor.Initializer;
import com.bachelor.InputArray;
import com.bachelor.SubNode;
import com.bachelor.TreeNode;

import static com.bachelor.NodeType.LEAF;

public class InitializeSubTree implements Initializer {
    private final int index;
    private final TreeNode[] T;

    public InitializeSubTree(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
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
