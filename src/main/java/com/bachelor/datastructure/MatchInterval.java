package com.bachelor.datastructure;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MatchInterval {
    public int matchEnd;
    private final ConcurrentLinkedDeque<VariableReplacement> variableReplacements;

     private MatchInterval(Builder builder){
        this.matchEnd = builder.matchEnd;
        this.variableReplacements = new ConcurrentLinkedDeque<>();
    }

    public void addToVariableReplacements(VariableReplacement variableReplacement){
        this.variableReplacements.add(variableReplacement);
    }

    public void addAllToVariableReplacements(Collection<? extends VariableReplacement> collection){
        this.variableReplacements.addAll(collection);
    }

    public ConcurrentLinkedDeque<VariableReplacement> getVariableReplacements() {
        return variableReplacements;
    }

    public static class Builder{
        private int matchStart;
        private int matchEnd;

        public Builder matchStart(int matchStart){
            this.matchStart = matchStart;
            return this;
        }

        public Builder matchEnd(int matchEnd){
            this.matchEnd = matchEnd;
            return this;
        }

        public MatchInterval build(){
            return new MatchInterval(this);
        }
    }

    @Override
    public String toString() {
        return "MatchInterval{" +
                ", matchEnd=" + matchEnd +
                ", variableReplacements="+ variableReplacements +
                '}';
    }
}
