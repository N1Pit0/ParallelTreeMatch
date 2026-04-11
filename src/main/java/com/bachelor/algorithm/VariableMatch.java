package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.preprocess.Variable;

public abstract class VariableMatch implements Runnable {
    protected final MTablesContainer mTablesContainer;
    protected final int tableIdx;
    protected final int subjPos;
    protected final int nextTableIdx;
    protected final SubNode[] subjectChain;
    protected final EulerChain subject;

    VariableMatch(Builder<?> builder) {
        this.mTablesContainer = builder.mTablesContainer;
        this.tableIdx = builder.tableIdx;
        this.subjPos = builder.subjPos;
        this.nextTableIdx = builder.nextTableIdx;
        this.subject = builder.subject;
        this.subjectChain = subject.getChain();
    }

    abstract static class Builder<T extends Builder<T>>{
        protected MTablesContainer mTablesContainer;
        protected int tableIdx;
        protected int subjPos;
        protected int nextTableIdx;
        protected EulerChain subject;
        protected Variable variable;

        T mTablesContainer(MTablesContainer mTablesContainer){
            this.mTablesContainer = mTablesContainer;
            return self();
        }

        T eulerChain(EulerChain eulerChain){
            this.subject = eulerChain;
            return self();
        }

        T tableIdx(int tableIdx){
            this.tableIdx = tableIdx;
            return self();
        }

        T subjPos(int subjPos){
            this.subjPos = subjPos;
            return self();
        }

        T nextTableIndex(int nextTableIdx){
            this.nextTableIdx = nextTableIdx;
            return self();
        }

        abstract VariableMatch build();

        protected abstract T self();
    }

    @Override
    public void run(){
        if (!extendMatch()) {
            mTablesContainer.clearTableEntry(tableIdx, subjPos);
        }
    }

    public abstract boolean extendMatch();
}
