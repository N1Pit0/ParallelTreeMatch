package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.Step;
import com.bachelor.preprocess.TreeNode;

import static com.bachelor.preprocess.NodeType.*;

@SuppressWarnings("ClassCanBeRecord")
public class InitializeType implements Initializer {
    private final int index;
    private final TreeNode[] T;

    public InitializeType(int index, InputArray inputArray) {
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {

        try {
            T[index].getLatches()[Step.INITIALIZE_TYPE.ordinal()].await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int iFather = T[index].getFather();

        if (iFather >= 0) {
            int edgeLabel = T[index].getEdge_label();
            T[iFather].tour[edgeLabel].setType(DUMMY);
        }

        int currentOutDegree = T[index].arity();
        if (currentOutDegree == 0) {
            T[index].tour[0].setType(LEAF);
        } else {
            T[index].tour[0].setType(FIRST);
            T[index].tour[currentOutDegree].setType(LAST);
        }

        if (iFather >= 0) {
            T[iFather].getLatches()[Step.INITIALIZE_TYPE.ordinal()].countDown();
        }

    }
}
