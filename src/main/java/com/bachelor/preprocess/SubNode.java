package com.bachelor.preprocess;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubNode implements Iterable<SubNode>{
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();

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
        readLock.lock();
        try {
            return nodeInfo;
        } finally {
            readLock.unlock();
        }
    }

    public void setNodeInfo(int nodeInfo) {
        writeLock.lock();
        try {
            this.nodeInfo = nodeInfo;
        } finally {
            writeLock.unlock();
        }
    }

    public SubNode getTourInfo() {
        readLock.lock();
        try {
            return tourInfo;
        } finally {
            readLock.unlock();
        }
    }

    public void setTourInfo(SubNode tourInfo) {
        writeLock.lock();
        try {
            this.tourInfo = tourInfo;
        } finally {
            writeLock.unlock();
        }
    }

    public int getSubtree() {
        readLock.lock();
        try {
            return subtree;
        } finally {
            readLock.unlock();
        }
    }

    public void setSubtree(int subtree) {
        writeLock.lock();
        try {
            this.subtree = subtree;
        } finally {
            writeLock.unlock();
        }
    }

    public NodeType getType() {
        readLock.lock();
        try {
            return type;
        } finally {
            readLock.unlock();
        }
    }

    public void setType(NodeType type) {
        writeLock.lock();
        try {
            this.type = type;
        } finally {
            writeLock.unlock();
        }
    }

    public int getCost() {
        readLock.lock();
        try{
            return cost;
        } finally {
            readLock.unlock();
        }
    }

    public void setCost(int cost) {
        writeLock.lock();
        try{
            this.cost = cost;
        } finally {
            writeLock.unlock();
        }
    }

    public int getCostWithoutLock() {
        return cost;
    }

    public void setCostWithoutLock(int cost) {
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
        readLock.lock();
        try {
            nodeInfoCopy = this.nodeInfo;
            tourInfoCopy = this.tourInfo;
            subtreeCopy = this.subtree;
            typeCopy = this.type;
            costCopy = this.cost;
        } finally {
            readLock.unlock();
        }

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
        readLock.lock();
        try{
            return next;
        } finally {
            readLock.unlock();
        }
    }

    public void setNext(SubNode next) {
        writeLock.lock();
        try{
            this.next = next;
        } finally {
            writeLock.unlock();
        }
    }

    public SubNode getNextWithoutLock() {
        return next;
    }

    public void setNextWithoutLock(SubNode next) {
        this.next = next;
    }

    public Lock getWriteLock(){
        return this.writeLock;
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