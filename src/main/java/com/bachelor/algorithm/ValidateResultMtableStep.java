package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.SubNode;

import static com.bachelor.preprocess.NodeType.*;

class ValidateResultMtableStep implements Runnable{
    private final MTablesContainer mTablesContainer;
    private final SubNode[] subjectChain;
    private final int entryIdx;

    ValidateResultMtableStep(MTablesContainer mTablesContainer, SubNode[] subjectChain, int entryIdx) {
        this.mTablesContainer = mTablesContainer;
        this.subjectChain = subjectChain;
        this.entryIdx = entryIdx;
    }

    @Override
    public void run() {
        MatchRegistry currentMatchRegistry = mTablesContainer
                .readPermutationsFromTable(0,entryIdx);

        if (currentMatchRegistry == null) {
            return;
        }

        MatchInterval currentMatchInterval = currentMatchRegistry
                .getMatchIntervals().getFirst();
        int startPos = currentMatchRegistry.matchStart;
        int endPos = currentMatchInterval.matchEnd;

        if (endPos >= 0 && endPos < subjectChain.length) {
            SubNode startNode = subjectChain[startPos];
            SubNode endNode = subjectChain[endPos];

            if (startNode.getType() != FIRST || endNode.getType() != LAST) {
                mTablesContainer.clearTableEntry(0, entryIdx);
            }
        } else {
            mTablesContainer.clearTableEntry(0, entryIdx);
        }
    }
}
