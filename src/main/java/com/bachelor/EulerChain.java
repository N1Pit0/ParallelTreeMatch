package com.bachelor;

import java.util.concurrent.locks.Lock;

import static com.bachelor.Coordinator.LOCK;
import static com.bachelor.InputArray.T;

public class EulerChain {

    //CHAIN needs some kind of synchronization.
    private static final SubNode[] CHAIN = new SubNode[T.length];
    private static final Lock readLock = LOCK.readLock();
    private static final Lock writeLock = LOCK.writeLock();

    public static void setAtIndex(int index, SubNode subNode){
        try{
            writeLock.lock();
            CHAIN[index] = subNode;
        }finally {
            writeLock.unlock();
        }
    }

    public static SubNode getFromIndex(int index){
        try{
            readLock.lock();
            return CHAIN[index];
        }finally {
            readLock.unlock();
        }
    }

}
