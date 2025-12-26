package com.bachelor;

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
        EulerChain.setAtIndex(T[iFather].tour[edgeLabel + 1].getCost(), T[iFather].tour[edgeLabel + 1]);
        EulerChain.setAtIndex(T[index].tour[0].getCost(), T[index].tour[0]);
    }
}
