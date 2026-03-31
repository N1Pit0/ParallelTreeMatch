package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.SubNode;

@SuppressWarnings("ClassCanBeRecord")
class CommitTemporaryCostsStep implements Runnable{
    private final SubNode current;

    CommitTemporaryCostsStep(SubNode current) {
        this.current = current;
    }

    @Override
    public void run() {
        current.setCost(current.getCostTmp());
        current.setNext(current.getNextTmp());
    }
}
