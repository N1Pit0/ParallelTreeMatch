package com.bachelor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class TreeNode {

    private final boolean isRoot;
    private final Lock readLock;
    private final Lock writeLock;

    //contains either a function symbol or a variable associated with the
    //node
    String label;

    //contains a pointer to the parent of the node.
    private int father;

    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    private int edge_label;

    public int getEdge_label(){
        try{
            readLock.lock();
            return this.edge_label;
        }finally {
            readLock.unlock();
        }
    }

    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    private final int outDegree;

    //  an array containing n + 1 elements, where n is the outdegree of the
    //  current node in the tree. Needs initialization
    public final SubNode[] tour;

    TreeNode(int outDegree, boolean isRoot, ReadWriteLock lock) {
        this.outDegree = outDegree;
        this.isRoot = isRoot;
        tour = new SubNode[outDegree + 1];
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
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
