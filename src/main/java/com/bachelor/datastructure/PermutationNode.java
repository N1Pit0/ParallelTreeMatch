package com.bachelor.datastructure;

import java.util.concurrent.ConcurrentLinkedDeque;

public class PermutationNode {
    private ConcurrentLinkedDeque<Permutation> permutations;
    private ConcurrentLinkedDeque<VariableReplacement> variableReplacements;

    public PermutationNode() {
        this.permutations = new ConcurrentLinkedDeque<>();
        this.variableReplacements = new ConcurrentLinkedDeque<>();
    }

    public ConcurrentLinkedDeque<Permutation> getPermutations() {
        return permutations;
    }

    public void addPermutationToCurrentNode(Permutation permutation) {
        this.permutations.add(permutation);
    }

    public void addToVariableReplacements(VariableReplacement variableReplacement){
        this.variableReplacements.add(variableReplacement);
    }

    @Override
    public String toString() {
        return "PermutationNode{" +
                "permutations=" + permutations +
                "variableReplacements=" + variableReplacements+
                '}';
    }

    public boolean isEmpty() {
        return this.permutations.isEmpty();
    }
}
