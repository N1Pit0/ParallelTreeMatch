package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

public abstract class VariableMatch implements Runnable {
    protected final MTablesContainer mTablesContainer;
    protected final int tableIdx;
    protected final int subjPos;
    protected final int nextTableIdx;
    protected final SubNode[] subjectChain;
    protected final EulerChain subject;
    protected final EulerChain pattern;

    VariableMatch(Builder<?> builder) {
        this.mTablesContainer = builder.mTablesContainer;
        this.tableIdx = builder.tableIdx;
        this.subjPos = builder.subjPos;
        this.nextTableIdx = builder.nextTableIdx;
        this.subject = builder.subject;
        this.subjectChain = subject.getChain();
        this.pattern = builder.pattern;
    }

    abstract static class Builder<T extends Builder<T>>{
        protected MTablesContainer mTablesContainer;
        protected int tableIdx;
        protected int subjPos;
        protected int nextTableIdx;
        protected EulerChain subject;
        protected EulerChain pattern;

        T mTablesContainer(MTablesContainer mTablesContainer){
            this.mTablesContainer = mTablesContainer;
            return self();
        }

        T subject(EulerChain subject){
            this.subject = subject;
            return self();
        }

        T pattern(EulerChain pattern){
            this.pattern = pattern;
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
