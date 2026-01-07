package com.bachelor.preprocess;

public class InputArray {

    //Needs initialization
    private final TreeNode[] T;
    private final int variableCount;

    public InputArray(TreeNode[] T, int variableCount){
        this.T = T;
        this.variableCount = variableCount;
    }

    public TreeNode[] getT(){
        return this.T;
    }
}
