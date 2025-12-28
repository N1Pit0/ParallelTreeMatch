package com.bachelor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class TreeNode {

    private static final Logger logger = LoggerFactory.getLogger(TreeNode.class);
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();

    //contains either a function symbol or a variable associated with the
    //node
    private String label;

    //contains a pointer to the parent of the node.
    private int father = 0;

    //  contains an integer specifying the node’s ordering relative to
//  its sibling, i.e., which argument of its parent the current node is.
    private int edge_label;

    //  contains the outdegree (the number of outgoing edges) of the
//  node.
    private final int outDegree;

    //  an array containing n + 1 elements, where n is the outdegree of the
    //  current node in the tree. Needs initialization
    final SubNode[] tour;

    TreeNode(int outDegree) {
        this.outDegree = outDegree;
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
        logger.debug("Inside the TreeNode for father {}", this.father);
        return this.father;
    }

}
