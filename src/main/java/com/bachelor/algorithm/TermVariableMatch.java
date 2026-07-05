package com.bachelor.algorithm;

import com.bachelor.datastructure.MatchInterval;
import com.bachelor.datastructure.MatchRegistry;
import com.bachelor.datastructure.VariableReplacement;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.TreeProcessingUtils;

import java.util.NoSuchElementException;

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
    public boolean extendMatch() {
        try {

            MatchRegistry currentRegistry = mTablesContainer
                    .readPermutationsFromTable(tableIdx,subjPos);

            MatchInterval currentMatchInterval = currentRegistry.getMatchIntervals().getFirst();
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

            MatchInterval nextTableMatchInterval = nextTableMatchRegistry.getMatchIntervals().getFirst();

            //Find and replace variable
            int variableMatchStart = currentMatchInterval.matchEnd + 1;
            int variableMatchEnd = nextTableMatchRegistry.matchStart;
            String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, variableMatchStart, variableMatchEnd);

            VariableReplacement variableReplacement = new VariableReplacement(variableToBeReplaced, newReplacement);
            currentMatchInterval.addToVariableReplacements(variableReplacement);
            currentMatchInterval.addAllToVariableReplacements(nextTableMatchRegistry.getMatchIntervalWithIndex(0).getVariableReplacements());

            currentMatchInterval.matchEnd = nextTableMatchInterval.matchEnd;

            //I am not sure if clearing this entry does any good.
            //Idea is that I am freeing up the memory.
            mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return true;
    }
}
