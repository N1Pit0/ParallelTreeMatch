package com.bachelor.initializer;

import com.bachelor.SubNode;

import static com.bachelor.InputArray.T;

public class InitializeTourInfo implements Runnable{
    private final int index;

    public InitializeTourInfo(int index){
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        if (iFather == -1) return;

        int edgeLabel = T[index].getEdge_label();
        T[iFather].tour[edgeLabel].setTourInfo(T[index].tour[0]);
        SubNode subNode = T[iFather].tour[T[index].getEdge_label() + 1]; // Change the name of the variable to something else
        T[index].tour[T[index].arity() + 1].setTourInfo(subNode);
    }
}
