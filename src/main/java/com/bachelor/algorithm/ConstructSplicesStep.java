package com.bachelor.algorithm;

import com.bachelor.preprocess.*;

class ConstructSplicesStep implements Runnable{
    private final EulerChain eulerChain;
    private final TreeNode[] T;
    private final int[][] splices;
    private final Object[] locks;
    private final int index;

    ConstructSplicesStep(EulerChain eulerChain, int[][] splices, Object[] locks, int index) {
        this.eulerChain = eulerChain;
        this.T = eulerChain.getT();
        this.splices = splices;
        this.locks = locks;
        this.index = index;
    }

    @Override
    public void run() {
        SubNode currentSubNode = eulerChain.getFromIndex(index);

        if (T[currentSubNode.getNodeInfo()].getVariable() != null) {
            int previousCost = currentSubNode.getCost() - 1, currentCost = currentSubNode.getCost();

            if (checkRangeExclusive(previousCost,splices.length)) {
                synchronized (locks[previousCost]) {
                    int currentEnd = splices[eulerChain.getFromIndex(index).getCost() - 1][1];
                    splices[eulerChain.getFromIndex(index).getCost() - 1][1] = Math.max(currentEnd, index - 1);
                }
            }
            if (checkRangeExclusive(currentCost, splices.length)) {
                synchronized (locks[currentCost]) {
                    int currentStart = splices[eulerChain.getFromIndex(index).getCost()][0];
                    splices[eulerChain.getFromIndex(index).getCost()][0] = Math.max(currentStart, index + 1);
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean checkRangeExclusive(int index, int from, int to) {
        return from <= index && index < to;
    }

    private static boolean checkRangeExclusive(int index, int to) {
        return checkRangeExclusive(index, 0, to);
    }
}
