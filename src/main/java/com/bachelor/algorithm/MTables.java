package com.bachelor.algorithm;

import com.bachelor.preprocess.EulerChain;

class MTables {
    private final int[][][] mTables;
    private final EulerChain subject;
    private final EulerChain pattern;
    private final Splice splice;

    MTables(Splice splice, EulerChain subject, EulerChain pattern) {
        this.splice = splice;
        this.subject = subject;
        this.pattern = pattern;
        this.mTables = new int[splice.getSplices().length][subject.getChainSize()][2];
    }

    void createMTables(){
        int[][] splicesArray = splice.getSplices();
        for (int i = 0; i < mTables.length; i++) {
            mTables[i] = Kmp.kmp(subject, pattern, splicesArray[i][0], splicesArray[i][1]);
        }
    }

    int[][][] getMTables() {
        return mTables;
    }

    EulerChain getSubject(){
        return this.subject;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== mTables (3D Array - Dimension: [Slices][Subject Positions][Match Info]) ===\n");

        if (mTables == null) {
            return "mTables=null";
        }

        for (int s = 0; s < mTables.length; s++) {
            sb.append("\nSlice M").append(s).append(" (Pattern range [").append(splice.getSplices()[s][0]).append(", ");
            sb.append(splice.getSplices()[s][1]).append("]):\n");

            int[][] table = mTables[s];
            if (table == null) {
                sb.append("  null\n");
                continue;
            }

            // Print matches in a compact format
            boolean hasMatches = false;
            for (int i = 0; i < table.length; i++) {
                if (table[i][0] != -1) {
                    if (!hasMatches) {
                        sb.append("  Matches:\n");
                        hasMatches = true;
                    }
                    sb.append("    Position ").append(i).append(": [").append(table[i][0]).append(", ");
                    sb.append(table[i][1]).append("] (length: ").append(table[i][1] - table[i][0] + 1).append(")\n");
                }
            }

            if (!hasMatches) {
                sb.append("  No matches found\n");
            }
        }

        return sb.toString();
    }

}
