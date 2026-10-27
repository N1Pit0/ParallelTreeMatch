package com.bachelor.algorithm;

import com.bachelor.preprocess.*;

/**
 * For the variable at this chain position, sets the boundaries of the surrounding parts:
 *   the part before the variable ends right before it,
 *   the part after the variable starts right after it.
 * An empty part is represented as end = start - 1 (only happens for the pattern "A").
 * Each boundary is written by exactly one variable, so no synchronization is needed.
 */
class ConstructSplicesStep implements Runnable {
    private final EulerChain eulerChain;
    private final TreeNode[] T;
    private final int[][] splices;
    private final int index;

    ConstructSplicesStep(EulerChain eulerChain, int[][] splices, int index) {
        this.eulerChain = eulerChain;
        this.T = eulerChain.getT();
        this.splices = splices;
        this.index = index;
    }

    @Override
    public void run() {
        SubNode current = eulerChain.getFromIndex(index);
        if (T[current.getNodeInfo()].getVariable() == null) {
            return;
        }

        int variableNumber = current.getCost();

        splices[variableNumber - 1][1] = index - 1;   // end of the part before
        splices[variableNumber][0] = index + 1;       // start of the part after
    }
}