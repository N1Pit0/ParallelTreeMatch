package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.*;
import com.bachelor.utils.TreeProcessingUtils;

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

        PermutationNode currentPermutations = mTablesContainer
                .readPermutationsFromTable(tableIdx,subjPos);

        List<Permutation> currentPermutationList = currentPermutations.getPermutations();
        Permutation currentPermutation = currentPermutationList.getFirst();
        int matchEnd = currentPermutation.matchEnd;

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

        PermutationNode nextTablePermutationNode =  mTablesContainer
                .readPermutationsFromTable(nextTableIdx, subtreeIdx);
        if (nextTablePermutationNode == null) {
            return false;
        }

        Permutation nextTablePermutation = nextTablePermutationNode.getPermutations().getFirst();

        int newMatchStart = currentPermutation.matchStart;
        int newMatchEnd = nextTablePermutation.matchEnd;
        String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, newMatchStart, newMatchEnd);

        currentPermutation.matchEnd = newMatchEnd;
        currentPermutation.variableReplacement = newReplacement;
        //I am not sure if clearing this entry does any good.
        //Idea is that I am freeing up the memory.
        mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);

        return true;
    }
}
