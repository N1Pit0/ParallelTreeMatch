package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.concurrent.ConcurrentLinkedDeque;

import static com.bachelor.preprocess.NodeType.*;

class TermVariableMatch implements Runnable{
    private final MTablesContainer mTablesContainer;
    private final int tableIdx;
    private final int subjPos;
    private final int offset;
    private final SubNode[] subjectChain;
    private final EulerChain subject;

    TermVariableMatch(MTablesContainer mTablesContainer, int tableIdx, int subjPos, int offset, EulerChain subject) {
        this.mTablesContainer = mTablesContainer;
        this.tableIdx = tableIdx;
        this.subjPos = subjPos;
        this.offset = offset;
        this.subject = subject;
        this.subjectChain = subject.getChain();
    }

    @Override
    public void run() {
        int nextTableIdx = tableIdx + offset; // i + 2^l

        if (nextTableIdx >= mTablesContainer.getLength()) {
            return;
        }

        if (!extendMatch(tableIdx, nextTableIdx)) {
            mTablesContainer.clearTableEntry(tableIdx, subjPos);
        }
    }

    //This matching works for term variable
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean extendMatch(int tableIdx, int nextTableIdx) {

        ConcurrentLinkedDeque<Permutation> currentPermutations = mTablesContainer
                .readPermutationsFromTable(tableIdx,subjPos);

        if(currentPermutations == null) return false;

        Permutation currentPermutation = currentPermutations.getFirst();
        int matchEnd = currentPermutation.matchEnd();

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

        if (mTablesContainer.isTableEntryEmpty(nextTableIdx, subtreeIdx)) {
            return false;
        }

        Permutation nextPermutation = mTablesContainer
                .readPermutationsFromTable(nextTableIdx, subtreeIdx).getFirst();
        Permutation updatedCurrentPermutation =
                new Permutation(currentPermutation.matchStart(), nextPermutation.matchEnd(),
                        currentPermutation.variable(), currentPermutation.replacement());
        mTablesContainer.writePermutationsIntoTable(updatedCurrentPermutation, tableIdx, subjPos);
        return true;
    }
}
