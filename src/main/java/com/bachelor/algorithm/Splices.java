package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Splices {
    private final int[][] splices;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final Lock readLock = LOCK.readLock();
    private final Lock writeLock = LOCK.writeLock();
    private final Logger LOGGER = LoggerFactory.getLogger(Splices.class);

    public Splices(EulerChain eulerChain){
        int size = eulerChain.getInputArray().getVariableCount();
        splices = new int[size+1][2];
    }

    public int readFromIndex(int index, int position){
        try{
            readLock.lock();
            return splices[index][position];
        } catch (ArrayIndexOutOfBoundsException e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException();
        }
        finally {
            readLock.unlock();
        }
    }

    public void writeAtIndex(int index, int position, int value){
        try{
            writeLock.lock();
            splices[index][position] = value;
        } catch (ArrayIndexOutOfBoundsException e){
          LOGGER.error(e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

    public int[][] getSplices() {
        return splices;
    }
}
