package com.bachelor.preprocess;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class InputArray {

    private final TreeNode[] T;
    private final List<Variable> variableList;
    private final int variableCount;

    public InputArray(TreeNode[] T, List<Variable> variableList) {
        this.T = T;
        this.variableList = variableList;
        this.variableCount = variableList.size();
    }

    public TreeNode[] getT() {
        return this.T;
    }

    public int getVariableCount() {
        return this.variableCount;
    }

    public Variable getVariableWithIndex(int index){
        return variableList.get(index);
    }
}
