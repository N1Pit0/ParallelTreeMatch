package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.ExecutorBarrierUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;


import static com.bachelor.preprocess.NodeType.*;

class MTableMerger {
    private final MTables mTablesContainer;
    private final ExecutorService executor;
    private final EulerChain subject;

    MTableMerger(MTables mTablesContainer, ExecutorService executor) {
        this.mTablesContainer = mTablesContainer;
        this.executor = executor;
        this.subject = mTablesContainer.getSubject();
    }

    void computeAndMergeMTables() {
        int[][][] mTables = mTablesContainer.getMTables();
        SubNode[] subjectChain = subject.getChain();

        mergeTables(mTables, subjectChain, executor);

        validateFinalResults(mTables, subjectChain, executor);

        printMTablesState("After Step 3 (validation - final result)", mTablesContainer);
    }


    private void mergeTables(int[][][] mTables, SubNode[] subjectChain, ExecutorService executor) {

        final int[] currentSize = {mTables.length};
        final int[] depth = {1};

        while (currentSize[0] > 1) {
            List<Runnable> depthIterationTasks = new LinkedList<>();

            depthIterationTasks.add(() -> {
                int offset = 1 << (depth[0] - 1);
                int jumpSize = offset * 2;

                List<Runnable> tablePairTasks = new LinkedList<>();

                for (int i = 0; i < mTables.length; i += jumpSize) {
                    final int tableIdx = i;
                    tablePairTasks.add(() -> {
                        List<Runnable> positionTasks = new LinkedList<>();

                        for (int j = 0; j < subject.getChainSize(); j++) {
                            final int subjPos = j;
                            positionTasks.add(() -> {
                                // If M_i[j] has a valid match
                                if (mTables[tableIdx][subjPos][0] != -1) {
                                    int nextTableIdx = tableIdx + offset; // i + 2^l

                                    if (nextTableIdx >= mTables.length) {
                                        return;
                                    }

                                    if (!extendMatch(mTables, subjectChain, tableIdx, subjPos, nextTableIdx)) {
                                        mTables[tableIdx][subjPos][0] = mTables[tableIdx][subjPos][1] = -1;
                                    }
                                }
                            });
                        }

                        ExecutorBarrierUtils.invokeAll(executor, positionTasks);
                    });
                }

                ExecutorBarrierUtils.invokeAll(executor, tablePairTasks);

                currentSize[0] = (currentSize[0] + 1) / 2;
                depth[0]++;
            });

            ExecutorBarrierUtils.invokeAll(executor, depthIterationTasks);
        }
    }

    private void validateFinalResults(int[][][] mTables, SubNode[] subjectChain, ExecutorService executor) {
        int[][] finalTable = mTables[0];

        List<Runnable> entryValidationTasks = new java.util.ArrayList<>();

        for (int i = 0; i < finalTable.length; i++) {
            final int entryIdx = i;
            entryValidationTasks.add(() -> {
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
            });
        }
        ExecutorBarrierUtils.invokeAll(executor, entryValidationTasks);
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
    @SuppressWarnings("SameParameterValue")
    private void printMTablesState(String label, MTables container) {
        System.out.println("\n" + label + ":");
        System.out.println(container);
    }
}
