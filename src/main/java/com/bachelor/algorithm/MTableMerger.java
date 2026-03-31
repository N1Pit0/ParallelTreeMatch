package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

class MTableMerger {
    private final MTablesContainer mTablesContainer;
    private final ExecutorService executor;
    private final EulerChain subject;

    MTableMerger(MTablesContainer mTablesContainer, ExecutorService executor) {
        this.mTablesContainer = mTablesContainer;
        this.executor = executor;
        this.subject = mTablesContainer.getSubject();
    }

    void computeAndMergeMTables() {
        int[][][] mTables = mTablesContainer.getMTables();
        SubNode[] subjectChain = subject.getChain();

        mergeTables(mTables, executor);

        validateFinalResults(mTables, subjectChain, executor);

        TreePrintUtils.printMTablesState("match", mTablesContainer);
    }

    private void mergeTables(int[][][] mTables, ExecutorService executor) {

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

                        for (int subjPos = 0; subjPos < subject.getChainSize(); subjPos++) {
                            if (mTables[tableIdx][subjPos][0] != -1) {
                                positionTasks.add(new PerformMatch(mTables, tableIdx, subjPos, offset, subject));
                            }
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
            entryValidationTasks.add(new ValidateResultMtableStep(finalTable, subjectChain, i));
        }
        ExecutorBarrierUtils.invokeAll(executor, entryValidationTasks);
    }
}
