package com.bachelor.preprocess;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubNode implements Iterable<SubNode>{


    // -> T[i] where i is the index of the node in the Euler chain.
    private int nodeInfo; // reference to the corresponding TreeNode inside the T array.

    private SubNode tourInfo; // a reference to the next element (used to form the linked list) in the Euler chain.

    private SubNode next;

    private int subtree; // used for storing the last occurrence of node i in the Euler chain.

    //specifies whether the j_th occurrence of i is the first or last occurrence of i in the Euler chain.
    private NodeType type; // Used to indicate whether "i" is a leaf node. In case "i" is not a leaf then this field

    //    cost is used in the prefix computation to compute the rank (position)
//    of each entry in the tour field. At the end, each entry of T [i].tour will
//    be placed in an array E at a position given by the respective cost
//    field
    private int cost = 1;

    private SubNode nextTmp;
    private int costTmp;

    public SubNode getNextTmp() { return nextTmp; }
    public void setNextTmp(SubNode n) { this.nextTmp = n; }

    public int getCostTmp() { return costTmp; }
    public void setCostTmp(int c) { this.costTmp = c; }


    public SubNode(){
    }

    public int getNodeInfo() {
            return nodeInfo;

    }

    public void setNodeInfo(int nodeInfo) {
            this.nodeInfo = nodeInfo;

    }

    public SubNode getTourInfo() {
            return tourInfo;

    }

    public void setTourInfo(SubNode tourInfo) {
            this.tourInfo = tourInfo;

    }

    public int getSubtree() {

            return subtree;

    }

    public void setSubtree(int subtree) {

            this.subtree = subtree;

    }

    public NodeType getType() {

            return type;

    }

    public void setType(NodeType type) {

            this.type = type;
    }

    public int getCost() {

            return cost;

    }

    public void setCost(int cost) {

            this.cost = cost;

    }


    @Override
    public String toString() {
        // copy fields under read lock to avoid holding the lock while building the string
        int nodeInfoCopy;
        SubNode tourInfoCopy;
        int subtreeCopy;
        NodeType typeCopy;
        int costCopy;

            nodeInfoCopy = this.nodeInfo;
            tourInfoCopy = this.tourInfo;
            subtreeCopy = this.subtree;
            typeCopy = this.type;
            costCopy = this.cost;


        String tourInfoStr = (tourInfoCopy == null) ? "null" : "SubNode@" + Integer.toHexString(System.identityHashCode(tourInfoCopy));

        return "SubNode{" +
                "nodeInfo=" + nodeInfoCopy +
                ", tourInfo=" + tourInfoStr +
                ", subtree=" + subtreeCopy +
                ", type=" + typeCopy +
                ", cost=" + costCopy +
                '}';
    }

    public SubNode getNext() {

            return next;

    }

    public void setNext(SubNode next) {

            this.next = next;

    }





    private class Itr implements Iterator<SubNode>{
        private SubNode current;

        Itr(SubNode head){
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public SubNode next() {
            SubNode node = current;
            current = current.getTourInfo();
            return node;
        }
    }

    @Override
    public Iterator<SubNode> iterator() {
        return new Itr(this);
    }
}