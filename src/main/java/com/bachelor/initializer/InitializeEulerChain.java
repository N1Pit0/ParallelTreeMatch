package com.bachelor.initializer;

import static com.bachelor.EulerChain.CHAIN;
import static com.bachelor.InputArray.T;

public class InitializeEulerChain implements Runnable{
    private final int index;

    public InitializeEulerChain(int index){
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        if(iFather == -1) return;

        int edgeLabel = T[index].getEdge_label();
        //CHAIN needs some kind of synchronization
        CHAIN[T[iFather].tour[edgeLabel + 1].getCost()] = T[iFather].tour[edgeLabel + 1];
        CHAIN[T[index].tour[0].getCost()] = T[index].tour[0];
    }
}
