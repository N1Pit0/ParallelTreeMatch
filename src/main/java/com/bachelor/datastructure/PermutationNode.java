package com.bachelor.datastructure;

import java.util.*;

public class PermutationNode {
    private List<Permutation> permutations;

    public PermutationNode() {
        this.permutations = new LinkedList<>();
    }

    public List<Permutation> getPermutations() {
        return permutations;
    }

    public void addPermutationToCurrentNode(Permutation permutation) {
        this.permutations.add(permutation);
    }

    @Override
    public String toString() {
        return "PermutationNode{" +
                "permutations=" + permutations +
                '}';
    }

    public boolean isEmpty() {
        return this.permutations.isEmpty();
    }

}
