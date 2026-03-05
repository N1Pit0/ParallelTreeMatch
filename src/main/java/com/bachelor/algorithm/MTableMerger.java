package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

import static com.bachelor.preprocess.NodeType.*;

class MTableMerger {
    private final MTables mTablesContainer;
    private final ExecutorService executor;
    private final EulerChain subject;

    MTableMerger(MTables mTablesContainer, ExecutorService executor, Phaser phaser) {
        this.mTablesContainer = mTablesContainer;
        this.executor = executor;
        this.subject = mTablesContainer.getSubject();
    }

    void computeAndMergeMTables(){
        int[][][] mTables = mTablesContainer.getMTables();
        SubNode[] subjectChain = subject.getChain();

        step2RecursiveMergeTables(mTables, subjectChain);

        step3ValidateFinalResults(mTables, subjectChain);

        printMTablesState("After Step 3 (validation - final result)", mTablesContainer);
    }

    /**
     * STEP 2: Recursively merges the r resulting tables from Step 1
     * Uses binary lifting to combine tables: in each iteration, combine tables
     * at exponential distances (2^1, 2^2, ...) until only table 0 remains
     */
    private void step2RecursiveMergeTables(int[][][] mTables, SubNode[] subjectChain) {

        int currentSize = mTables.length;
        int depth = 1;

        while (currentSize > 1 ) {

            int offset = 1 << (depth - 1);
            int jumpSize = offset * 2;

            for (int i = 0; i < mTables.length; i+=jumpSize) {
                for (int j = 0; j < subject.getChainSize(); j++) {
                    // If M_i[j] has a valid match
                    if (mTables[i][j][0] != -1) {
                        int nextTableIdx = i + offset; // i + 2^l

                        if (nextTableIdx >= mTables.length) {
                            continue;
                        }

                        if (!extendMatch(mTables, subjectChain, i, j, nextTableIdx)) {
                            mTables[i][j][0] = mTables[i][j][1] = -1;
                        }
                    }
                }
            }

            currentSize = (currentSize + 1) / 2;
            depth++;
        }

    }

    /**
     * STEP 3: Finally we check whether C₁ is satisfied
     * For each i ∈ {1,2,...,Eₛ}:
     *   if (M₁[i][1] ≠ 0) then
     *     if (E[M₁[i][2]].type ≠ first ∧ E[M₁[i][2]].type ≠ last) then
     *       M₁[i][1] = M₁[i][2] = 0 (invalidate)
     */
    private void step3ValidateFinalResults(int[][][] mTables, SubNode[] subjectChain) {
        int[][] finalTable = mTables[0];

        for (int i = 0; i < finalTable.length; i++) {
            if (finalTable[i][0] != -1) {
                int startPos = finalTable[i][0];
                int endPos = finalTable[i][1];

                if (endPos >= 0 && endPos < subjectChain.length) {
                    SubNode startNode = subjectChain[startPos];
                    SubNode endNode = subjectChain[endPos];

                    if (startNode.getType() != FIRST || endNode.getType() != LAST) {
                        finalTable[i][0] = finalTable[i][1] = -1;
                    }
                } else {
                    finalTable[i][0] = finalTable[i][1] = -1;
                }
            }
        }
    }

    /**
     * Extends a match from M_i[j] by attempting to merge with M_(i+2^l)[...]
     * Implements the substitution logic: if the position after the current match
     * is a variable node (FIRST or LEAF), try to find a continuation in the next table.
     */
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

    /**
     * Helper method to print mTables state (factored out printing logic).
     */
    private void printMTablesState(String label, MTables container) {
        System.out.println("\n" + label + ":");
        System.out.println(container);
    }
}
