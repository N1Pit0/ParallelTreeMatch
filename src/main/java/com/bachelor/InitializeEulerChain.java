package com.bachelor;

public class InitializeEulerChain implements Runnable{
    private final int index;
    private final EulerChain eulerChain;
    private final TreeNode[] T;

    public InitializeEulerChain(int index, EulerChain eulerChain, InputArray inputArray){
        this.index = index;
        this.eulerChain = eulerChain;
        this.T = inputArray.getT();
    }

    @Override
    public void run() {
        int iFather = T[index].getFather();
        if(iFather <= 0) return;

        int edgeLabel = T[index].getEdge_label();
        eulerChain.setAtIndex(T[iFather].tour[edgeLabel + 1].getCost(), T[iFather].tour[edgeLabel + 1]);
        eulerChain.setAtIndex(T[index].tour[0].getCost(), T[index].tour[0]);
    }
}
