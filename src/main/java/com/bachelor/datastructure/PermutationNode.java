package com.bachelor.datastructure;

import java.util.*;

public class PermutationNode {
    private int matchStartIndex;
    private List<Permutation> permutations;

    public PermutationNode(int matchStartIndex) {
        this.matchStartIndex = matchStartIndex;
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
                ", matchStartIndex=" + matchStartIndex +
                '}';
    }

    public boolean isEmpty() {
        return this.permutations.isEmpty();
    }

    public int getMatchStartIndex() {
        return matchStartIndex;
    }
}
