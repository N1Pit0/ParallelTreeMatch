package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.*;

import java.util.List;

import static com.bachelor.preprocess.NodeType.*;

class TermVariableMatch extends VariableMatch {

    private TermVariableMatch(Builder builder) {
        super(builder);
    }

    static class Builder extends VariableMatch.Builder<Builder>{

        @Override
        VariableMatch build() {
            return new TermVariableMatch(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    //This matching works for term variable
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean extendMatch() {

        PermutationChain currentPermutations = mTablesContainer
                .readPermutationsFromTable(tableIdx,subjPos);

        List<Permutation> currentPermutationList = currentPermutations.getHead().getPermutations();
        Permutation currentPermutation = currentPermutationList.getFirst();
        int matchEnd = currentPermutation.matchEnd();

        int nextPosInSubject = matchEnd + 1;

        if (nextPosInSubject >= subject.getChainSize()) {
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
