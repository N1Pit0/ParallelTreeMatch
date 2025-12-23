package com.bachelor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class SubNode {
    private final Lock readLock;
    private final Lock writeLock;

    // -> T[i] where i is the index of the node in the Euler chain.
    private int nodeInfo; // reference to the corresponding TreeNode inside the T array.

    private SubNode tourInfo; // a reference to the next element (used to form the linked list) in the Euler chain.

    private int subtree; // used for storing the last occurrence of node i in the Euler chain.

    //specifies whether the j_th occurrence of i is the first or last occurrence of i in the Euler chain.
    private NodeType type; // Used to indicate whether i is a leaf node. In case i is not a leaf then this field

    //    cost is used in the prefix computation to compute the rank (position)
//    of each entry in the tour field. At the end, each entry of T [i].tour will
//    be placed in an array E at a position given by the respective cost
//    field
    private int cost;

    public SubNode(ReadWriteLock lock) {
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public int getNodeInfo() {
        try {
            readLock.lock();
            return nodeInfo;
        } finally {
            readLock.unlock();
        }
    }

    public void setNodeInfo(int nodeInfo) {
        try {
            writeLock.lock();
            this.nodeInfo = nodeInfo;
        } finally {
            writeLock.unlock();
        }
    }

    public SubNode getTourInfo() {
        try {
            readLock.lock();
            return tourInfo;
        } finally {
            readLock.unlock();
        }
    }

    public void setTourInfo(SubNode tourInfo) {
        try {
            writeLock.lock();
            this.tourInfo = tourInfo;
        } finally {
            writeLock.lock();
        }
    }

    int getSubtree() {
        return subtree;
    }

    void setSubtree(int subtree) {
        this.subtree = subtree;
    }

    NodeType getType() {
        return type;
    }

    void setType(NodeType type) {
        this.type = type;
    }

    int getCost() {
        return cost;
    }

    void setCost(int cost) {
        this.cost = cost;
    }
}