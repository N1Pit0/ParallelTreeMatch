package com.bachelor.preprocess;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubNode {
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();

    // -> T[i] where i is the index of the node in the Euler chain.
    private int nodeInfo; // reference to the corresponding TreeNode inside the T array.

    private SubNode tourInfo; // a reference to the next element (used to form the linked list) in the Euler chain.

    private int subtree; // used for storing the last occurrence of node i in the Euler chain.

    //specifies whether the j_th occurrence of i is the first or last occurrence of i in the Euler chain.
    private NodeType type; // Used to indicate whether "i" is a leaf node. In case "i" is not a leaf then this field

    //    cost is used in the prefix computation to compute the rank (position)
//    of each entry in the tour field. At the end, each entry of T [i].tour will
//    be placed in an array E at a position given by the respective cost
//    field
    private int cost;

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
            writeLock.unlock();
        }
    }

    public int getSubtree() {
        try {
            readLock.lock();
            return subtree;
        } finally {
            readLock.unlock();
        }
    }

    public void setSubtree(int subtree) {
        try {
            writeLock.lock();
            this.subtree = subtree;
        } finally {
            writeLock.unlock();
        }
    }

    public NodeType getType() {
        try {
            readLock.lock();
            return type;
        } finally {
            readLock.unlock();
        }
    }

    public void setType(NodeType type) {
        try {
            writeLock.lock();
            this.type = type;
        } finally {
            writeLock.unlock();
        }
    }

    public int getCost() {
        try{
            readLock.lock();
            return cost;
        } finally {
            readLock.unlock();
        }
    }

    public void setCost(int cost) {
        try{
            writeLock.lock();
            this.cost = cost;
        } finally {
            writeLock.unlock();
        }
    }
}