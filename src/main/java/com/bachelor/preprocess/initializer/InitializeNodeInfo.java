package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.TreeNode;

@SuppressWarnings("ClassCanBeRecord")
public class InitializeNodeInfo implements Runnable {
    private final int index;
    private final TreeNode[] T;

    public InitializeNodeInfo(int index, InputArray inputArray) {
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        if (iFather >= 0) {
            int edgeLabel = T[index].getEdge_label();
            T[iFather].tour[edgeLabel].setNodeInfo(iFather);
            T[iFather].tour[T[iFather].arity()].setNodeInfo(iFather);
        }
        T[index].tour[0].setNodeInfo(index);
    }
}
