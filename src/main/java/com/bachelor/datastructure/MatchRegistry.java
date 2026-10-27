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

    public void addAllToMatchIntervals(Collection<MatchInterval> matchIntervals){
        this.matchIntervals.addAll(matchIntervals);
    }

    @Override
    public String toString() {
        return " matchIntervals=" + matchIntervals;
    }

    public boolean isEmpty() {
        return this.matchIntervals.isEmpty();
    }

    public static MatchRegistry concat(MatchRegistry first,MatchRegistry second){
        MatchRegistry result = new MatchRegistry();
        result.addAllToMatchIntervals(first.getMatchIntervals());
        result.addAllToMatchIntervals(second.getMatchIntervals());
        return result;
    }
}
