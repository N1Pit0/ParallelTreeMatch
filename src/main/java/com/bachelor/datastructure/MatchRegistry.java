package com.bachelor.datastructure;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MatchRegistry {
    public int matchStart;
    private final ConcurrentLinkedDeque<MatchInterval> matchIntervals;

    public MatchRegistry() {
        this.matchIntervals = new ConcurrentLinkedDeque<>();
    }

    public ConcurrentLinkedDeque<MatchInterval> getMatchIntervals() {
        return matchIntervals;
    }

    public MatchInterval getMatchIntervalWithIndex(int index) throws NoSuchElementException {
        return this.matchIntervals.stream().skip(index).findFirst().get();
    }

    public void addMatchIntervalToCurrentNode(MatchInterval matchInterval) {
        this.matchIntervals.add(matchInterval);
    }

    public void addAllToPermutations(Collection<MatchInterval> matchIntervals){
        this.matchIntervals.addAll(matchIntervals);
    }

    @Override
    public String toString() {
        return "MatchRegistry{" +
                "matchStart="+ matchStart +
                " with matchIntervals=" + matchIntervals +
                '}';
    }

    public boolean isEmpty() {
        return this.matchIntervals.isEmpty();
    }

    public void concat(MatchRegistry other){
        this.matchIntervals.addAll(other.getMatchIntervals());
//        this.variableReplacements.addAll(other.getVariableReplacements());
    }
}
