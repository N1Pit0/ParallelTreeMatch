package com.bachelor.algorithm;

import com.bachelor.datastructure.Permutation;
import com.bachelor.datastructure.PermutationChain;
import com.bachelor.preprocess.EulerChain;
import com.bachelor.preprocess.SubNode;

import java.util.List;

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

        PermutationChain currentPermutations = mTablesContainer
                .readPermutationsFromTable(tableIdx,subjPos);

        List<Permutation> currentPermutationList = currentPermutations.getHead().getPermutations();
        Permutation currentPermutation = currentPermutationList.getFirst();
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

        PermutationChain nextPermutationchain =  mTablesContainer
                .readPermutationsFromTable(nextTableIdx, subtreeIdx);
        if (nextPermutationchain == null) {
            return false;
        }

        Permutation nextPermutation = nextPermutationchain.getHead().getPermutations().getFirst();

        Permutation updatedCurrentPermutation =
                new Permutation(currentPermutation.matchStart(), nextPermutation.matchEnd(),
                        currentPermutation.variable(), currentPermutation.replacement());
        currentPermutationList.remove(currentPermutation);
        mTablesContainer.writePermutationsIntoTable(updatedCurrentPermutation, tableIdx, subjPos);
        return true;
    }
}
