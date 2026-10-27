package com.bachelor.preprocess;

public class EulerChain {

    private final SubNode[] CHAIN;
    private final InputArray inputArray;
    private final TreeNode[] T;

    public EulerChain(InputArray inputArray) {
        this.inputArray = inputArray;
        this.T = inputArray.getT();
        int size = 0;
        for (var elem : T) {
            size += elem.arity() + 1;
        }
        this.CHAIN = new SubNode[size];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"");
        for (SubNode node : CHAIN) {
            stringBuilder.append(T[node.getNodeInfo()].getLabel());
        }
        return stringBuilder.append("\"").toString();
    }

    public void setAtIndex(int index, SubNode subNode) {
        CHAIN[index] = subNode;
    }

    public SubNode getFromIndex(int index) {
        return CHAIN[index];
    }

    public int getChainSize() {
        return CHAIN.length;
    }

    public SubNode[] getChain() {
        return CHAIN;
    }

    public TreeNode[] getT() {
        return T;
    }

    public InputArray getInputArray() {
        return this.inputArray;
    }
}
