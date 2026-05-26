package com.bachelor.preprocess;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TreeNode {
    //  an array containing n + 1 elements, where n is the outdegree of the
    //  current node in the tree. Needs initialization
    public final SubNode[] tour;
    private final CountDownLatch[] latches;
    //contains either a function symbol or a variable associated with the
    //node
    private final String label;
    private final Variable variable;
    //contains a pointer to the parent of the node.
    private final int father;
    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    private final int edge_label;
    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    private final int outDegree;

    private TreeNode(Builder builder) {
        this.outDegree = builder.outDegree;
        this.variable = builder.variable;
        this.label = builder.label;
        this.tour = builder.tour;
        this.father = builder.father;
        this.edge_label = builder.edgeLabel;
        this.latches = new CountDownLatch[Step.values().length];
        for (Step step : Step.values()) {
            this.latches[step.ordinal()] = new CountDownLatch(this.outDegree);
        }
    }

    public int getEdgeLabel() {
        return this.edge_label;
    }

    public int arity() {
        return outDegree;
    }

    public int getFather() {
        return this.father;
    }

    public String getLabel() {
        return this.label;
    }

    public CountDownLatch[] getLatches() {
        return this.latches;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "label='" + label + '\'' +
                ", variable=" + variable +
                ", father=" + father +
                ", edge_label=" + edge_label +
                ", outDegree=" + outDegree +
                ", tour=" + Arrays.toString(tour) +
                '}';
    }

    public static class Builder {
        private final String label;
        private int father;
        private Variable variable;
        private int outDegree;
        private int edgeLabel;
        private SubNode[] tour;

        public Builder(String label) {
            this.label = label;
        }

        public Builder father(int father) {
            this.father = father;
            return this;
        }

        public Builder variable(Variable answer) {
            this.variable = answer;
            return this;
        }

        public Builder outDegree(int outDegree) {
            this.outDegree = outDegree;
            return this;
        }

        public Builder edgeLabel(int edgeLabel) {
            this.edgeLabel = edgeLabel;
            return this;
        }

        public Builder tour(SubNode[] tour) {
            this.tour = tour;
            return this;
        }

        public TreeNode build() {
            return new TreeNode(this);
        }
    }
}
