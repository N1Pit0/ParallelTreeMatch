package com.bachelor.algorithm;

import com.bachelor.preprocess.SubNode;

import static com.bachelor.preprocess.NodeType.*;

class ValidateResultMtableStep implements Runnable{
    private final int[][] finalTable;
    private final SubNode[] subjectChain;
    private final int entryIdx;

    ValidateResultMtableStep(int[][] finalTable, SubNode[] subjectChain, int entryIdx) {
        this.finalTable = finalTable;
        this.subjectChain = subjectChain;
        this.entryIdx = entryIdx;
    }

    @Override
    public void run() {
        if (finalTable[entryIdx][0] != -1) {
            int startPos = finalTable[entryIdx][0];
            int endPos = finalTable[entryIdx][1];

            if (endPos >= 0 && endPos < subjectChain.length) {
                SubNode startNode = subjectChain[startPos];
                SubNode endNode = subjectChain[endPos];

                if (startNode.getType() != FIRST || endNode.getType() != LAST) {
                    finalTable[entryIdx][0] = finalTable[entryIdx][1] = -1;
                }
            } else {
                finalTable[entryIdx][0] = finalTable[entryIdx][1] = -1;
            }
        }
    }
}
