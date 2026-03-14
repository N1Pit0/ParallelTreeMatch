package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.Initializer;
import com.bachelor.preprocess.InputArray;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TreeNode;

@SuppressWarnings("ClassCanBeRecord")
public class InitializeTourInfo implements Initializer {
    private final int index;
    private final TreeNode[] T;

    public InitializeTourInfo(int index, InputArray inputArray){
        this.index = index;
        this.T = inputArray.getT();
    }

    @Override
    public void initialize() {
        int iFather = T[index].getFather();
        if (iFather >= 0) {
            int edgeLabel = T[index].getEdge_label();
            T[iFather].tour[edgeLabel].setTourInfo(T[index].tour[0]);
            T[iFather].tour[edgeLabel].setNext(T[index].tour[0]);
            SubNode next = T[iFather].tour[T[index].getEdge_label() + 1];
            T[index].tour[T[index].arity()].setTourInfo(next);
            T[index].tour[T[index].arity()].setNext(next);
        }

        if(index == 0){
            T[index].tour[T[index].arity()].setCost(0);
        }

    }
}
