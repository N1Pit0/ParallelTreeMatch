package com.bachelor.initializer;

import static com.bachelor.InputArray.T;

public class InitializeNodeInfo implements Runnable{
    private final int index;

    public InitializeNodeInfo(int index){
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        int edgeLabel = T[index].getEdge_label();
        if(iFather != -1){
            T[iFather].tour[edgeLabel].setNodeInfo(iFather);
        }
    }
}
