package com.bachelor.algorithm;

import com.bachelor.preprocess.*;

import java.util.Objects;

/**
 * An implementation of the Knuth-Morris-Pratt algorithm
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 * @author Nikolozi Matsaberidze
 */

class Kmp {

    // Given a pattern and a text kmp finds all the places that the pattern
    // is found in the text (even overlapping pattern matches)
    static int[][] kmp(EulerChain subject, EulerChain pattern, int patStart, int patEnd) {
        int subjectChainSize = subject.getChainSize();

        int[][] matches = new int[subjectChainSize][2];
        for (int i = 0; i < subjectChainSize; i++) {
            matches[i][0] = -1;
            matches[i][1] = -1;
        }

        // Validate slice bounds
        if (patEnd < patStart || patStart < 0 || patEnd >= pattern.getChainSize()) {
            return matches;
        }

        // Pattern slice length (inclusive range)
        int patternLength = patEnd - patStart + 1;
        int currentSubjectPosIndex = 0;
        int currentPatternPosIndex = 0;  // j tracks position within the pattern slice, starting from 0

        // Build failure function for the pattern slice
        int[] failureFunction = kmpFailureHelper(pattern, patStart, patEnd);

        while (currentSubjectPosIndex < subjectChainSize) {
            if (currentPatternPosIndex >= 0 && isNodeLabelMatch(subject, pattern, currentSubjectPosIndex, patStart + currentPatternPosIndex)) {
                currentPatternPosIndex++;
                currentSubjectPosIndex++;
            }

            if (currentPatternPosIndex == patternLength) {
                // Match found: record start and end indices
                int matchStart = currentSubjectPosIndex - patternLength;
                int matchEnd = currentSubjectPosIndex - 1;
                if (matchStart >= 0 && matchStart < subjectChainSize) {
                    matches[matchStart][0] = matchStart;
                    matches[matchStart][1] = matchEnd;
                }
                // Continue searching for overlapping matches
                currentPatternPosIndex = failureFunction[currentPatternPosIndex - 1];

            } else if (currentSubjectPosIndex < subjectChainSize && !isNodeLabelMatch(subject, pattern, currentSubjectPosIndex, patStart + currentPatternPosIndex)) {
                if (currentPatternPosIndex > 0) {
                    currentPatternPosIndex = failureFunction[currentPatternPosIndex - 1];
                } else {
                    currentSubjectPosIndex++;
                }
            }
        }

        return matches;
    }

    // For each index suffixStartPos compute the longest match between the proper
    // prefix starting at 0 and the proper suffix starting at suffixStartPos
    // Builds the failure function for a slice of the pattern from patStart to patEnd (inclusive)
    private static int[] kmpFailureHelper(EulerChain pattern, int patStart, int patEnd) {
        int patternLength = patEnd - patStart + 1;
        int[] failureFunction = new int[patternLength];

        for (int suffixStartPos = 1, prefixStartPos = 0; suffixStartPos < patternLength; ) {
            if (isNodeLabelMatch(pattern, pattern, patStart + suffixStartPos, patStart + prefixStartPos)) {
                failureFunction[suffixStartPos++] = ++prefixStartPos;
            } else {
                if (prefixStartPos > 0) {
                    prefixStartPos = failureFunction[prefixStartPos - 1];
                } else {
                    suffixStartPos++;
                }
            }
        }
        return failureFunction;
    }

    private static boolean isNodeLabelMatch(EulerChain aChain, EulerChain chainComparedAgainst,
                                            int aChainPosIndex,
                                            int chainComparedAgainstPosIndex) {
        SubNode[] subjectChain = aChain.getChain();
        SubNode[] patternChain = chainComparedAgainst.getChain();
        TreeNode[] subjectT = aChain.getT();
        TreeNode[] patternT = chainComparedAgainst.getT();

        try {
            SubNode subjectNode = subjectChain[aChainPosIndex];
            SubNode patternNode = patternChain[chainComparedAgainstPosIndex];
            return Objects.equals(subjectT[subjectNode.getNodeInfo()].getLabel(),
                    patternT[patternNode.getNodeInfo()].getLabel());
        } catch (NullPointerException e) {
            System.out.printf("aChainPostIndex: %d\n", aChainPosIndex);
            System.out.printf("chainComparedAgainstPosIndex: %d\n", chainComparedAgainstPosIndex);
            throw new RuntimeException(e);
        }
    }

}
