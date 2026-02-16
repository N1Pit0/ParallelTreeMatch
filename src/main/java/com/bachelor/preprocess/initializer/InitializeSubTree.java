package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

import static com.bachelor.preprocess.NodeType.LEAF;

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

        if (iFather >= 0) {
            int edgeLabel = T[index].getEdge_label();
            int subTree = T[iFather].tour[T[iFather].arity()].getCost();
            T[iFather].tour[edgeLabel].setSubtree(subTree);
            T[iFather].tour[T[iFather].arity()].setSubtree(subTree);
        }

        SubNode firstSubNode = T[index].tour[0];
        if(firstSubNode.getType().equals(LEAF)){
            firstSubNode.setSubtree(firstSubNode.getCost());
        }
    }
}
