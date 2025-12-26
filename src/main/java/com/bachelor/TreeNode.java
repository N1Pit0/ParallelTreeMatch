package com.bachelor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static com.bachelor.Coordinator.LOCK;

public class TreeNode {

    private final boolean isRoot;
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();

    //contains either a function symbol or a variable associated with the
    //node
    private String label;

    //contains a pointer to the parent of the node.
    private int father;

    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    private int edge_label;

    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    private final int outDegree;

    //  an array containing n + 1 elements, where n is the outdegree of the
    //  current node in the tree. Needs initialization
    final SubNode[] tour;

    TreeNode(int outDegree, boolean isRoot, ReadWriteLock lock) {
        this.outDegree = outDegree;
        this.isRoot = isRoot;
        tour = new SubNode[outDegree + 1];
    }

    public int getEdge_label() {
        try {
            readLock.lock();
            return this.edge_label;
        } finally {
            readLock.unlock();
        }
    }

    public int arity(){
        return outDegree;
    }

    //Father probably does not need synchronization. Should be final. Will change it later
    public int getFather() {
        try {
            readLock.lock();
            if (this.isRoot) return -1; // Signals the Thread to stop the process since root does not have a father
            return this.father;
        }finally {
            readLock.unlock();
        }
    }

}
