package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import static com.bachelor.preprocess.NodeType.FIRST;
import static com.bachelor.preprocess.NodeType.LEAF;

class PerformMatch implements Runnable{
    private final int[][][] mTables;
    private final int tableIdx;
    private final int subjPos;
    private final int offset;
    private final SubNode[] subjectChain;
    private final EulerChain subject;

    PerformMatch(int[][][] mTables, int tableIdx, int subjPos, int offset, EulerChain subject) {
        this.mTables = mTables;
        this.tableIdx = tableIdx;
        this.subjPos = subjPos;
        this.offset = offset;
        this.subject = subject;
        this.subjectChain = subject.getChain();
    }

    @Override
    public void run() {
        int nextTableIdx = tableIdx + offset; // i + 2^l

        if (nextTableIdx >= mTables.length) {
            return;
        }

        if (!extendMatch(mTables, subjectChain, tableIdx, subjPos, nextTableIdx)) {
            mTables[tableIdx][subjPos][0] = mTables[tableIdx][subjPos][1] = -1;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean extendMatch(int[][][] mTables, SubNode[] subjectChain,
                                int tableIdx, int subjectPos, int nextTableIdx) {
        int matchEnd = mTables[tableIdx][subjectPos][1];
        int nextPosInSubject = matchEnd + 1;

        if (nextPosInSubject >= subjectChain.length) {
            return false;
        }

        SubNode nodeAtNextPos = subjectChain[nextPosInSubject];

        if (nodeAtNextPos.getType() != FIRST && nodeAtNextPos.getType() != LEAF) {
            return false;
        }

        int subtreeIdx = nodeAtNextPos.getSubtree() + 1;

        if (subtreeIdx >= subject.getChainSize()) {
            return false;
        }

        if (mTables[nextTableIdx][subtreeIdx][0] == -1) {
            return false;
        }

        int newEnd = mTables[nextTableIdx][subtreeIdx][1];
        mTables[tableIdx][subjectPos][1] = newEnd;
        return true;
    }
}
