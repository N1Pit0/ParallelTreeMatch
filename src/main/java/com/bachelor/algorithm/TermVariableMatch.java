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

        MatchRegistry currentPermutations = mTablesContainer
                .readPermutationsFromTable(tableIdx,subjPos);

        MatchInterval currentMatchInterval = currentPermutations.getPermutations().getFirst();
        int matchEnd = currentMatchInterval.matchEnd;

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

        MatchRegistry nextTableMatchRegistry =  mTablesContainer
                .readPermutationsFromTable(nextTableIdx, subtreeIdx);
        if (nextTableMatchRegistry == null) {
            return false;
        }

        MatchInterval nextTableMatchInterval = nextTableMatchRegistry.getPermutations().getFirst();

        //Find and replace variable
        int variableMatchStart = currentMatchInterval.matchEnd + 1;
        int variableMatchEnd = nextTableMatchInterval.matchStart;
        String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, variableMatchStart, variableMatchEnd);

        VariableReplacement variableReplacement = new VariableReplacement(variableToBeReplaced, newReplacement);
        currentPermutations.addToVariableReplacements(variableReplacement);
        currentPermutations.addAllToVariableReplacements(nextTableMatchRegistry.getVariableReplacements());

        currentMatchInterval.matchEnd = nextTableMatchInterval.matchEnd;

        //I am not sure if clearing this entry does any good.
        //Idea is that I am freeing up the memory.
        mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);

        return true;
    }
}
