package com.bachelor.datastructure;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MatchRegistry {
    private final ConcurrentLinkedDeque<MatchInterval> matchIntervals;
    private final ConcurrentLinkedDeque<VariableReplacement> variableReplacements;

    public MatchRegistry() {
        this.matchIntervals = new ConcurrentLinkedDeque<>();
        this.variableReplacements = new ConcurrentLinkedDeque<>();
    }

    public ConcurrentLinkedDeque<MatchInterval> getPermutations() {
        return matchIntervals;
    }

    public void addPermutationToCurrentNode(MatchInterval matchInterval) {
        this.matchIntervals.add(matchInterval);
    }

    public void addToVariableReplacements(VariableReplacement variableReplacement){
        this.variableReplacements.add(variableReplacement);
    }

    public void addAllToVariableReplacements(Collection<? extends VariableReplacement> collection){
        this.variableReplacements.addAll(collection);
    }

    public void addAllToPermutations(Collection<MatchInterval> matchIntervals){
        this.matchIntervals.addAll(matchIntervals);
    }

    public ConcurrentLinkedDeque<VariableReplacement> getVariableReplacements() {
        return variableReplacements;
    }

    @Override
    public String toString() {
        return "MatchRegistry{" +
                "matchIntervals=" + matchIntervals +
                "variableReplacements=" + variableReplacements+
                '}';
    }

    public boolean isEmpty() {
        return this.matchIntervals.isEmpty();
    }

    public void concat(MatchRegistry other){
        this.matchIntervals.addAll(other.getPermutations());
        this.variableReplacements.addAll(other.getVariableReplacements());
    }
}
