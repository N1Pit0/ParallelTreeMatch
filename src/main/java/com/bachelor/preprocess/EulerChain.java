package com.bachelor.preprocess;

import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EulerChain {

    private final SubNode[] CHAIN;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final InputArray inputArray;
    private final TreeNode[] T;

    @Override
    public String toString(){
        return Arrays.toString(CHAIN);
    }

    public EulerChain(InputArray inputArray){
        this.inputArray = inputArray;
        this.T = inputArray.getT();
        int size = 0;
        for (var elem : T){
            size += elem.arity() + 1;
        }
        this.CHAIN = new SubNode[size];
    }

    public void setAtIndex(int index, SubNode subNode){
            CHAIN[index] = subNode;
    }

    public SubNode getFromIndex(int index){
            return CHAIN[index];
    }

    public int getChainSize() {
        return CHAIN.length;
    }

    public SubNode[] getChain(){
        return CHAIN;
    }

    public TreeNode[] getT() {
        return T;
    }

    public InputArray getInputArray(){
        return this.inputArray;
    }
}
