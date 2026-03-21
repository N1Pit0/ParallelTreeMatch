package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.SubNode;

@SuppressWarnings("ClassCanBeRecord")
class CommitTemporaryCosts implements Runnable{
    private final SubNode current;

    CommitTemporaryCosts(SubNode current) {
        this.current = current;
    }

    @Override
    public void run() {
        current.setCost(current.getCostTmp());
        current.setNext(current.getNextTmp());
    }
}
