package com.bachelor.preprocess;

@SuppressWarnings("ClassCanBeRecord")
public class InputArray {

    private final TreeNode[] T;
    private final int variableCount;

    public InputArray(TreeNode[] T, int variableCount) {
        this.T = T;
        this.variableCount = variableCount;
    }

    public TreeNode[] getT() {
        return this.T;
    }

    public int getVariableCount() {
        return this.variableCount;
    }
}
