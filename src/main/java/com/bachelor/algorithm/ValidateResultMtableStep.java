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
        PermutationChain currentPermutations = mTablesContainer
                .readPermutationsFromTable(0,entryIdx);

        if (currentPermutations == null) {
            return;
        }

        Permutation currentPermutation = currentPermutations
                .getHead().getPermutations().getFirst();
        int startPos = currentPermutation.matchStart();
        int endPos = currentPermutation.matchEnd();

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
