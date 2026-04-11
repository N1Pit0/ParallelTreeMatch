package com.bachelor.algorithm;

import com.bachelor.algorithm.stringmatch.Kmp;
import com.bachelor.datastructure.*;
import com.bachelor.preprocess.EulerChain;

import java.util.HashMap;
import java.util.Map;

public class MTablesContainer {
    private final Map<Integer, Map<Integer, PermutationChain>> mTables;
    private final EulerChain subject;
    private final EulerChain pattern;
    private final Splice splice;

    MTablesContainer(Splice splice, EulerChain subject, EulerChain pattern) {
        this.splice = splice;
        this.subject = subject;
        this.pattern = pattern;
        this.mTables = new HashMap<>();
    }

    void createMTables() {
        int[][] splicesArray = splice.getSplices();
        StringMatch<PermutationChain> stringMatch = new Kmp();
        for (int i = 0; i < splicesArray.length; i++) {
            Map<Integer, PermutationChain> matches = stringMatch.matchString(subject, pattern, splicesArray[i][0], splicesArray[i][1]);
            mTables.put(i, matches);
        }
    }

    EulerChain getSubject() {
        return this.subject;
    }

    int getLength() {
        return mTables.size();
    }

    boolean isTableEntryEmpty(int tableIdx, int subjPos) {
        return mTables.get(tableIdx) == null || mTables.get(tableIdx).get(subjPos) == null
                || mTables.get(tableIdx).get(subjPos).isEmpty();
    }

    void writePermutationsIntoTable(Permutation permutation, int tableIndex, int subjectPos) {
        //I don't like adding straight into the head
        mTables.get(tableIndex).get(subjectPos).getHead().addPermutationToCurrentNode(permutation);
    }

    PermutationChain readPermutationsFromTable(int tableIndex, int subjectPos) {
        return mTables.get(tableIndex).get(subjectPos);
    }

    void clearTableEntry(int tableIdx, int subjPos) {
        mTables.get(tableIdx).remove(subjPos);
    }

    Map<Integer, PermutationChain> getFirstTable() {
        return this.mTables.get(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== mTables (3D Array - Dimension: [Slices][Subject Positions][Match Info]) ===\n");

        int firstTable = 0;

        sb.append("\nSlice M").append(firstTable).append(" (Pattern range [").append(splice.getSplices()[firstTable][0]).append(", ");
        sb.append(splice.getSplices()[firstTable][1]).append("]):\n");

        Map<Integer, PermutationChain> table = mTables.get(firstTable);

        // Print matches in a compact format
        boolean hasMatches = false;
        for (Integer i : table.keySet()) {
            if (!isTableEntryEmpty(firstTable, i)) {
                if (!hasMatches) {
                    sb.append("  Matches:\n");
                    hasMatches = true;
                }
                sb.append(readPermutationsFromTable(firstTable, i)).append("\n");
            }
        }

        if (!hasMatches) {
            sb.append("  No matches found\n");
        }

        return sb.toString();
    }

}
