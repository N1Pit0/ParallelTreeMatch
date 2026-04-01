package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MTablesContainer {
    private final Map<Integer, Map<Integer, ConcurrentLinkedDeque<Permutation>>> mTables;
    private final boolean[][] isValueInTableEntry;
    private final EulerChain subject;
    private final EulerChain pattern;
    private final Splice splice;

    MTablesContainer(Splice splice, EulerChain subject, EulerChain pattern) {
        this.splice = splice;
        this.subject = subject;
        this.pattern = pattern;
        this.mTables = new HashMap<>();
        this.isValueInTableEntry = new boolean[splice.getSplices().length][subject.getChainSize()];
    }

    void createMTables() {
        int[][] splicesArray = splice.getSplices();
        for (int i = 0; i < splicesArray.length; i++) {
            Map<Integer, ConcurrentLinkedDeque<Permutation>> matches = Kmp.kmp(subject, pattern, splicesArray[i][0], splicesArray[i][1]);
            mTables.put(i, matches);
        }
    }

    EulerChain getSubject() {
        return this.subject;
    }

    int getLength() {
        return mTables.size();
    }

    boolean isTableEntryEmpty(int tableIndex, int subjectPos) {
        return !this.isValueInTableEntry[tableIndex][subjectPos];
    }

    void clearTableEntry(int tableIndex, int subjectPos) {
        this.isValueInTableEntry[tableIndex][subjectPos] = false;
    }

    private void assignValueToTableEntry(int tableIndex, int subjectPos) {
        this.isValueInTableEntry[tableIndex][subjectPos] = true;
    }

    void writePermutationsIntoTable(Permutation permutation, int tableIndex, int subjectPos) {
        assignValueToTableEntry(tableIndex, subjectPos);
        mTables.get(tableIndex).get(subjectPos).add(permutation);
    }

    ConcurrentLinkedDeque<Permutation> readPermutationsFromTable(int tableIndex, int subjectPos) {
        var a = mTables.get(tableIndex).get(subjectPos);
        return a;
    }

    Map<Integer, ConcurrentLinkedDeque<Permutation>> getFirstTable() {
        return this.mTables.get(0);
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("=== mTables (3D Array - Dimension: [Slices][Subject Positions][Match Info]) ===\n");
//
//        if (mTables == null) {
//            return "mTables=null";
//        }
//
//        for (int s = 0; s < mTables.length; s++) {
//            sb.append("\nSlice M").append(s).append(" (Pattern range [").append(splice.getSplices()[s][0]).append(", ");
//            sb.append(splice.getSplices()[s][1]).append("]):\n");
//
//            int[][] table = mTables[s];
//            if (table == null) {
//                sb.append("  null\n");
//                continue;
//            }
//
//            // Print matches in a compact format
//            boolean hasMatches = false;
//            for (int i = 0; i < table.length; i++) {
//                if (table[i][0] != -1) {
//                    if (!hasMatches) {
//                        sb.append("  Matches:\n");
//                        hasMatches = true;
//                    }
//                    sb.append("    Position ").append(i).append(": [").append(table[i][0]).append(", ");
//                    sb.append(table[i][1]).append("] (length: ").append(table[i][1] - table[i][0] + 1).append(")\n");
//                }
//            }
//
//            if (!hasMatches) {
//                sb.append("  No matches found\n");
//            }
//        }
//
//        return sb.toString();
//    }

}
