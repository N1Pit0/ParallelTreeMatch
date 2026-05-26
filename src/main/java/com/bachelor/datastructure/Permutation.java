package com.bachelor.datastructure;

import com.bachelor.preprocess.Variable;

public class Permutation {
    public int matchStart;
    public int matchEnd;
    public Variable variable;
    public String variableReplacement;

    private Permutation(Builder builder){
        this.matchStart = builder.matchStart;
        this.matchEnd = builder.matchEnd;
        this.variable = builder.variable;
        this.variableReplacement = builder.variableReplacement;
    }

    public static class Builder{
        private int matchStart;
        private int matchEnd;
        private Variable variable;
        private String variableReplacement;

        public Builder matchStart(int matchStart){
            this.matchStart = matchStart;
            return this;
        }

        public Builder matchEnd(int matchEnd){
            this.matchEnd = matchEnd;
            return this;
        }

        public Builder variable(Variable variable){
            this.variable = variable;
            return this;
        }

        public Builder variableReplacement(String variableReplacement){
            this.variableReplacement = variableReplacement;
            return this;
        }

        public Permutation build(){
            return new Permutation(this);
        }
    }

    @Override
    public String toString() {
        return "Permutation{" +
                "matchStart=" + matchStart +
                ", matchEnd=" + matchEnd +
                ", variable=" + variable +
                ", variableReplacement='" + variableReplacement +
                '}';
    }
}
