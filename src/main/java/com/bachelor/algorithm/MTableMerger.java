package com.bachelor.algorithm;

import com.bachelor.datastructure.PermutationChain;
import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        mergeTables();

        validateFinalResults();

        TreePrintUtils.printMTablesState("match", mTablesContainer);
    }

    private void mergeTables() {

        int mTablesLength = mTablesContainer.getLength();
        final int[] currentSize = {mTablesLength};
        final int[] depth = {1};

        while (currentSize[0] > 1) {
            List<Runnable> depthIterationTasks = new LinkedList<>();

            depthIterationTasks.add(() -> {
                int offset = 1 << (depth[0] - 1);
                int jumpSize = offset * 2;

                List<Runnable> tablePairTasks = new LinkedList<>();

                for (int i = 0; i < mTablesLength; i += jumpSize) {
                    final int tableIdx = i;
                    tablePairTasks.add(() -> {
                        List<Runnable> positionTasks = new LinkedList<>();

                        for (int subjPos = 0; subjPos < subject.getChainSize(); subjPos++) {
                            PermutationChain currentPermutations = mTablesContainer
                                    .readPermutationsFromTable(tableIdx,subjPos);

                            if (currentPermutations != null) {
                                positionTasks.add(new TermVariableMatch(mTablesContainer, tableIdx, subjPos, offset, subject));
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

    private void validateFinalResults() {
        Map<Integer, PermutationChain> finalTable = mTablesContainer.getFirstTable();
        SubNode[] subjectChain = subject.getChain();

        List<Runnable> entryValidationTasks = new LinkedList<>();

        for (int i = 0; i < finalTable.size(); i++) {
            entryValidationTasks.add(new ValidateResultMtableStep(mTablesContainer, subjectChain, i));
        }
        ExecutorBarrierUtils.invokeAll(executor, entryValidationTasks);
    }
}
