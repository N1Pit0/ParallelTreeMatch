package com.bachelor.datastructure;

import com.bachelor.preprocess.Variable;

public class VariableReplacement {
    public Variable variable;
    public String variableReplacement;

    public VariableReplacement(Variable variable, String variableReplacement){
        this.variable = variable;
        this.variableReplacement = variableReplacement;
    }

    @Override
    public String toString() {
        return "VariableReplacement{" +
                "variable=" + variable.getName() +
                ", variableReplacement=" + variableReplacement +
                '}';
    }
}
