package com.bachelor.algorithm;

import com.bachelor.datastructure.Permutation;
import com.bachelor.datastructure.PermutationNode;
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

    private PermutationNode collectAllMatches(PermutationNode positionMatchList) {
        PermutationNode permutations = new PermutationNode(positionMatchList.getMatchStartIndex());

        for (Permutation currentNode : positionMatchList.getPermutations()) {
            int nextPos = currentNode.matchEnd + 1;
            SubNode nodeAtNextPos = subjectChain[nextPos];

            if (subjectChain[nextPos].getType().equals(FIRST)
                    || subjectChain[nextPos].getType().equals(LEAF)) {
                int subtreeIdx = nodeAtNextPos.getSubtree() + 1;

                if (subtreeIdx >= subject.getChainSize()) {
                    continue;
                }

                PermutationNode nextPermutationNode = mTablesContainer
                        .readPermutationsFromTable(nextTableIdx, subtreeIdx);
                if (nextPermutationNode == null) {
                    continue;
                }

                for(Permutation nextNode : nextPermutationNode.getPermutations()){
                    //TODO: FINISH IT!!!
                }
            }
        }

        return permutations;
    }
}
