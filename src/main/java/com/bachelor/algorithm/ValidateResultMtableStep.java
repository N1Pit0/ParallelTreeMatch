package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.NodeType;
import com.bachelor.preprocess.SubNode;

import static com.bachelor.preprocess.NodeType.*;

/**
 * Keeps an entry of table 0 only if the match is one complete subtree of the subject:
 * it starts at a FIRST or LEAF occurrence and ends exactly where that node's subtree ends.
 */
class ValidateResultMtableStep implements Runnable {
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
        MatchRegistry registry = mTablesContainer.readPermutationsFromTable(0, entryIdx);
        if (registry == null) {
            return;
        }
        if (registry.isEmpty()) {
            mTablesContainer.clearTableEntry(0, entryIdx);
            return;
        }

        int startPos = registry.matchStart;
        //getFirst() is a potential bug for sequential matching algorithm
        int endPos = registry.getMatchIntervals().getFirst().matchEnd;

        if (!isCompleteSubtree(startPos, endPos)) {
            mTablesContainer.clearTableEntry(0, entryIdx);
        }
    }

    private boolean isCompleteSubtree(int start, int end) {
        if (start < 0 || end < start || end >= subjectChain.length) {
            return false;
        }
        SubNode startNode = subjectChain[start];
        NodeType type = startNode.getType();
        return (type == FIRST || type == LEAF) && startNode.getSubtree() == end;
    }
}