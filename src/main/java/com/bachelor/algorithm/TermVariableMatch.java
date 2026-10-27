package com.bachelor.algorithm;

import com.bachelor.datastructure.MatchInterval;
import com.bachelor.datastructure.MatchRegistry;
import com.bachelor.datastructure.VariableReplacement;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.TreeProcessingUtils;

import static com.bachelor.preprocess.NodeType.FIRST;
import static com.bachelor.preprocess.NodeType.LEAF;

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
    @Override
    public boolean extendMatch() {
        MatchRegistry currentRegistry = mTablesContainer.readPermutationsFromTable(tableIdx, subjPos);
        if (currentRegistry == null || currentRegistry.getMatchIntervals().isEmpty()) {
            return false;
        }

        MatchInterval currentMatchInterval = currentRegistry.getMatchIntervals().getFirst();
        int nextPosInSubject = currentMatchInterval.matchEnd + 1;

        if (nextPosInSubject >= subject.getChainSize()) {
            return false;
        }

        SubNode nodeAtNextPos = subjectChain[nextPosInSubject];
        if (nodeAtNextPos.getType() != FIRST && nodeAtNextPos.getType() != LEAF) {
            return false;
        }

        int subtreeIdx = nodeAtNextPos.getSubtree() + 1;
        if (subtreeIdx > subject.getChainSize()) {
            return false;
        }

        MatchRegistry nextRegistry = mTablesContainer.readPermutationsFromTable(nextTableIdx, subtreeIdx);
        if (nextRegistry == null || nextRegistry.getMatchIntervals().isEmpty()) {
            return false;
        }

        MatchInterval nextMatchInterval = nextRegistry.getMatchIntervals().getFirst();

        // Find and replace variable
        int variableMatchStart = currentMatchInterval.matchEnd + 1;
        int variableMatchEnd = nextRegistry.matchStart;
        String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, variableMatchStart, variableMatchEnd);

        currentMatchInterval.addToVariableReplacements(new VariableReplacement(variableToBeReplaced, newReplacement));
        currentMatchInterval.addAllToVariableReplacements(nextMatchInterval.getVariableReplacements());
        currentMatchInterval.matchEnd = nextMatchInterval.matchEnd;

        // Frees memory. Only safe if no other task reads this entry concurrently.
        mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);

        return true;
    }
}
