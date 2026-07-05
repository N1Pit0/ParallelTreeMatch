package com.bachelor.algorithm;

import com.bachelor.datastructure.*;
import com.bachelor.preprocess.SubNode;

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

    @Override
    public boolean extendMatch() {
        return false;
    }

    private MatchRegistry collectAllMatches(MatchRegistry positionMatchList) {
        MatchRegistry permutations = new MatchRegistry();

        for (MatchInterval currentNode : positionMatchList.getPermutations()) {
            int nextPos = currentNode.matchEnd + 1;
            SubNode nodeAtNextPos = subjectChain[nextPos];

            if (subjectChain[nextPos].getType().equals(FIRST)
                    || subjectChain[nextPos].getType().equals(LEAF)) {
                int subtreeIdx = nodeAtNextPos.getSubtree() + 1;

                if (subtreeIdx >= subject.getChainSize()) {
                    continue;
                }

                MatchRegistry nextPermutationNode = mTablesContainer
                        .readPermutationsFromTable(nextTableIdx, subtreeIdx);
                if (nextPermutationNode == null) {
                    continue;
                }

                for(MatchInterval nextNode : nextPermutationNode.getPermutations()){
                    //TODO: FINISH IT!!!
                }
            }
        }

        return permutations;
    }
}
