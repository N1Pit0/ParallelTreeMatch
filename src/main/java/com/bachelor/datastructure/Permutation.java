package com.bachelor.datastructure;

import com.bachelor.preprocess.Variable;

public record Permutation(int matchStart, int matchEnd, Variable variable, String replacement) {

    @Override
    public String toString() {
        return "Permutation{" +
                "matchStart=" + matchStart +
                ", matchEnd=" + matchEnd +
                ", variable=" + variable +
                ", replacement='" + replacement + '\'' +
                '}';
    }
}
