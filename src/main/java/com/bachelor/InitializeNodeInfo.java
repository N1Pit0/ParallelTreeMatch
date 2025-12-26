package com.bachelor;

import static com.bachelor.InputArray.T;

public class InitializeNodeInfo implements Runnable{
    private final int index;

    public InitializeNodeInfo(int index){
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        if (iFather == -1) return;

        int edgeLabel = T[index].getEdge_label();
        T[iFather].tour[edgeLabel].setNodeInfo(iFather);
    }
}
