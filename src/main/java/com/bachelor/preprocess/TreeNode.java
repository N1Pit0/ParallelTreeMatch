package com.bachelor.preprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TreeNode {
    private final CountDownLatch[] latches;

    private static final Logger logger = LoggerFactory.getLogger(TreeNode.class);

    //contains either a function symbol or a variable associated with the
    //node
    private final String label;

    private final boolean isVariable;

    //contains a pointer to the parent of the node.
    private final int father;

    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    private final int edge_label;

    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    private final int outDegree;

    //  an array containing n + 1 elements, where n is the outdegree of the
    //  current node in the tree. Needs initialization
    public final SubNode[] tour;

    // latches[0] for InitializeNodeInfo
    // latches[1] for InitializeTourInfo
    // latches[2] for InitializeType
    // latches[3] for InitializeSubtree
    // latches[5] for InitializeEulerChain
    private TreeNode(Builder builder) {
        this.outDegree = builder.outDegree;
        this.isVariable = builder.isVariable;
        this.label = builder.label;
        this.tour = builder.tour;
        this.father = builder.father;
        this.edge_label = builder.edgeLabel;
        this.latches = new CountDownLatch[5]; // 5 Here is number of steps/initializers required for algorithm
        for (int i = 0; i < this.outDegree; i++) {
            this.latches[i] = new CountDownLatch(this.outDegree);
        }
    }

    public static class Builder {
        private final String label;
        private int father;
        private boolean isVariable;
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

        public Builder isVariable(boolean answer) {
            this.isVariable = answer;
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

        public TreeNode build(){
            return new TreeNode(this);
        }
    }

    public int getEdge_label() {
        return this.edge_label;
    }

    public int arity(){
        return outDegree;
    }

    public int getFather() {
        logger.debug("Inside the TreeNode for father {}", this.father);
        return this.father;
    }

    public CountDownLatch[] getLatches(){
        return this.latches;
    }

    public boolean isVariable() {
        return isVariable;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "label='" + label + '\'' +
                ", isVariable=" + isVariable +
                ", father=" + father +
                ", edge_label=" + edge_label +
                ", outDegree=" + outDegree +
                ", tour=" + Arrays.toString(tour) +
                '}';
    }
}
