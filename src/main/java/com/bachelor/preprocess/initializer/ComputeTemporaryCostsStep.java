package com.bachelor.preprocess.initializer;

import com.bachelor.preprocess.SubNode;

class ComputeTemporaryCosts implements Runnable {
    private final SubNode current;

    ComputeTemporaryCosts(SubNode current) {
        this.current = current;
    }

    @Override
    public void run() {
        SubNode next = current.getNext();

        if (next != null) {
            current.setCostTmp(
                    current.getCost() + next.getCost()
            );

            current.setNextTmp(
                    next.getNext()
            );
        } else {
            current.setCostTmp(current.getCost());
            current.setNextTmp(null);
        }
    }
}
