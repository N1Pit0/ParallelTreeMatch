package com.bachelor;

import static com.bachelor.InputArray.T;
import static com.bachelor.NodeType.*;

public class InitializeLeaf implements Runnable {
    private final int index;

    public InitializeLeaf(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();

        if (iFather <= 0) return;

        int edgeLabel = T[index].getEdge_label();
        T[iFather].tour[edgeLabel].setType(DUMMY);

        int currentOutDegree = T[index].arity();
        if (currentOutDegree == 0) {
            T[index].tour[0].setType(LEAF);
        } else {
            T[index].tour[0].setType(FIRST);
            T[index].tour[currentOutDegree + 1].setType(LAST);
        }
    }
}
