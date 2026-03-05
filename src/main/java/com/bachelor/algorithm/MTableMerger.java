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
        int k = mTables.length - 1;
        int logK = (int) Math.ceil(Math.log(k) / Math.log(2));
        int r = (int) Math.ceil((k + 1) /(double) logK);

//        printMTablesState("Initial mTables (after KMP)", mTablesContainer);

        // STEP 1: Combine each of the log k tables
        // For i ∈ {1,2,...,r}, j ∈ {1,2,...,Eₛ}, l = 1 to log k
        // Merges tables spaced 2^l apart: M[i] with M[i+2], M[i+4], M[i+8], ...
//        step1CombineLogKTables(mTables, subjectChain, logK, r, k);

//        printMTablesState("After Step 1 (combine log k tables)", mTablesContainer);

        // STEP 2: Recursively merge the r resulting tables
        // Tables M₁logₖ, M₂logₖ, ..., Mᵣlogₖ are recursively merged using binary lifting
        // for l = 1 to log r: if (i + 2^l ≤ r) then combine M[i] with M[i+2^l]
        step2RecursiveMergeTables(mTables, subjectChain, r);

//        printMTablesState("After Step 2 (recursive merge)", mTablesContainer);

        // STEP 3: Validate final results
        // Check that non-null entries in M₁ satisfy constraints:
        // - E[M₁[i][2]].type must be FIRST or LAST
        step3ValidateFinalResults(mTables, subjectChain);

//        printMTablesState("After Step 3 (validation - final result)", mTablesContainer);
    }

    /**
     * STEP 1: Combines each of the log k tables
     * For each table group i, extends matches by combining with tables at distance 2^l
     * After this step, we have r tables instead of k
     */
    private void step1CombineLogKTables(int[][][] mTables, SubNode[] subjectChain,
                                       int logK, int r, int k) {
        // for each i ∈ {1,2,...,r} pardo
        for (int i = 0; i < mTables.length; i+=logK) {
            // for each j ∈ {1,2,...,Eₛ} pardo
            for (int j = 0; j < subject.getChainSize(); j++) {

                if (mTables[i][j][0] != -1) {
                    int l = 1;
                    do {
                        int nextTableIdx = i + l;

                        if (nextTableIdx >= k) {
                            break;
                        }

                        // Check condition: E[M_i[j][2] + 1].type = first ∨ E[M_i[j][2] + 1].type = leaf
                        if (!extendMatch(mTables, subjectChain, i, j, nextTableIdx)) {
                            // Extend failed, invalidate entry
                            mTables[i][j][0] = mTables[i][j][1] = -1;
                            break;
                        }
                        l++;
                    }while(l < logK);
                }
            }
        }
    }

    /**
     * STEP 2: Recursively merges the r resulting tables from Step 1
     * Uses binary lifting to combine tables: in each iteration, combine tables
     * at exponential distances (2^1, 2^2, ...) until only table 0 remains
     */
    private void step2RecursiveMergeTables(int[][][] mTables, SubNode[] subjectChain, int r) {
        if (r <= 1) {
            return; // Only one table, no merging needed
        }

        int currentSize = mTables.length;
        int depth = 1;

        while (currentSize > 1 ) {

            int offset = 1 << (depth - 1);
            int jumpSize = offset * 2;

            // For each table i that we're currently processing
            for (int i = 0; i < mTables.length; i+=jumpSize) {
                // For each subject position j
                for (int j = 0; j < subject.getChainSize(); j++) {
                    // If M_i[j] has a valid match
                    if (mTables[i][j][0] != -1) {
                        // Try to extend using tables at distances 2^l
                        int nextTableIdx = i + offset; // i + 2^l

                        if (nextTableIdx > jumpSize || nextTableIdx >= mTables.length) {
                            continue; // There is a bug here. Not being able to check the other half of the table
                        }

                        // Try to extend the match
                        if (!extendMatch(mTables, subjectChain, i, j, nextTableIdx)) {
                            // Extension failed, invalidate this entry
                            mTables[i][j][0] = mTables[i][j][1] = -1;
//                            break;
                        }
                    }
                }
            }

            String label = String.format("After merging tables with offset %d (depth %d)", offset, depth);
            printMTablesState(label, mTablesContainer);
            // Prepare for next iteration:
            // After merging, tables at indices 0, 2, 4, 6, ... contain all the info
            // So the new "active" set becomes tables 0, 1, 2, 3, ... (ceil(currentSize/2))
            int nextSize = (currentSize + 1) / 2;
            depth++;

            // Copy data from merged tables back to lower indices for next iteration
            // Tables at indices [2i] and [2i+1] are merged into [i]
            // But in our in-place algorithm, we don't need to copy - just track size

            currentSize = nextSize;
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
        int[][] finalTable = mTables[0]; // M₁ is the first (merged) table

        for (int i = 0; i < finalTable.length; i++) {
            // if (M_1[i][1] ≠ 0) then
            if (finalTable[i][0] != -1) {
                int startPos = finalTable[i][0];
                int endPos = finalTable[i][1];

                // Verify bounds
                if (endPos >= 0 && endPos < subjectChain.length) {
                    SubNode startNode = subjectChain[startPos];
                    SubNode endNode = subjectChain[endPos];

                    // if (E[M₁[i][2]].type ≠ first ∧ E[M₁[i][2]].type ≠ last) then
                    if (startNode.getType() != FIRST && endNode.getType() != LAST) {
                        finalTable[i][0] = finalTable[i][1] = -1;
                    }
                } else {
                    // Out of bounds, invalidate
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
//        int matchStart = mTables[tableIdx][subjectPos][0];
        int matchEnd = mTables[tableIdx][subjectPos][1];
        int nextPosInSubject = matchEnd + 1;

        // Check if we can look at the next position
        if (nextPosInSubject >= subjectChain.length) {
            return false;
        }

        SubNode nodeAtNextPos = subjectChain[nextPosInSubject];

        // Check: position after match must be FIRST or LEAF (variable node)
        if (nodeAtNextPos.getType() != FIRST && nodeAtNextPos.getType() != LEAF) {
            return false;
        }

        // Get subtree index: node's subtree position + 1
        int subtreeIdx = nodeAtNextPos.getSubtree() + 1;

        // Bounds check
        if (subtreeIdx >= subject.getChainSize()) {
            return false;
        }

        // Check if next table has a match at this subtree position
        if (mTables[nextTableIdx][subtreeIdx][0] == -1) {
            return false;
        }

        // Success: extend this match with the next table's match
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
