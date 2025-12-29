package com.bachelor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EulerChain {

    //CHAIN needs some kind of synchronization.
    private final SubNode[] CHAIN;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();

    public EulerChain(InputArray inputArray){
        this.CHAIN = new SubNode[inputArray.getT().length]; //It needs different Length
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

}
