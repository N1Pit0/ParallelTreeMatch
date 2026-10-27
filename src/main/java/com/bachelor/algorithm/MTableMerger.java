package com.bachelor.algorithm;

import com.bachelor.datastructure.MatchRegistry;
import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.ExecutorBarrierUtils;
import com.bachelor.utils.TreePrintUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

class MTableMerger {
    private final MTablesContainer mTablesContainer;
    private final ExecutorService executor;
    private final EulerChain subject;
    private final EulerChain pattern;

    MTableMerger(MTablesContainer mTablesContainer, ExecutorService executor) {
        this.mTablesContainer = mTablesContainer;
        this.executor = executor;
        this.subject = mTablesContainer.getSubject();
        this.pattern = mTablesContainer.getPattern();
    }

    void computeAndMergeMTables() {
        mergeTables();

        validateFinalResults();

        TreePrintUtils.printMTablesFinalState("matches found", mTablesContainer);
    }

    private void mergeTables() {
        int tableCount = mTablesContainer.getLength();

        // Round with offset 2^(l-1): table i absorbs table i + offset, for i = 0, 2·offset, 4·offset, ...
        for (int offset = 1; offset < tableCount; offset *= 2) {
            List<VariableMatch> tasks = new LinkedList<>();

            for (int tableIdx = 0; tableIdx + offset < tableCount; tableIdx += 2 * offset) {
                int nextTableIdx = tableIdx + offset;
                for (int subjPos : mTablesContainer.entryPositions(tableIdx)) {
                    tasks.add(MatchingAlgorithmFactory.create(mTablesContainer, subject, pattern, tableIdx, nextTableIdx, subjPos));
                }
            }

            // All tasks of a round are independent (different tables, or different entries of the same table)
            ExecutorBarrierUtils.forEachIndex(executor, tasks.size(), i -> tasks.get(i).run());
        }
    }

    private void validateFinalResults() {
        SubNode[] subjectChain = subject.getChain();

        ExecutorBarrierUtils.forEachIndex(executor, subjectChain.length,
                index -> new ValidateResultMtableStep(mTablesContainer, subjectChain, index).run());
    }
}
