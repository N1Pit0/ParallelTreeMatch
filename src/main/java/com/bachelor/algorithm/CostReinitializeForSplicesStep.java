package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.TermVariable;
import com.bachelor.preprocess.TreeNode;

class CostReinitializeForSplicesStep implements Runnable {
    private final EulerChain eulerChain;
    private final TreeNode[] T;
    private final int index;


    CostReinitializeForSplicesStep(EulerChain eulerChain, int index) {
        this.eulerChain = eulerChain;
        this.T = eulerChain.getT();
        this.index = index;
    }

    @Override
    public void run() {
        SubNode current = eulerChain.getFromIndex(index);
        current.setCost(0);
        if (T[current.getNodeInfo()].getVariable() != null) {
            current.setCost(1);
        }
    }
}
