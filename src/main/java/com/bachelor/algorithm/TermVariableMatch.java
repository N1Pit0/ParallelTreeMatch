package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.*;
import com.bachelor.utils.TreeProcessingUtils;

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

        // TODO: Need to look into it. Why are we taking first?
        // Answer: TermVariable match could only have a single permutation per MTable entry
        Permutation currentPermutation = currentPermutations.getPermutations().getFirst();
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

        //Find and replace variable
        int variableMatchStart = currentPermutation.matchEnd + 1;
        int variableMatchEnd = nextTablePermutation.matchStart;
        String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, variableMatchStart, variableMatchEnd);

        Variable variableOrder = pattern.getInputArray().getVariableWithIndex(nextTableIdx-1);
        VariableReplacement variableReplacement = new VariableReplacement(variableOrder, newReplacement);
        currentPermutations.addToVariableReplacements(variableReplacement);

        currentPermutation.matchEnd = nextTablePermutation.matchEnd;

        //I am not sure if clearing this entry does any good.
        //Idea is that I am freeing up the memory.
        mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);

        return true;
    }
}
