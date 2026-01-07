package com.bachelor.preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EulerChain {

    private final List<SubNode> CHAIN;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();
    private final TreeNode[] T;

    public EulerChain(InputArray inputArray){
        this.CHAIN = new ArrayList<>();
        this.T = inputArray.getT();
    }

    public void setAtIndex(int index, SubNode subNode){
        try{
            writeLock.lock();
            while(CHAIN.size() <= index){
                CHAIN.add(null);
            }
            CHAIN.set(index, subNode);
        }finally {
            writeLock.unlock();
        }
    }

    public SubNode getFromIndex(int index){
        try{
            readLock.lock();
            return CHAIN.get(index);
        }finally {
            readLock.unlock();
        }
    }

    public int getChainSize() {
        return CHAIN.size();
    }

    public TreeNode[] getT() {
        return T;
    }
}
