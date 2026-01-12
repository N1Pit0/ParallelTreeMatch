package com.bachelor.preprocess;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EulerChain {

    private final SubNode[] CHAIN;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();
    private final TreeNode[] T;

    public EulerChain(InputArray inputArray){
        this.T = inputArray.getT();
        int size = 0;
        for (var elem : T){
            size += elem.arity() + 1;
        }
        this.CHAIN = new SubNode[size];
    }

    public void setAtIndex(int index, SubNode subNode){
        try{
            writeLock.lock();
            CHAIN[index] = subNode;
        }finally {
            writeLock.unlock();
        }
    }

    public SubNode getFromIndex(int index){
        try{
            readLock.lock();
            return CHAIN[index];
        }finally {
            readLock.unlock();
        }
    }

    public int getChainSize() {
        return CHAIN.length;
    }

    public TreeNode[] getT() {
        return T;
    }
}
