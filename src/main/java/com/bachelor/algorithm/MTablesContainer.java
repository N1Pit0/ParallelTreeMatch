package com.bachelor.algorithm;

import com.bachelor.algorithm.stringmatch.Kmp;
import com.bachelor.datastructure.*;
import com.bachelor.preprocess.EulerChain;

import java.util.HashMap;
import java.util.Map;

public class MTablesContainer {
    private final Map<Integer, Map<Integer, MatchRegistry>> mTables;
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
        StringMatch<MatchRegistry> stringMatch = new Kmp();
        for (int i = 0; i < splicesArray.length; i++) {
            Map<Integer, MatchRegistry> matches = stringMatch.matchString(subject, pattern, splicesArray[i][0], splicesArray[i][1]);
            mTables.put(i, matches);
        }
    }

    EulerChain getSubject() {
        return this.subject;
    }

    EulerChain getPattern(){return this.pattern;}

    int getLength() {
        return mTables.size();
    }

    boolean isTableEntryEmpty(int tableIdx, int subjPos) {
        return mTables.get(tableIdx) == null || mTables.get(tableIdx).get(subjPos) == null
                || mTables.get(tableIdx).get(subjPos).isEmpty();
    }

    void writePermutationsIntoTable(MatchInterval matchInterval, int tableIndex, int subjectPos) {
        mTables.get(tableIndex).get(subjectPos).addMatchIntervalToCurrentNode(matchInterval);
    }

    void writePermutationNodeIntoTable(MatchRegistry matchRegistry, int tableIndex, int subjectPos){
        MatchRegistry oldValue = mTables.get(tableIndex).get(subjectPos);
        mTables.get(tableIndex).replace(subjectPos, oldValue, matchRegistry);
    }

    MatchRegistry readPermutationsFromTable(int tableIndex, int subjectPos) {
        return mTables.get(tableIndex).get(subjectPos);
    }

    void clearTableEntry(int tableIdx, int subjPos) {
        mTables.get(tableIdx).remove(subjPos);
    }

    void clearTable(int tableIdx){
        mTables.remove(tableIdx);
    }

    Map<Integer, MatchRegistry> getFirstTable() {
        return this.mTables.get(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== mTables (3D Array - Dimension: [Slices][Subject Positions][Match Info]) ===\n");

        int firstTable = 0;

        sb.append("\nSlice M").append(firstTable).append(" (Pattern range [").append(splice.getSplices()[firstTable][0]).append(", ");
        sb.append(splice.getSplices()[firstTable][1]).append("]):\n");

        Map<Integer, MatchRegistry> table = mTables.get(firstTable);

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
