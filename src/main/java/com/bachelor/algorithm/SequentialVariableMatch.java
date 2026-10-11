package com.bachelor.algorithm;

import com.bachelor.datastructure.MatchInterval;
import com.bachelor.datastructure.MatchRegistry;
import com.bachelor.datastructure.VariableReplacement;
import com.bachelor.preprocess.SequentialVariable;
import com.bachelor.preprocess.SubNode;
import com.bachelor.utils.TreeProcessingUtils;

import static com.bachelor.preprocess.NodeType.FIRST;
import static com.bachelor.preprocess.NodeType.LEAF;

class SequentialVariableMatch extends VariableMatch {

    private SequentialVariableMatch(Builder builder) {
        super(builder);
    }

    static class Builder extends VariableMatch.Builder<Builder> {

        @Override
        VariableMatch build() {
            return new SequentialVariableMatch(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    //TODO: This code is a mess. I am not sure what is going on here and I do not like this many hard-copying collections this many times
    @Override
    public boolean extendMatch() {
//        MatchRegistry posMatchList = collectAllMatches(mTablesContainer.readPermutationsFromTable(tableIdx, subjPos));
//
//        mTablesContainer.writePermutationNodeIntoTable(posMatchList, tableIdx, subjPos);
//        if (posMatchList.isEmpty()) {
//            return false;
//        }
//
//        if (variableToBeReplaced instanceof SequentialVariable) {
//            MatchRegistry currentNode = mTablesContainer.readPermutationsFromTable(tableIdx, subjPos);
//            posMatchList = currentNode;
//            while (!posMatchList.isEmpty()) {
//                posMatchList = collectAllMatches(posMatchList);
//                currentNode.concat(posMatchList);
//            }
//        }

        return true;
    }

//    private MatchRegistry collectAllMatches(MatchRegistry currentMatchRegistry) {
//        MatchRegistry result = new MatchRegistry();
//
//        for (MatchInterval currentMatchInterval : currentMatchRegistry.getMatchIntervals()) {
//            int variableMatchStart = currentMatchInterval.matchEnd + 1;
//            if (variableMatchStart >= subjectChain.length) continue;
//            SubNode variableMatchStartNode = subjectChain[variableMatchStart];
//
//            if (subjectChain[variableMatchStart].getType().equals(FIRST)
//                    || subjectChain[variableMatchStart].getType().equals(LEAF)) {
//                int subtreeIdx = variableMatchStartNode.getSubtree() + 1;
//
//                if (subtreeIdx >= subject.getChainSize()) {
//                    continue;
//                }
//
//                MatchRegistry nextTableMatchRegistry = mTablesContainer
//                        .readPermutationsFromTable(nextTableIdx, subtreeIdx);
//                if (nextTableMatchRegistry == null) {
//                    continue;
//                }
//
//                for (MatchInterval nextTableMatchInterval : nextTableMatchRegistry.getMatchIntervals()) {
//                    //Find and replace variable
//                    int variableMatchEnd = nextTableMatchRegistry.matchStart;
//                    String newReplacement = TreeProcessingUtils.getReplacementForVar(subject, variableMatchStart, variableMatchEnd);
//
//                    VariableReplacement variableReplacement = new VariableReplacement(variableToBeReplaced, newReplacement);
//                    result.addToVariableReplacements(variableReplacement);
//
//                    MatchInterval newMatchInterval = new MatchInterval.Builder()
//                            .matchStart(currentMatchRegistry.matchStart)
//                            .matchEnd(nextTableMatchInterval.matchEnd)
//                            .build();
//
//                    result.addPermutationToCurrentNode(newMatchInterval);
//                }
//                result.concat(nextTableMatchRegistry);
//                //I am not sure if clearing this entry does any good.
//                //Idea is that I am freeing up the memory.
//                mTablesContainer.clearTableEntry(nextTableIdx, subtreeIdx);
//            }
//        }
//        result.concat(currentMatchRegistry);
//
//        return result;
//    }
}
